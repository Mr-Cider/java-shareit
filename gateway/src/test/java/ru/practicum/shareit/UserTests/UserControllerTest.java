package ru.practicum.shareit.UserTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import org.springframework.http.MediaType;
import ru.practicum.shareit.user.dto.UserDto;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User Controller Test")
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserClient userClient;

    NewUserDto correctNewUser;

    UserDto responseUserDto;

    @BeforeEach
    void setUp() {
        correctNewUser = NewUserDto.builder()
                .email("correctEmail@email.com")
                .name("correctName")
                .build();

        responseUserDto = UserDto.builder()
                .id(1L)
                .email("correctEmail@email.com")
                .name("correctName")
                .build();
    }

    @DisplayName("Корректная сериализация нового пользователя")
    @Test
    public void newUserCorrectSerialization() throws Exception {
        String json = objectMapper.writeValueAsString(correctNewUser);
        NewUserDto deserialized = objectMapper.readValue(json, NewUserDto.class);
        assertThat(deserialized.getName()).isEqualTo("correctName");
        assertThat(deserialized.getEmail()).isEqualTo("correctEmail@email.com");
    }

    @DisplayName("Сериализация нового пользователя с некорректным email")
    @Test
    public void newUserIncorrectEmailSerialization() throws Exception {
        NewUserDto newUserDto = NewUserDto.builder()
                .email("incorrectEmail")
                .name("incorrectName")
                .build();
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserDto)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Сериализация нового пользователя с некорректным email")
    @Test
    public void newUserNameIsEmpty() throws Exception {
        NewUserDto newUserDto = NewUserDto.builder()
                .email("test@test.ru")
                .name("")
                .build();
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserDto)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Сериализация нового пользователя с пустым email")
    @Test
    public void newUserEmailIsEmpty() throws Exception {
        NewUserDto newUserDto = NewUserDto.builder()
                .name("namename")
                .build();
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserDto)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Корректная сериализация обновленного пользователя")
    @Test
    public void updateUserCorrectSerialization() throws Exception {
        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .email("correctEmail@email.com")
                .name("correctName")
                .build();
        String json = objectMapper.writeValueAsString(updateUserDto);
        UpdateUserDto deserialized = objectMapper.readValue(json, UpdateUserDto.class);
        assertThat(deserialized.getName()).isEqualTo("correctName");
        assertThat(deserialized.getEmail()).isEqualTo("correctEmail@email.com");
    }

    @DisplayName("Сериализация обновленного пользователя с некорректным email")
    @Test
    public void updateUserIncorrectEmailSerialization() throws Exception {
        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .email("incorrectEmail-email,com")
                .name("correctName")
                .build();
        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDto)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Сериализация обновленного пользователя с отсутствующим email")
    @Test
    public void updateUserEmailIsEmpty() throws Exception {
        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .name("correctName")
                .build();
        String json = objectMapper.writeValueAsString(updateUserDto);
        UpdateUserDto deserialized = objectMapper.readValue(json, UpdateUserDto.class);
        assertThat(deserialized.getName()).isEqualTo("correctName");
    }

    @DisplayName("Получение пользователя по id")
    @Test
    public void getUser() throws Exception {
        when(userClient.getUser(1L)).thenReturn(ResponseEntity.ok(responseUserDto));
        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value(responseUserDto.getEmail()))
                .andExpect(jsonPath("$.name").value(responseUserDto.getName()));
    }

    @DisplayName("Создание пользователя")
    @Test
    public void shouldCreateUser() throws Exception {
        when(userClient.createUser(correctNewUser)).thenReturn(ResponseEntity.ok(responseUserDto));
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(correctNewUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value(responseUserDto.getEmail()))
                .andExpect(jsonPath("$.name").value(responseUserDto.getName()));
    }

    @DisplayName("Обновление пользователя")
    @Test
    public void shouldUpdateUser() throws Exception {
        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .email("updateEmail@email.com")
                .name("updateName")
                .build();
        UserDto updatedUserDto = UserDto.builder()
                .id(1L)
                .email(updateUserDto.getEmail())
                .name(updateUserDto.getName())
                .build();
        when(userClient.updateUser(1L, updateUserDto)).thenReturn(ResponseEntity.ok(updatedUserDto));
        mockMvc.perform(patch("/users/1")
                        .content(objectMapper.writeValueAsString(updatedUserDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedUserDto.getId()))
                .andExpect(jsonPath("$.email").value(updateUserDto.getEmail()));
    }

    @DisplayName("Удаление пользователя")
    @Test
    public void shouldDeleteUser() throws Exception {
        when(userClient.deleteUser(1L)).thenReturn(ResponseEntity.noContent().build());
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
        verify(userClient, times(1)).deleteUser(1L);
    }
}