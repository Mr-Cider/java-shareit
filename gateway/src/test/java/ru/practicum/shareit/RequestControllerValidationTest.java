package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.RequestClient;
import ru.practicum.shareit.request.controller.RequestController;
import ru.practicum.shareit.request.dto.NewRequestDto;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(RequestController.class)
public class RequestControllerValidationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RequestClient requestClient;

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
}
