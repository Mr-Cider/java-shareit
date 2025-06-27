package ru.practicum.shareit.item;
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
import ru.practicum.shareit.booking.dto.BookingDateDto;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.BaseClassForTests.X_SHARER_USER_ID;

@AutoConfigureMockMvc
@WebMvcTest(controllers = ItemController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@DisplayName("Item Controller Tests")
public class ItemControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    private ItemDto itemDto;

    private ItemWithBookingsDto itemWithBookingsDto;
    private BookingDateDto lastBookingDateDto;
    private BookingDateDto nextBookingDateDto;

    @BeforeEach
    void setUp() {
        itemDto = ItemDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .build();
        lastBookingDateDto = BookingDateDto.builder()
                .id(1L)
                .bookerId(2L)
                .start(LocalDateTime.of(2025, 5, 20, 0, 0))
                .end(LocalDateTime.of(2025, 5, 25, 0, 0))
                .build();
        nextBookingDateDto = BookingDateDto.builder()
                .id(2L)
                .bookerId(2L)
                .start(LocalDateTime.of(2025, 6, 1, 0, 0))
                .end(LocalDateTime.of(2025, 6, 10, 0, 0))
                .build();

        itemWithBookingsDto = ItemWithBookingsDto.builder()
                .id(1L)
                .name("itemWithBookingsName")
                .description("itemWithBookingsDescription")
                .available(true)
                .lastBooking(lastBookingDateDto)
                .nextBooking(nextBookingDateDto)
                .comments(Collections.emptyList())
                .request(null)
                .build();
    }

    @DisplayName("Получение вещей пользователя")
    @Test
    public void shouldGetUserItems() throws Exception {

        when(itemService.getUserItems(1L)).thenReturn(List.of(itemDto));
        mockMvc.perform(get("/items")
                        .header(X_SHARER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @DisplayName("Получение пустого списка вещей пользователя")
    @Test
    public void shouldGetUserItemsIsEmpty() throws Exception {

        when(itemService.getUserItems(1L)).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/items")
                        .header(X_SHARER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @DisplayName("Получение вещи пользователя по id юзера и id вещи")
    @Test
    public void shouldGetItem() throws Exception {
        when(itemService.getItem(1L, 1L)).thenReturn(itemWithBookingsDto);
        mockMvc.perform(get("/items/1")
                        .header(X_SHARER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemWithBookingsDto.getId()))
                .andExpect(jsonPath("$.name").value(itemWithBookingsDto.getName()))
                .andExpect(jsonPath("$.description").value(itemWithBookingsDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemWithBookingsDto.getAvailable()))
                .andExpect(jsonPath("$.lastBooking.id").value(lastBookingDateDto.getId()))
                .andExpect(jsonPath("$.lastBooking.bookerId").value(lastBookingDateDto.getBookerId()))
                .andExpect(jsonPath("$.lastBooking.start").exists())
                .andExpect(jsonPath("$.lastBooking.end").exists())
                .andExpect(jsonPath("$.nextBooking.id").value(nextBookingDateDto.getId()))
                .andExpect(jsonPath("$.nextBooking.bookerId").value(nextBookingDateDto.getBookerId()))
                .andExpect(jsonPath("$.nextBooking.start").exists())
                .andExpect(jsonPath("$.nextBooking.end").exists())
                .andExpect(jsonPath("$.comments").isArray())
                .andExpect(jsonPath("$.request").isEmpty());
    }

    @DisplayName("Создание вещи")
    @Test
    public void shouldCreateItem() throws Exception {
        NewItemDto newItemDto = NewItemDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();
        when(itemService.addItem(1L, newItemDto)).thenReturn(itemDto);
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
                .name("name")
                .description("description")
                .available(true)
                .build();
        when(itemService.updateItem(1L, updateItemDto)).thenReturn(itemDto);
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
        when(itemService.searchItems("name")).thenReturn(List.of(itemDto));
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
        when(itemService.addComment(1L, 1L, newCommentDto)).thenReturn(commentDto);
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
