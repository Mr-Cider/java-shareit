package ru.practicum.shareit.ItemTests;

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
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.ShareItGatewayTest.X_SHARER_USER_ID;

@DisplayName("Item Controller Test")
@WebMvcTest(ItemController.class)
public class ItemControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemClient itemClient;

    NewItemDto newItemDto;

    ItemDto itemDto;

    ItemWithBookingsDto itemWithBookingsDto;



    @BeforeEach
    void setUp() {
        newItemDto = NewItemDto.builder()
                .name("correctName")
                .description("correctDescription")
                .available(true)
                .build();
        itemDto = ItemDto.builder()
                .id(1L)
                .name("correctName")
                .description("correctDescription")
                .available(true)
                .build();
        itemWithBookingsDto = ItemWithBookingsDto.builder()
                .id(1L)
                .name("correctName")
                .description("correctDescription")
                .available(true)
                .build();
    }

    @DisplayName("Корректная сериализация новой вещи")
    @Test
    public void newItemCorrectSerialization() throws Exception {

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

    @DisplayName("Получение вещей пользователя")
    @Test
    public void shouldGetUserItems() throws Exception {

        when(itemClient.getUserItems(1L)).thenReturn(ResponseEntity.ok(List.of(itemDto)));
        mockMvc.perform(get("/items")
                        .header(X_SHARER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @DisplayName("Получение пустого списка вещей пользователя")
    @Test
    public void shouldGetUserItemsIsEmpty() throws Exception {

        when(itemClient.getUserItems(1L)).thenReturn(ResponseEntity.ok(Collections.emptyList()));
        mockMvc.perform(get("/items")
                        .header(X_SHARER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @DisplayName("Получение вещи пользователя по id юзера и id вещи")
    @Test
    public void shouldGetItem() throws Exception {
        when(itemClient.getItem(1L, 1L)).thenReturn(ResponseEntity.ok(itemWithBookingsDto));
        mockMvc.perform(get("/items/1")
                        .header(X_SHARER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemWithBookingsDto.getId()))
                .andExpect(jsonPath("$.name").value(itemWithBookingsDto.getName()))
                .andExpect(jsonPath("$.description").value(itemWithBookingsDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemWithBookingsDto.getAvailable()));
    }

    @DisplayName("Создание вещи")
    @Test
    public void shouldCreateItem() throws Exception {
        NewItemDto newItemDto = NewItemDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();
        when(itemClient.createItem(1L, newItemDto)).thenReturn(ResponseEntity.ok(itemDto));
        mockMvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(newItemDto))
                        .header(X_SHARER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()));
    }

    @DisplayName("Обновление вещи")
    @Test
    public void shouldUpdateItem() throws Exception {
        UpdateItemDto updateItemDto = UpdateItemDto.builder()
                .id(1L)
                .name("correctName")
                .description("correctDescription")
                .available(true)
                .build();
        when(itemClient.updateItem(1L, 1L, updateItemDto)).thenReturn(ResponseEntity.ok(itemDto));
        mockMvc.perform(patch("/items/1")
                        .content(objectMapper.writeValueAsString(updateItemDto))
                        .header(X_SHARER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(updateItemDto.getName()))
                .andExpect(jsonPath("$.description").value(updateItemDto.getDescription()));
    }

    @DisplayName("Поиск вещи по имени или описанию")
    @Test
    public void shouldSearchItemsByNameOrDescription() throws Exception {
        when(itemClient.searchItemsByNameOrDescription("name")).thenReturn(ResponseEntity.ok(List.of(itemDto)));
        mockMvc.perform(get("/items/search?text=name")
                        .header(X_SHARER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @DisplayName("Добавление комментариев")
    @Test
    public void shouldAddComment() throws Exception {
        NewCommentDto newCommentDto = NewCommentDto.builder()
                .text("textForComment")
                .build();
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .authorName("authorName")
                .text("textForComment")
                .item(itemDto)
                .created(LocalDateTime.now())
                .build();
        when(itemClient.addComment(1L, 1L, newCommentDto)).thenReturn(ResponseEntity.ok(commentDto));
        mockMvc.perform(post("/items/1/comment")
                        .header(X_SHARER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCommentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentDto.getId()))
                .andExpect(jsonPath("$.authorName").value(commentDto.getAuthorName()))
                .andExpect(jsonPath("$.text").value(commentDto.getText()))
                .andExpect(jsonPath("$.created").isNotEmpty());
    }
}

