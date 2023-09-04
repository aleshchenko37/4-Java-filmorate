import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    private User user;
    private UserController userController;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController();
    }

    @Test
    public void shouldCreateUser() {
        user = User.builder()
                .email("a.leshchenko37@mail.ru")
                .login("a.leshchenko37")
                .name("anastasiya")
                .birthday(LocalDate.of(1996, 12, 9))
                .build();
        User userSaved = userController.createUser(user);
        assertEquals(user, userSaved, "Пользователи не идентичны");
        assertEquals(1, userController.getAllUsers().size(), "Количество пользователей в хранилище неверно");
    }

    @Test
    public void shouldNotCreateEmailInvalid() {
        user = User.builder()
                .email("a.leshchenko37mail.ru")
                .login("a.leshchenko37")
                .name("anastasiya")
                .birthday(LocalDate.of(1996, 12, 9))
                .build();
        final ValidateException e = assertThrows(ValidateException.class, () -> userController.createUser(user));
        assertEquals("Email должен содержать символ @", e.getMessage());
        assertEquals(0, userController.getAllUsers().size(), "Список должен быть пустым");
    }

    @Test
    public void shouldNotCreateLoginContainsSpaces() {
        user = User.builder()
                .email("a.leshchenko37@mail.ru")
                .login("a. leshchenko37")
                .name("anastasiya")
                .birthday(LocalDate.of(1996, 12, 9))
                .build();
        final ValidateException e = assertThrows(ValidateException.class, () -> userController.createUser(user));
        assertEquals("Логин не может содержать пробелы", e.getMessage());
        assertEquals(0, userController.getAllUsers().size(), "Список должен быть пустым");
    }

    @Test
    public void shouldCreateNameEqualsLogin() {
        user = User.builder()
                .email("a.leshchenko37@mail.ru")
                .login("a.leshchenko37")
                .birthday(LocalDate.of(1996, 12, 9))
                .build();
        User userSaved = userController.createUser(user);
        assertEquals(user.getLogin(), user.getName(), "Имя не равно логину");
        assertEquals(1, userController.getAllUsers().size(), "Количество пользователей в хранилище неверно");
    }

    @Test
    public void shouldNotBeBurnBeforeNow() {
        user = User.builder()
                .email("a.leshchenko37@mail.ru")
                .login("a.leshchenko37")
                .name("anastasiya")
                .birthday(LocalDate.of(2025, 12, 9))
                .build();
        final ValidateException e = assertThrows(ValidateException.class, () -> userController.createUser(user));
        assertEquals("Дата рождения не может быть в будущем", e.getMessage());
        assertEquals(0, userController.getAllUsers().size(), "Список должен быть пустым");
    }
}
