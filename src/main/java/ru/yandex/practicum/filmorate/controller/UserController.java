package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable int id) {
        log.info("Получен GET-запрос к эндпоинту: '/users/{id}'");
        return userService.findUserById(id);
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Получен GET-запрос к эндпоинту: '/users'");
        return userService.findAllUsers();
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getListOfUsersFriends(@PathVariable int id) {
        log.info("Получен GET-запрос к эндпоинту: '/users/{id}/friends'");
        return userService.findUsersFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getListOfCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Получен GET-запрос к эндпоинту: '/users/{id}/friends/common/{otherId}'");
        return userService.getListOfCommonFriends(id, otherId);
    }

    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable int id) {
        log.info("Получен DELETE-запрос к эндпоинту: '/users'/{id}'");
        userService.removeUser(id);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) { // объект User передается в теле запроса (без id), если поля объекта не заполнены произойдёт ошибка
        log.info("Получен POST-запрос к эндпоинту: '/users'");
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) { // объект User передается в теле запроса
        log.info("Получен PUT-запрос к эндпоинту: '/users'");
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен PUT-запрос к эндпоинту: '/user'/{id}/friends/{friendId}'");
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен DELETE-запрос к эндпоинту: '/user'/{id}/friends/{friendId}'");
        userService.removeFriend(id, friendId);
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validators.UserValidator;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class UserController extends Controller {
    UserValidator validator = new UserValidator();

    @PostMapping(value = "/users")
    public User createUser(@Valid @RequestBody User user) { // объект User передается в теле запроса (без id), если поля объекта не заполнены произойдёт ошибка
        validator.validate(user);
        log.info("Получен POST-запрос к эндпоинту: '/user', пользователь добавлен");
        return (User) create(user);
    }

    @PutMapping(value = "/users")
    public User updateUser(@Valid @RequestBody User user) { // объект User передается в теле запроса
        validator.validate(user);
        log.info("Получен PUT-запрос к эндпоинту: '/user', пользователь обновлен");
        return (User) update(user);
    }

    @GetMapping("/users")
    public List<Entity> getAllUsers() {
        return getAll();
    }
}
