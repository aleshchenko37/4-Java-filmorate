import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    private Film film;
    private FilmController filmController;

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController();
    }

    @Test
    public void shouldCreateFilm() {
        film = Film.builder()
                .name("Леон")
                .description("Осиротевшая девочка становится напарницей наемного убийцы. Культовый триллер с Жаном Рено и Натали Портман.")
                .releaseDate(LocalDate.of(1994, 9, 14))
                .duration(7980)
                .build();
        Film filmSaved = filmController.createFilm(film);
        assertEquals(film, filmSaved, "Фильмы не идентичны");
        assertEquals(1, filmController.getAllFilms().size(), "Количество фильмов в хранилище неверно");
    }

    @Test
    public void shouldNotCreateNameIsEmpty() {
        film = Film.builder() // фильм без имени
                .name("")
                .description("Осиротевшая девочка становится напарницей наемного убийцы. Культовый триллер с Жаном Рено и Натали Портман.")
                .releaseDate(LocalDate.of(1994, 9, 14))
                .duration(7980)
                .build();
        final ValidateException e = assertThrows(ValidateException.class, () -> filmController.createFilm(film));
        assertEquals("Наименование фильма не может быть пустым", e.getMessage());
        assertEquals(0, filmController.getAllFilms().size(), "Список должен быть пустым");
    }

    @Test
    public void shouldNotCreateFilmDescriptionMoreThan200() {
        film = Film.builder() // фильм с длинным описанием
                .name("Леон")
                .description("«Лео́н» (фр. Léon, прокатное название в Северной Америке — «Профессионал» (англ. The Professional)) — фильм французского режиссёра Люка Бессона о профессиональном наёмном убийце по имени Леон, познакомившемся со своей соседкой Матильдой в результате трагической гибели её семьи. Фильм вышел на экраны в 1994 году. Снят по собственному сценарию Бессона. Главные роли в фильме исполняют Жан Рено, Гэри Олдмен, Натали Портман и Дэнни Айелло.")
                .releaseDate(LocalDate.of(1994, 9, 14))
                .duration(7980)
                .build();
        final ValidateException e = assertThrows(ValidateException.class, () -> filmController.createFilm(film));
        assertEquals("Описание не должно быть длинее 200 символов", e.getMessage());
        assertEquals(0, filmController.getAllFilms().size(), "Список должен быть пустым");
    }

    @Test
    public void shouldNotCreateFilmDateIsBefore() {
        film = Film.builder() // фильм с длинным описанием
                .name("Леон")
                .description("Осиротевшая девочка становится напарницей наемного убийцы. Культовый триллер с Жаном Рено и Натали Портман.")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(7980)
                .build();
        final ValidateException e = assertThrows(ValidateException.class, () -> filmController.createFilm(film));
        assertEquals("Дата релиза не должна быть раньше 28 декабря 1895 года", e.getMessage());
        assertEquals(0, filmController.getAllFilms().size(), "Список должен быть пустым");
    }

    @Test
    public void shouldNotCreateFilmDurationIsWrong() {
        film = Film.builder()
                .name("Леон")
                .description("Осиротевшая девочка становится напарницей наемного убийцы. Культовый триллер с Жаном Рено и Натали Портман.")
                .releaseDate(LocalDate.of(1994, 9, 14))
                .duration(-1)
                .build();
        final ValidateException e = assertThrows(ValidateException.class, () -> filmController.createFilm(film));
        assertEquals("Продолжительность фильма не может быть отрицательным числом", e.getMessage());
        assertEquals(0, filmController.getAllFilms().size(), "Список должен быть пустым");
    }
}
