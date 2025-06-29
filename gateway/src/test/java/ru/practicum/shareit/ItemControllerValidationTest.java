package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.NewCommentDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import org.springframework.http.MediaType;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.BaseClassForTests.X_SHARER_USER_ID;

@DisplayName("Item Controller Validation Test")
@WebMvcTest(ItemController.class)
public class ItemControllerValidationTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemClient itemClient;

    @DisplayName("Корректная сериализация новой вещи")
    @Test
    public void newItemCorrectSerialization() throws Exception {
        NewItemDto newItemDto = NewItemDto.builder()
                .name("correctName")
                .description("correctDescription")
                .available(true)
                .build();
        String json = objectMapper.writeValueAsString(newItemDto);
        NewItemDto deserializedItemDto = objectMapper.readValue(json, NewItemDto.class);
        assertThat(deserializedItemDto.getName()).isEqualTo(newItemDto.getName());
        assertThat(deserializedItemDto.getDescription()).isEqualTo(newItemDto.getDescription());
        assertThat(deserializedItemDto.getAvailable()).isEqualTo(newItemDto.getAvailable());
    }

    @DisplayName("Сериализация новой вещи с пустым именем")
    @Test
    public void newItemNameIsBlank() throws Exception {
        NewItemDto newItemDto = NewItemDto.builder()
                .name("")
                .description("correctDescription")
                .available(true)
                .build();
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L)
                        .content(objectMapper.writeValueAsString(newItemDto)))
                .andExpect(status().isBadRequest());

    }

    @DisplayName("Сериализация новой вещи с пустым описанием")
    @Test
    public void newItemDescriptionIsBlank() throws Exception {
        NewItemDto newItemDto = NewItemDto.builder()
                .name("name")
                .description("")
                .available(true)
                .build();
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L)
                        .content(objectMapper.writeValueAsString(newItemDto)))
                .andExpect(status().isBadRequest());

    }

    @DisplayName("Сериализация новой вещи с пустым статусом")
    @Test
    public void newItemAvailableIsEmpty() throws Exception {
        NewItemDto newItemDto = NewItemDto.builder()
                .name("name")
                .description("description")
                .build();
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L)
                        .content(objectMapper.writeValueAsString(newItemDto)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Корректная сериализация обновленной вещи")
    @Test
    public void updateItemCorrectSerialization() throws Exception {
        UpdateItemDto updateItemDto = UpdateItemDto.builder()
                .id(1L)
                .name("correctName")
                .description("correctDescription")
                .available(true)
                .build();
        String json = objectMapper.writeValueAsString(updateItemDto);
        UpdateItemDto deserializedItemDto = objectMapper.readValue(json, UpdateItemDto.class);
        assertThat(deserializedItemDto.getName()).isEqualTo(updateItemDto.getName());
        assertThat(deserializedItemDto.getDescription()).isEqualTo(updateItemDto.getDescription());
        assertThat(deserializedItemDto.getAvailable()).isEqualTo(updateItemDto.getAvailable());
    }

    @DisplayName("Сериализация обновленной вещи с пустым именем")
    @Test
    public void updateItemNameIsBlank() throws Exception {
        UpdateItemDto updateItemDto = UpdateItemDto.builder()
                .name("")
                .description("correctDescription")
                .available(true)
                .build();
        String json = objectMapper.writeValueAsString(updateItemDto);
        UpdateItemDto deserializedItemDto = objectMapper.readValue(json, UpdateItemDto.class);
        assertThat(deserializedItemDto.getName()).isEqualTo(updateItemDto.getName());
        assertThat(deserializedItemDto.getDescription()).isEqualTo(updateItemDto.getDescription());
        assertThat(deserializedItemDto.getAvailable()).isEqualTo(updateItemDto.getAvailable());

    }

    @DisplayName("Сериализация обновленной вещи с пустым описанием")
    @Test
    public void updateItemDescriptionIsBlank() throws Exception {
        UpdateItemDto updateItemDto = UpdateItemDto.builder()
                .name("name")
                .description("")
                .available(true)
                .build();
        String json = objectMapper.writeValueAsString(updateItemDto);
        UpdateItemDto deserializedItemDto = objectMapper.readValue(json, UpdateItemDto.class);
        assertThat(deserializedItemDto.getName()).isEqualTo(updateItemDto.getName());
        assertThat(deserializedItemDto.getDescription()).isEqualTo(updateItemDto.getDescription());
        assertThat(deserializedItemDto.getAvailable()).isEqualTo(updateItemDto.getAvailable());

    }

    @DisplayName("Сериализация обновленной вещи с пустым статусом")
    @Test
    public void updateItemAvailableIsEmpty() throws Exception {
        UpdateItemDto updateItemDto = UpdateItemDto.builder()
                .name("name")
                .description("description")
                .build();
        String json = objectMapper.writeValueAsString(updateItemDto);
        UpdateItemDto deserializedItemDto = objectMapper.readValue(json, UpdateItemDto.class);
        assertThat(deserializedItemDto.getName()).isEqualTo(updateItemDto.getName());
        assertThat(deserializedItemDto.getDescription()).isEqualTo(updateItemDto.getDescription());
        assertThat(deserializedItemDto.getAvailable()).isNull();
    }

    @DisplayName("Корректная сериализация нового комментария")
    @Test
    public void addCommentCorrectSerialization() throws Exception {
        NewCommentDto newCommentDto = NewCommentDto.builder()
                .text("correctText")
                .build();
        String json = objectMapper.writeValueAsString(newCommentDto);
        NewCommentDto deserializedItemDto = objectMapper.readValue(json, NewCommentDto.class);
        assertThat(deserializedItemDto.getText()).isEqualTo(newCommentDto.getText());
    }

    @DisplayName("Сериализация нового комментария с пустым текстом")
    @Test
    public void addCommentTextIsBlank() throws Exception {
        NewCommentDto newCommentDto = NewCommentDto.builder()
                .text("")
                .build();
        mockMvc.perform(post("/items/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L)
                        .content(objectMapper.writeValueAsString(newCommentDto)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Сериализация нового комментария с отсутствующим текстом")
    @Test
    public void addCommentTextIsEmpty() throws Exception {
        NewCommentDto newCommentDto = NewCommentDto.builder()
                .build();
        mockMvc.perform(post("/items/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L)
                        .content(objectMapper.writeValueAsString(newCommentDto)))
                .andExpect(status().isBadRequest());
    }







}

