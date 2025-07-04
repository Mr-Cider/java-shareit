package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(controllers = UserController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@DisplayName("User Controller Tests")
public class UserControllerTests {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    private NewUserDto newUserDto;

    private UpdateUserDto updateUserDto;

    private UserDto responseUserDto;

    @BeforeEach
    void setUp() {
        newUserDto = NewUserDto.builder()
                .email("email@email.com")
                .name("name")
                .build();

        updateUserDto = UpdateUserDto.builder()
                .email("update_email@test.ru")
                .name("update_name")
                .build();

        responseUserDto = UserDto.builder()
                .id(1L)
                .email("test@test.ru")
                .name("name")
                .build();
    }

    @DisplayName("Получение пользователя по id")
    @Test
    public void getUser() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .email("test@test.ru")
                .name("name")
                .build();
        when(userService.getUser(userDto.getId())).thenReturn(userDto);
        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@test.ru"))
                .andExpect(jsonPath("$.name").value("name"));
    }

    @DisplayName("Создание пользователя")
    @Test
    public void shouldCreateUser() throws Exception {
        when(userService.createUser(newUserDto)).thenReturn(responseUserDto);
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(newUserDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@test.ru"))
                .andExpect(jsonPath("$.name").value("name"));
    }

    @DisplayName("Обновление пользователя")
    @Test
    public void shouldUpdateUser() throws Exception {
        UserDto updatedUserDto = UserDto.builder()
                .id(1L)
                .email(updateUserDto.getEmail())
                .name(updateUserDto.getName())
                .build();
        when(userService.updateUser(1L, updateUserDto)).thenReturn(updatedUserDto);
        mockMvc.perform(patch("/users/1")
                        .content(objectMapper.writeValueAsString(updatedUserDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value(updateUserDto.getEmail()));
    }

    @DisplayName("Удаление пользователя")
    @Test
    public void shouldDeleteUser() throws Exception {
    doNothing().when(userService).deleteUser(1L);
    mockMvc.perform(delete("/users/1"))
            .andExpect(status().isOk());
    verify(userService, times(1)).deleteUser(1L);
    }
}
