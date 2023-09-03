package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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
