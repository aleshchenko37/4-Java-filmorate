package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Film> findFilmById(int id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from film where film_id = ?", id);
        if (filmRows.next()) {
            Film film = new Film(filmRows.getInt("film_id"), filmRows.getString("name"), filmRows.getString("description"), filmRows.getDate("release_date").toLocalDate(), filmRows.getInt("duration"), getRate(id), getMpa(id), getGenres(id));
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            return Optional.of(film);
        } else {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new ObjectNotFoundException("Фильм с идентификатором " + id + " не найден.");
        }
    }

    @Override
    public Collection<Film> findAllFilms() {
        String sql = "select * from film";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public Film createFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("film").usingGeneratedKeyColumns("film_id");
        // тот же фильм, но с id. mpa и жанры пока не добавлены
        film.setId(simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue());

        if (film.getMpa() != null) {
            String sqlQuery = "insert into film_rating(film_id, rating_id) values (?, ?)";
            jdbcTemplate.update(sqlQuery, film.getId(), film.getMpa().getId());
        }

        if (film.getGenres() != null) {
            for (Integer genreId : getUniqueGenres(film.getGenres())) {
                String genreQuery = "insert into film_genre(film_id, genre_id) values (?, ?)";
                jdbcTemplate.update(genreQuery, film.getId(), genreId);
            }
        }
        //возвращаем исходный объект присвоенным id, рейтинг сохранен, жанры сохранены
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "update film set name = ?, description = ?, release_date = ?, duration = ? where film_id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getId());
        if (film.getRate() != 0) {
            // если в фильме передан рейтинг, сохраняем его, если нет - рассчитываем
            String sqlRate = "update film set rate = ? where film_id = ?";
            jdbcTemplate.update(sqlRate, film.getRate(), film.getId());
        } else {
            String sqlRate = "update film set rate = ? where film_id = ?";
            jdbcTemplate.update(sqlRate, getRate(film.getId()), film.getId());
        }
        if (film.getMpa() != null) {
            String sqlMpa = "update film_rating set rating_id = ? where film_id = ?";
            jdbcTemplate.update(sqlMpa, film.getMpa().getId(), film.getId());
        }

        if (film.getGenres() != null) {
            // удаляем все старые записи о жанрах
            String deleteSql = "delete from film_genre where film_id = ?";
            jdbcTemplate.update(deleteSql, film.getId());
            for (Integer genreId : getUniqueGenres(film.getGenres())) {
                String sqlGenre = "insert into film_genre(film_id, genre_id) values (?, ?)";
                jdbcTemplate.update(sqlGenre, film.getId(), genreId);
            }
        }
        log.info("Фильм с id: {} обновлен", film.getId());
        return findFilmById(film.getId()).get();
    }

    @Override
    public void addLike(int filmId, int userId) {
        SqlRowSet checkRows = jdbcTemplate.queryForRowSet("select * from film_user where film_id = ? and user_id = ?", filmId, userId);
        if (checkRows.next()) {
            throw new ValidateException("Вы уже поставили лайк этому фильму");
        } else {
            String sqlQuery = "insert into film_user(film_id, user_id) values (?, ?)";
            jdbcTemplate.update(sqlQuery, filmId, userId);
            // обновляем рейтинг в таблице film
            String sqlRating = "update film set rate = ? where film_id = ?";
            jdbcTemplate.update(sqlRating, getRate(filmId), filmId);
            log.info("Пользователь с id: {} поставил лайк фильму с id: {}", userId, filmId);
        }
    }

    @Override
    public void removeLike(int filmId, int userId) {
        SqlRowSet checkRows = jdbcTemplate.queryForRowSet("select * from film_user where film_id = ? and user_id = ?", filmId, userId);
        log.info("Проверка поставленных лайков пользователя с id: {} фильму с id {}", userId, filmId);
        if (checkRows.next()) {
            String sql = "delete from film_user where film_id = ? and user_id = ?";
            jdbcTemplate.update(sql, filmId, userId);
            // обновляем рейтинг в таблице film
            String sqlRating = "update film set rate = ? where film_id = ?";
            jdbcTemplate.update(sqlRating, getRate(filmId), filmId);
            log.info("Пользователь с id: {} удалил лайк фильму с id: {}", userId, filmId);
        } else {
            throw new ValidateException("Вы не ставили лайк этому фильму");
        }
    }

    @Override
    public Collection<Film> getPopular(int count) {
        String sql = "select * from film order by rate limit ?"; //(select film_id from film_user group by film_id order by count(user_id) desc limit ?)";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), count);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        // метод получает отбъект film из таблицы film, в которой есть все поля, кроме mpa и genre
        int id = rs.getInt("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        int rate = getRate(id);
        return new Film(id, name, description, releaseDate, duration, rate, getMpa(id), getGenres(id));
    }

    private Mpa getMpa(int filmId) {
        Mpa mpa = new Mpa();
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("select fr.rating_id, r.name from film_rating as fr inner join rating as r on fr.rating_id = r.rating_id where film_id = ?", filmId);
        if (mpaRows.next()) {
            mpa.setId(mpaRows.getInt("rating_id"));
            mpa.setName(mpaRows.getString("name"));
        }
        return mpa;
    }

    private Collection<Genre> getGenres(int filmId) {
        String sql = "select * from film_genre join genre on film_genre.genre_id = genre.genre_id where film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(rs.getInt("genre_id"), rs.getString("name")), filmId);
    }

    private int getRate(int filmId) {
        int rate = 0;
        SqlRowSet rateRows = jdbcTemplate.queryForRowSet("select count(user_id) as count from film_user where film_id = ?", filmId);
        if (rateRows.next()) {
            rate = rateRows.getInt("count");
        }
        return rate;
    }

    private Collection<Integer> getUniqueGenres(Collection<Genre> genres) {
        Set<Integer> uniqueGenresIds = new HashSet<>();
        for (Genre genre : genres) {
            uniqueGenresIds.add(genre.getId());
        }
        return uniqueGenresIds;
    }
}
