package ru.practicum.shareit.RequestTests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.request.RequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static ru.practicum.shareit.ShareItGatewayTest.X_SHARER_USER_ID;

@DisplayName("Request Client Tests")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RequestClientTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RequestClient requestClient;

    private MockRestServiceServer mockServer;

    UserDto userDto;

    private NewRequestDto correctNewRequestDto;

    private ItemRequestDto correctRequestDto;

    List<ItemRequestDto> requestDtoList;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = requestClient.getRestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);

        userDto = UserDto.builder()
                .id(1L)
                .name("name")
                .email("email@email.com")
                .build();
        correctNewRequestDto = NewRequestDto.builder()
                .description("correctDescription")
                .build();
        correctRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("correctDescription")
                .requestor(userDto)
                .created(LocalDateTime.now())
                .build();
        requestDtoList = List.of(
                ItemRequestDto.builder()
                        .id(1L)
                        .description("firstDescription")
                        .requestor(userDto)
                        .created(LocalDateTime.now())
                        .build(),
                ItemRequestDto.builder()
                        .id(2L)
                        .description("secondDescription")
                        .requestor(userDto)
                        .created(LocalDateTime.now())
                        .build()
        );
    }

    @DisplayName("Создание запроса")
    @Test
    public void shouldCreateRequest() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/requests"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(objectMapper.writeValueAsString(correctRequestDto),
                        MediaType.APPLICATION_JSON));
        ResponseEntity<Object> response = requestClient.createRequest(userDto.getId(), correctNewRequestDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ItemRequestDto responseBody = objectMapper.convertValue(response.getBody(), ItemRequestDto.class);
        assertThat(responseBody).isEqualTo(correctRequestDto);
    }

    @DisplayName("Получение списка запросов пользователя")
    @Test
    public void shouldGetRequests() throws Exception {

        mockServer.expect(requestTo("http://localhost:9090/requests?from=0&size=10"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-Sharer-User-Id", userDto.getId().toString()))
                .andRespond(withSuccess(objectMapper.writeValueAsString(requestDtoList),
                        MediaType.APPLICATION_JSON));
        ResponseEntity<Object> response = requestClient.getUserRequests(userDto.getId(), 0, 10);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<ItemRequestDto> responseBody = objectMapper.readValue(
                objectMapper.writeValueAsString(response.getBody()), new TypeReference<List<ItemRequestDto>>() {});
        assertThat(responseBody)
                .hasSize(2)
                .isEqualTo(requestDtoList);
    }

    @DisplayName("Получение списка запросов других пользователей")
    @Test
    public void shouldGetAllRequests() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/requests/all"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(X_SHARER_USER_ID, "2"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(requestDtoList), MediaType.APPLICATION_JSON));
        ResponseEntity<Object> response = requestClient.getAllRequests(2L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<ItemRequestDto> responseBody = objectMapper.readValue(
                objectMapper.writeValueAsString(response.getBody()), new TypeReference<List<ItemRequestDto>>() {
        });
        assertThat(responseBody)
                .hasSize(2)
                .isEqualTo(requestDtoList);
    }

    @DisplayName("Получение запроса по id")
    @Test
    public void shouldGetRequestById() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/requests/1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(
                        objectMapper.writeValueAsString(correctRequestDto), MediaType.APPLICATION_JSON));
        ResponseEntity<Object> response = requestClient.getRequestById(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ItemRequestDto responseBody = objectMapper.convertValue(response.getBody(), ItemRequestDto.class);
        assertThat(responseBody).isEqualTo(correctRequestDto);
    }

}

