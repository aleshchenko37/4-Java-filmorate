package ru.yandex.practicum.filmorate.validators;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

@Slf4j
public class FilmValidator {
    public void validate(Film film) throws ValidateException {
        if (film.getName().equals("")) {
            log.info("Наименование фильма не может быть пустым");
            throw new ValidateException("Наименование фильма не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            log.info("Описание не должно быть длинее 200 символов");
            throw new ValidateException("Описание не должно быть длинее 200 символов");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            log.info("Дата релиза не должна быть раньше 28 декабря 1895 года");
            throw new ValidateException("Дата релиза не должна быть раньше 28 декабря 1895 года");
        } else if (film.getDuration() < 0) {
            log.info("Продолжительность фильма не может быть отрицательным числом");
            throw new ValidateException("Продолжительность фильма не может быть отрицательным числом");
        }
    }
}
