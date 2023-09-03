package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.GenreAndMpaService;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@RestController
public class GenreAndMpaController {
    private final GenreAndMpaService genreAndMpaService;

    public GenreAndMpaController(GenreAndMpaService genreAndMpaService) {
        this.genreAndMpaService = genreAndMpaService;
    }

    @GetMapping("/genres")
    public Collection<Genre> getAllGenres() {
        log.info("Получен GET-запрос к эндпоинту: '/films/popular?count={count}'");
        return genreAndMpaService.getAllGenres();
    }

    @GetMapping("/genres/{id}")
    public Optional<Genre> getGenreById(@PathVariable int id) {
        log.info("Получен GET-запрос к эндпоинту: '/genres/{id}'");
        return genreAndMpaService.findGenreById(id);
    }

    @GetMapping("/mpa")
    public Collection<Mpa> getAllMpa() {
        log.info("Получен GET-запрос к эндпоинту: '/mpa'");
        return genreAndMpaService.getAllMpa();
    }

    @GetMapping("/mpa/{id}")
    public Optional<Mpa> getMpaById(@PathVariable int id) {
        log.info("Получен GET-запрос к эндпоинту: '/mpa/{id}'");
        return genreAndMpaService.findMpaById(id);
    }
}