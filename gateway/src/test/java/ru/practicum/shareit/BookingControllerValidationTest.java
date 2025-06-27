package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.NewBookingDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.time.LocalDateTime;

@WebMvcTest(BookingController.class)
public class BookingControllerValidationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookingController bookingController;

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
                        .content(objectMapper.writeValueAsString(newBookingDto)))
                .andExpect(status().isBadRequest());
    }
}
