package ru.practicum.shareit.RequestTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.RequestClient;
import ru.practicum.shareit.request.controller.RequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequestDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.ShareItGatewayTest.X_SHARER_USER_ID;

@DisplayName("Request Controller Test")
@WebMvcTest(RequestController.class)
public class RequestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RequestClient requestClient;

    NewRequestDto newRequestDto;

    ItemRequestDto requestDto;

    List<ItemRequestDto> requestDtoList;

    @BeforeEach
    void setUp() {
        newRequestDto = NewRequestDto.builder()
                .description("description")
                .build();
        requestDto = ItemRequestDto.builder()
                .id(1L)
                .description("description")
                .created(LocalDateTime.now())
                .build();
        requestDtoList = List.of(requestDto);
    }

    @DisplayName("Корректная сериализация нового запроса")
    @Test
    public void createRequestCorrectSerialization() throws Exception {
        NewRequestDto newRequestDto = NewRequestDto.builder()
                .description("description")
                .build();
        String json = objectMapper.writeValueAsString(newRequestDto);
        NewRequestDto deserializedRequestDto = objectMapper.readValue(json, NewRequestDto.class);
        assertThat(deserializedRequestDto.getDescription()).isEqualTo(newRequestDto.getDescription());
    }

    @DisplayName("Сериализация нового запроса с пустым описанием")
    @Test
    public void createRequestDescriptionIsBlank() throws Exception {
        NewRequestDto newRequestDto = NewRequestDto.builder()
                .description("")
                .build();
        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Сериализация нового запроса с отсутствующим описанием")
    @Test
    public void createRequestDescriptionIsEmpty() throws Exception {
        NewRequestDto newRequestDto = NewRequestDto.builder().build();
        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Создание запроса")
    @Test
    public void shouldCreateRequest() throws Exception {
        when(requestClient.createRequest(1L, newRequestDto))
                .thenReturn(ResponseEntity.ok(requestDto));
        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L)
                        .content(objectMapper.writeValueAsString(newRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(requestDto)))
                .andExpect(jsonPath("$.description").value(requestDto.getDescription()));
    }

    @DisplayName("Получить запросы пользователя")
    @Test
    public void shouldGetUserRequests() throws Exception {
        when(requestClient.getUserRequests(1L, 0, 10)).thenReturn(ResponseEntity.ok(requestDtoList));
        mockMvc.perform(get("/requests")
                .header(X_SHARER_USER_ID, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDtoList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @DisplayName("Получить запросы других пользователей")
    @Test
    public void shouldGetAllRequests() throws Exception {
        when(requestClient.getAllRequests(2L)).thenReturn(ResponseEntity.ok(requestDtoList));
        mockMvc.perform(get("/requests/all")
                        .header(X_SHARER_USER_ID, 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDtoList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @DisplayName("Получение запроса")
    @Test
    public void shouldGetRequest() throws Exception {
        when(requestClient.getRequestById(1L)).thenReturn(ResponseEntity.ok(requestDto));
        mockMvc.perform(get("/requests/1")
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(objectMapper.writeValueAsString(requestDtoList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(requestDto.getDescription()));
    }
}
