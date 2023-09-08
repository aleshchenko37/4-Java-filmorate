package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dao.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dao.UserStorage;

import java.util.Collection;
import java.util.Optional;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Optional<Film> findFilmById(int id) {
        return filmStorage.findFilmById(id);
        // метод осуществляет проверку корректности переданного id
    }

    public Collection<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        findFilmById(film.getId());
        return filmStorage.updateFilm(film);
    }

    public void addLike(int filmId, int userId) {
        if (findFilmById(filmId).isPresent()) {
            if (userStorage.findUserById(userId).isPresent()) {
                filmStorage.addLike(filmId, userId);
            } else {
                throw new ObjectNotFoundException("Пользователь с id " + userId + " не найден");
            }
        } else {
            throw new ObjectNotFoundException("Фильм с id " + filmId + " не найден");
        }
    }

    public void removeLike(int filmId, int userId) {
        if (findFilmById(filmId).isPresent()) {
            if (userStorage.findUserById(userId).isPresent()) {
                filmStorage.removeLike(filmId, userId);
            } else {
                throw new ObjectNotFoundException("Пользователь с id " + userId + " не найден");
            }
        } else {
            throw new ObjectNotFoundException("Фильм с id " + filmId + " не найден");
        }
    }

    public Collection<Film> getListOfPopularFilms(String count) {
        int countAsInt = Integer.parseInt(count);
        if (countAsInt < 0) {
            throw new ValidateException("Количество фильмов не может быть меньше 0");
        }
        return filmStorage.getPopular(countAsInt);
    }
}
