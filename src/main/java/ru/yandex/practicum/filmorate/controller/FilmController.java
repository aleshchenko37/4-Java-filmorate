package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
public class FilmController extends Controller {
    FilmValidator validator = new FilmValidator();

    @PostMapping(value = "/films")
    public Film createFilm(@Valid @RequestBody Film film) { // объект Film передается в теле запроса (без id), если поля объекта не заполнены произойдёт ошибка
        validator.validate(film);
        log.info("Получен POST-запрос к эндпоинту: '/film', фильм добавлен");
        return (Film) create(film);
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film) { // объект Film передается в теле запроса, если поля объекта не заполнены произойдёт ошибка
        validator.validate(film);
        log.info("Получен PUT-запрос к эндпоинту: '/film', фильм обновлен");
        return (Film) update(film);
    }

    @GetMapping("/films")
    public List<Entity> getAllFilms() {
        return getAll();
    }
}
