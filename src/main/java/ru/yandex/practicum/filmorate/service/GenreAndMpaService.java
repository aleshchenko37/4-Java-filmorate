package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.GenreAndMpaStorage;

import java.util.Collection;
import java.util.Optional;

@Service
public class GenreAndMpaService {
    private final GenreAndMpaStorage genreAndMpaStorage;

    public GenreAndMpaService(GenreAndMpaStorage genreAndMpaStorage) {
        this.genreAndMpaStorage = genreAndMpaStorage;
    }

    public Collection<Genre> getAllGenres() {
        return genreAndMpaStorage.findAllGenres();
    }

    public Optional<Genre> findGenreById(int id) {
        return genreAndMpaStorage.findGenreById(id);
    }

    public Collection<Mpa> getAllMpa() {
        return genreAndMpaStorage.getAllMpa();
    }

    public Optional<Mpa> findMpaById(int id) {
        return genreAndMpaStorage.findMpaById(id);
    }
}
