package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserStorage;

import java.util.Collection;
import java.util.Optional;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Optional<User> findUserById(int id) {
        return userStorage.findUserById(id);
        // метод осуществляет проверку корректности переданного id
    }

    public Collection<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    public Collection<User> findUsersFriends(int id) {
        findUserById(id);
        return userStorage.findUsersFriends(id);
    }

    public Collection<User> getListOfCommonFriends(int id, int otherId) {
        findUserById(id);
        findUserById(otherId);
        return userStorage.findCommonFriends(id, otherId);
    }

    public void removeUser(int id) {
        findUserById(id);
        userStorage.removeUser(id);
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        findUserById(user.getId());
        return userStorage.updateUser(user);
    }

    public void addFriend(int id, int friendId) {
        findUserById(id);
        findUserById(friendId);
        userStorage.addFriend(id, friendId);
    }

    public void removeFriend(int id, int friendId) {
        findUserById(id);
        findUserById(friendId);
        userStorage.removeFriend(id, friendId);
    }
}
