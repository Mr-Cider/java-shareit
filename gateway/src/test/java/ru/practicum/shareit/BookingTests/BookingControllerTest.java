package ru.practicum.shareit.BookingTests;

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
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.ShareItGatewayTest.X_SHARER_USER_ID;

@DisplayName("Booking Controller Test")
@WebMvcTest(BookingController.class)
public class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookingClient bookingClient;

    UserDto userDto;

    NewBookingDto newBookingDto;

    BookingDto bookingDto;

    List<BookingDto> bookingDtoList;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(1L)
                .name("name")
                .email("email@email.com")
                .build();
        newBookingDto = NewBookingDto.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .itemId(1L)
                .build();
        bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .status("WAITING")
                .build();
        bookingDtoList = List.of(bookingDto);
    }

    @DisplayName("Корректная сериализация нового бронирования")
    @Test
    public void createBookingCorrectSerialization() throws Exception {
        NewBookingDto newBookingDto = NewBookingDto.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .itemId(1L)
                .build();
        String json = objectMapper.writeValueAsString(newBookingDto);
        NewBookingDto deserializedBookingDto = objectMapper.readValue(json, NewBookingDto.class);
        assertThat(deserializedBookingDto.getStart()).isEqualTo(newBookingDto.getStart());
        assertThat(deserializedBookingDto.getEnd()).isEqualTo(newBookingDto.getEnd());
        assertThat(deserializedBookingDto.getItemId()).isEqualTo(newBookingDto.getItemId());
    }

    @DisplayName("Сериализация бронирования с отсутствующим временем старта")
    @Test
    public void createBookingStartTimeIsNull() throws Exception {
        NewBookingDto newBookingDto = NewBookingDto.builder()
                .end(LocalDateTime.now().plusDays(1))
                .itemId(1L)
                .build();
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L)
                        .content(objectMapper.writeValueAsString(newBookingDto)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Сериализация бронирования с отсутствующим временем окончания бронирования")
    @Test
    public void createBookingEndTimeIsNull() throws Exception {
        NewBookingDto newBookingDto = NewBookingDto.builder()
                .start(LocalDateTime.now())
                .itemId(1L)
                .build();
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L)
                        .content(objectMapper.writeValueAsString(newBookingDto)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Сериализация бронирования с отсутствующим временем старта")
    @Test
    public void createBookingItemIdIsNull() throws Exception {
        NewBookingDto newBookingDto = NewBookingDto.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 1L)
                        .content(objectMapper.writeValueAsString(newBookingDto)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Создание бронирования")
    @Test
    public void shouldCreateBooking() throws Exception {
        when(bookingClient.createBooking(userDto.getId(), newBookingDto)).thenReturn(ResponseEntity.ok(bookingDto));
        mockMvc.perform(post("/bookings")
                        .header(X_SHARER_USER_ID, userDto.getId())
                        .content(objectMapper.writeValueAsString(newBookingDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.start").exists())
                .andExpect(jsonPath("$.end").exists())
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @DisplayName("Подтверждение бронирования")
    @Test
    public void shouldApproveOrRejectBooking() throws Exception {
        bookingDto.setStatus("APPROVED");
        when(bookingClient.approveOrRejectBooking(userDto.getId(), bookingDto.getId(), true))
                .thenReturn(ResponseEntity.ok(bookingDto));
        mockMvc.perform(patch("/bookings/1?approved=true")
                        .header(X_SHARER_USER_ID, userDto.getId())
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.start").exists())
                .andExpect(jsonPath("$.end").exists())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @DisplayName("Получение подтверждения бронирования")
    @Test
    public void shouldGetBookingApproved() throws Exception {
        bookingDto.setStatus("APPROVED");
        when(bookingClient.getBookingApproved(userDto.getId(), bookingDto.getId())).thenReturn(ResponseEntity.ok(bookingDto));
        mockMvc.perform(get("/bookings/1").header(X_SHARER_USER_ID, userDto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.start").exists())
                .andExpect(jsonPath("$.end").exists())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @DisplayName("Получение списка всех бронирований текущего пользователя")
    @Test
    public void shouldGetBookingStateAll() throws Exception {
        when(bookingClient.getBookingState(userDto.getId(), BookingState.ALL)).thenReturn(ResponseEntity.ok(bookingDtoList));
        mockMvc.perform(get("/bookings").header(X_SHARER_USER_ID, userDto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}