package ru.practicum.shareit.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@Sql(scripts = "/data.sql")
@DisplayName("User Storage")
public class UserStorageTest {
    User user;
    @Autowired
    private UserStorage userStorage;

    @DisplayName("Тест на нахождение дубликатов")
    @Test
    public void shouldExistByEmail() {
        boolean exists = userStorage.existsByEmailIgnoreCase("firstEmail@test.ru");
        assertThat(exists).isTrue();
    }

    @DisplayName("Тест на нахождение дубликатов с отличающимся регистром")
    @Test
    public void shouldExistByEmailInUpperCase() {
        boolean exists = userStorage.existsByEmailIgnoreCase("fIRSTeMAIl@tEST.Ru");
        assertThat(exists).isTrue();
    }


    @DisplayName("Тест на отсутствие дубликата")
    @Test
    public void shouldNotExistByEmail() {
        boolean exists = userStorage.existsByEmailIgnoreCase("nonexistent@mail.ru");
        assertThat(exists).isFalse();
    }
}

