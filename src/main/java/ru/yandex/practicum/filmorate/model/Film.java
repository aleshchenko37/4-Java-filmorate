package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

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
