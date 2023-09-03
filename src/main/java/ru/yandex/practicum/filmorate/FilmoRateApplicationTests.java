package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    User user = User.builder()
            .email("mail@mail.ru")
            .login("Nick Name")
            .name("dolore")
            .birthday(LocalDate.of(1946, 8, 20))
            .build();

    @Test
    public void testCreateUser() {
        Optional<User> userCreated = Optional.of(userStorage.createUser(user));
        assertThat(userCreated)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );

        assertThat(userStorage.findAllUsers().size()).isEqualTo(1);
        assertThat(userStorage.findAllUsers()).isEqualTo(List.of(user));
    }

    @Test
    public void testFindUserById() {
        Optional<User> userOptional = userStorage.findUserById(1);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }
}
