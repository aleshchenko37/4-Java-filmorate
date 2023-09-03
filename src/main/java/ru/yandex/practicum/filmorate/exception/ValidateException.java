package ru.yandex.practicum.filmorate.exception;

public class ValidateException extends IllegalArgumentException {
    public ValidateException(String message) {
        super(message);
    }
}
