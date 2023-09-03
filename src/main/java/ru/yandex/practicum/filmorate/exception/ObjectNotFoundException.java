package ru.yandex.practicum.filmorate.exception;

public class ObjectNotFoundException extends NullPointerException {
    public ObjectNotFoundException(String message) {
        super(message);
    }

}
