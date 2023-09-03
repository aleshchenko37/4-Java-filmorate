package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    Optional<User> findUserById(int id);

    Collection<User> findAllUsers();

    Collection<User> findUsersFriends(int id);

    Collection<User> findCommonFriends(int id, int otherId);

    boolean removeUser(int id);

    User createUser(User user);

    User updateUser(User user);

    void addFriend(int id, int friendId);

    void removeFriend(int id, int friendId);
}
