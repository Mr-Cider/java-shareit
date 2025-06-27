package ru.practicum.shareit.user;


import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("User Service Tests")
@SpringBootTest
public class UserServiceTest {
    private final UserService userService;
    private final EntityManager em;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(1L)
                .email("firstEmail@test.ru")
                .name("firstName")
                .build();
    }

    @DisplayName("Получение пользователя по cуществующему id")
    @Test
    public void shouldGetUser() {
        UserDto result = userService.getUser(userDto.getId());
        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userDto);
    }

    @DisplayName("Получение пользователя по несуществующему id")
    @Test
    public void shouldNotGetUserByNonExistentId() {
        long id = 999L;
        assertThatThrownBy(() -> userService.getUser(id))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("Создание пользователя")
    @Test
    public void shouldCreateUser() {
        NewUserDto newUserDto = NewUserDto.builder()
                .email("email@email.ru")
                .name("nameForCreate")
                .build();
        userService.createUser(newUserDto);
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        User responseQuery = query.setParameter("email", newUserDto.getEmail()).getSingleResult();
        assertThat(responseQuery)
                .isNotNull()
                .satisfies(u -> {
                    assertThat(u.getId()).isNotNull();
                    assertThat(u.getEmail()).isEqualTo(newUserDto.getEmail());
                    assertThat(u.getName()).isEqualTo(newUserDto.getName());
                });
    }

    @DisplayName("Создание пользователя с дубликатом email")
    @Test
    public void shouldDontCreateUserByDuplicateEmail() {
        NewUserDto newUserDto = NewUserDto.builder()
                .email("firstEmail@test.ru")
                .name("nameForCreate")
                .build();
        assertThatThrownBy(() -> userService.createUser(newUserDto)).isInstanceOf(DuplicateEmailException.class);
    }

    @DisplayName("Обновление пользователя")
    @Test
    public void shouldUpdateUser() {
        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .email("test2@test2.ru")
                .name("nameForUpdate")
                .build();
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class);
        User oldUser = query.setParameter("id", 1).getSingleResult();
        UserDto oldUserDto = UserDto.builder()
                .id(oldUser.getId())
                .email(oldUser.getEmail())
                .name(oldUser.getName())
                .build();
        assertThat(oldUserDto).isNotNull()
                .satisfies(u -> {
                    assertThat(u.getId()).isEqualTo(userDto.getId());
                    assertThat(u.getEmail()).isEqualTo(userDto.getEmail());
                    assertThat(u.getName()).isEqualTo(userDto.getName());
                });
        userService.updateUser(oldUser.getId(), updateUserDto);
        User newUser = query.setParameter("id", oldUser.getId()).getSingleResult();
        assertThat(newUser).isNotNull()
                .satisfies(u -> {
                    assertThat(u.getId()).isEqualTo(oldUserDto.getId());
                    assertThat(u.getEmail()).isEqualTo(updateUserDto.getEmail());
                    assertThat(u.getName()).isEqualTo(updateUserDto.getName());
                });
    }

    @DisplayName("Удаление пользователя")
    @Test
    public void shouldDeleteUser() {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
        List<User> allUsers = query.getResultList();
        assertThat(allUsers).hasSize(3);
        userService.deleteUser(1L);
        List<User> newAllUsers = query.getResultList();
        assertThat(newAllUsers).hasSize(2);
    }
}
