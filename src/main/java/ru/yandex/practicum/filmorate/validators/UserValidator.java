package ru.yandex.practicum.filmorate.validators;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class UserValidator {
    public void validate(User user) throws ValidateException {
        if (user.getEmail().indexOf('@') < 0) {
            log.info("Email должен содержать символ @");
            throw new ValidateException("Email должен содержать символ @");
        } else if (user.getLogin().contains(" ")) {
            log.info("Логин не может содержать пробелы");
            throw new ValidateException("Логин не может содержать пробелы");
        } else if (user.getName() == null) {
            log.info("Имя пользователя не задано, поэтому в поле Name сохранен Логин");
            user.setName(user.getLogin());
        } else if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Дата рождения не может быть в будущем");
            throw new ValidateException("Дата рождения не может быть в будущем");
        }
    }
}
