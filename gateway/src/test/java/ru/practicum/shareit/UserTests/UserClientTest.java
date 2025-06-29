package ru.practicum.shareit.UserTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@DisplayName("User Client Test")
@SpringBootTest
public class UserClientTest {
    @Autowired
    private UserClient userClient;

    private MockRestServiceServer mockServer;
    @Autowired
    ObjectMapper objectMapper;

    private NewUserDto correctNewUserDto;

    private UserDto correctUserDto;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = userClient.getRestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);
        correctNewUserDto = NewUserDto.builder()
                .email("correctEmail@mail.ru")
                .name("correctName")
                .build();

        correctUserDto = UserDto.builder()
                .id(1L)
                .email("correctEmail@mail.ru")
                .name("correctName")
                .build();
    }

    @DisplayName("Получение пользователя")
    @Test
    public void shouldGetUser() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/users/1"))
                        .andExpect(method(HttpMethod.GET))
                                .andRespond(withSuccess(objectMapper.writeValueAsString(correctUserDto),
                                        MediaType.APPLICATION_JSON));
        ResponseEntity<Object> response = userClient.getUser(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        UserDto responseBody = objectMapper.convertValue(response.getBody(), UserDto.class);
        assertThat(responseBody).isEqualTo(correctUserDto);
        mockServer.verify();
    }

    @DisplayName("Создание пользователя")
    @Test
    public void shouldCreateUser() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/users"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(objectMapper.writeValueAsString(correctUserDto), MediaType.APPLICATION_JSON));
        ResponseEntity<Object> response = userClient.createUser(correctNewUserDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        UserDto responseBody = objectMapper.convertValue(response.getBody(), UserDto.class);
        assertThat(responseBody).isEqualTo(correctUserDto);
    }

    @DisplayName("Обновление пользователя")
    @Test
    public void shouldUpdateUser() throws Exception {
        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .email("updateEmail@mail.ru")
                .name("updateName")
                .build();
        UserDto updatedUserDto = UserDto.builder()
                .id(1L)
                .email(updateUserDto.getEmail())
                .name(updateUserDto.getName())
                .build();
        mockServer.expect(requestTo("http://localhost:9090/users/1"))
                .andExpect(method(HttpMethod.PATCH))
                .andRespond(withSuccess(objectMapper.writeValueAsString(updatedUserDto), MediaType.APPLICATION_JSON));
        ResponseEntity<Object> response = userClient.updateUser(1L, updateUserDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        UserDto responseBody = objectMapper.convertValue(response.getBody(), UserDto.class);
        assertThat(responseBody).isEqualTo(updatedUserDto);
    }

    @DisplayName("Удаление пользователя")
    @Test
    public void shouldDeleteUser() {
        mockServer.expect(requestTo("http://localhost:9090/users/1"))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withNoContent());
        ResponseEntity<Void> response = userClient.deleteUser(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        mockServer.verify();
    }
}
