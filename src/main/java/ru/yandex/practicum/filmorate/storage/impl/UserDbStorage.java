package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@Component
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> findUserById(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from users where user_id = ?", id);
        if (userRows.next()) {
            User user = new User(userRows.getInt("user_id"), userRows.getString("email"), userRows.getString("login"), userRows.getString("name"), userRows.getDate("birthday").toLocalDate());
            log.info("Найден пользователь: {} {}", user.getId(), user.getLogin());
            return Optional.of(user);
        } else {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new ObjectNotFoundException("Пользователь с идентификатором " + id + " не найден.");
        }
    }

    @Override
    public Collection<User> findAllUsers() {
        String sql = "select * from users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public Collection<User> findUsersFriends(int id) {
        String sql = "select * from users where user_id in (select friend_id from user_friend where user_id = ?)";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id);
    }

    @Override
    public Collection<User> findCommonFriends(int id, int otherId) {
        String sql = "select * from users where user_id in (select friend_id from user_friend where user_id = ? and friend_id in (select friend_id from user_friend where user_id = ?))";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id, otherId);
    }

    @Override
    public boolean removeUser(int id) {
        String sql = "delete from users WHERE user_id = ? ";
        log.info("Пользователь с id: {} удален", id);
        return jdbcTemplate.update(sql, id) > 0;
    }

    @Override
    public User createUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("users").usingGeneratedKeyColumns("user_id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue());
        log.info("Создан пользователь: {} {}", user.getId(), user.getLogin());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sql = "update users set email = ?, login = ?, name = ?, birthday = ? where user_id = ?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        log.info("Пользователь с id: {} обновлен", user.getId());
        return user;
    }

    @Override
    public void addFriend(int id, int friendId) {
        String sqlCheck = "select * from user_friend where user_id = ? and friend_id = ? or friend_id = ? and user_id = ?";
        // запрос на получение всех записей, в которых есть id и первого пользователя, и второго
        log.info("Проверка запросов на дружбу пользователей с id: {} и {}", id, friendId);
        Collection<Friends> friends = jdbcTemplate.query(sqlCheck, (rs, rowNum) -> new Friends(rs.getInt("user_id"), rs.getInt("friend_id")), id, friendId, id, friendId);
        if (friends.isEmpty()) {
            // если ни одной пары не найдено, добавляем запись
            String sql = "insert into user_friend (user_id, friend_id) values (?, ?)";
            jdbcTemplate.update(sql, id, friendId);
            log.info("Пользователь с id: {} добавил в друзья пользователя с id: {}", id, friendId);
        } else if (friends.size() == 2) {
            // если в списке две записи (1 | 2  и 2 | 1 ) значит оба пользователя отправили запросы на дружбу
            throw new ValidateException("Вы уже являетесь друзьями");
        } else {
            // остался вариант, в котором в таблице одна запись. Определяем, кто отправил запрос на дружбу
            for (Friends friendPair : friends) {
                if (friendPair.getUserId() == id) {
                    throw new ValidateException("Вы уже отправили запрос этому пользователю");
                } else {
                    String sql = "insert into user_friend (user_id, friend_id) values (?, ?)";
                    jdbcTemplate.update(sql, id, friendId);
                    log.info("Пользователь с id: {} добавил в друзья пользователя с id: {}", id, friendId);
                }
            }
        }
    }

    @Override
    public void removeFriend(int id, int friendId) {
        String sqlCheck = "select * from user_friend where user_id = ? and friend_id = ? or friend_id = ? and user_id = ?";
        // запрос на получение всех записей, в которых есть id и первого пользователя, и второго
        log.info("Проверка запросов на дружбу пользователей с id: {} и {}", id, friendId);
        Collection<Friends> friends = jdbcTemplate.query(sqlCheck, (rs, rowNum) -> new Friends(rs.getInt("user_id"), rs.getInt("friend_id")), id, friendId, id, friendId);
        if (friends.isEmpty()) {
            throw new ValidateException("Вы не отправляли пользователю запрос на дружбу");
        } else if (friends.size() == 2) {
            // если запросы были с обеих сторон
            String sql = "delete from user_friend where user_id = ? and friend_id = ?";
            jdbcTemplate.update(sql, id, friendId);
            log.info("Запрос на дружбу пользователя с id: {} к пользователю с id: {} удален", id, friendId);
        } else {
            for (Friends friendPair : friends) {
                if (friendPair.getUserId() == id) {
                    String sql = "delete from user_friend where user_id = ? and friend_id = ?";
                    jdbcTemplate.update(sql, id, friendId);
                    log.info("Запрос на дружбу пользователя с id: {} к пользователю с id: {} удален", id, friendId);
                } else {
                    throw new ValidateException("Вы не отправляли пользователю запрос на дружбу");
                }
            }
        }
    }

    private User makeUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("user_id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();

        return new User(id, email, login, name, birthday);
    }
}