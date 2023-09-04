package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class User {
    // модификаторы final убраны для корректности десериализации объектов из базы данных
    private int id;
    @NonNull
    @Email
    private String email;
    @NonNull
    @Pattern(regexp = "\\S*$")    // не должно быть пробелов
    private String login;
    private String name;
    @Past
    private LocalDate birthday;

    public User(int id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        if ((name == null) || (name.isEmpty()) || (name.isBlank())) {
            this.name = login;
        }
        this.birthday = birthday;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("email", email);
        values.put("login", login);
        values.put("name", name);
        values.put("birthday", birthday);
        return values;
    }

@Data
@Builder
public class User extends Entity {
    @NonNull
    @Email
    private final String email;
    @NonNull
    @Pattern(regexp = "\\S*$")    // не должно быть пробелов
    private final String login;
    private String name;
    @Past
    private final LocalDate birthday;
}
