package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.validators.AfterDate;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data // обеспечивает создание только @RequiredArgsConstructor
@Builder
@AllArgsConstructor
// при создании объекта Film нужен конструктор без id (@RequiredArgsConstructor, поэтому поле id не final и присваивается в контроллере)
// при обновлении объекта нужен конструктор со всеми параметрами (@AllArgsConstructor)
public class Film {
    private int id;
    @NonNull
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @AfterDate
    private LocalDate releaseDate;
    @Positive
    private int duration;
    int rate;
    private Mpa mpa;
    private Collection<Genre> genres;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        values.put("rate", rate);
        return values;
    }

@Data
@Builder
// при создании объекта Film нужен конструктор без id (@RequiredArgsConstructor, поэтому поле id не final и присваивается в контроллере)
// при обновлении объекта нужен конструктор со всеми параметрами (@AllArgsConstructor)
public class Film extends Entity {
    @NonNull
    @NotBlank
    private final String name;
    @Size(min = 1, max = 200)
    private final String description;
    private final LocalDate releaseDate;
    @Positive
    private final int duration; // в секундах
}
