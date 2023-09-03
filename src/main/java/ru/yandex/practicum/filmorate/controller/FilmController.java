package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/{id}")
    public Optional<Film> getFilmById(@PathVariable int id) {
        log.info("Получен GET-запрос к эндпоинту: '/films/{id}'");
        return filmService.findFilmById(id);
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Получен GET-запрос к эндпоинту: '/films'");
        return filmService.findAllFilms();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) { // объект Film передается в теле запроса (без id), если поля объекта не заполнены произойдёт ошибка
        log.info("Получен POST-запрос к эндпоинту: '/films'");
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) { // объект Film передается в теле запроса, если поля объекта не заполнены произойдёт ошибка
        log.info("Получен PUT-запрос к эндпоинту: '/films'");
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен PUT-запрос к эндпоинту: '/films/{id}/like/{userId}'");
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен DELETE-запрос к эндпоинту: '/films/{id}/like/{userId}'");
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getListOfPopularFilms(@RequestParam(defaultValue = "10") String count) {
        log.info("Получен GET-запрос к эндпоинту: '/films/popular?count={count}'");
        return filmService.getListOfPopularFilms(count);
    }
}
