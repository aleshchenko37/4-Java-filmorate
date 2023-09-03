package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

public interface GenreAndMpaStorage {
    Collection<Genre> findAllGenres();

    Optional<Genre> findGenreById(int id);

    Collection<Mpa> getAllMpa();

    Optional<Mpa> findMpaById(int id);
}
