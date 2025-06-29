package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User Controller Validation Test")
@WebMvcTest(UserController.class)
public class UserControllerValidationTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserClient userClient;

    @DisplayName("Корректная сериализация нового пользователя")
    @Test
    public void newUserCorrectSerialization() throws Exception {
        NewUserDto newUserDto = NewUserDto.builder()
                .email("correctEmail@email.com")
                .name("correctName")
                .build();
        String json = objectMapper.writeValueAsString(newUserDto);
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
}
