package ru.practicum.shareit.booking;
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
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.ShareItServerTests.X_SHARER_USER_ID;

@AutoConfigureMockMvc
@WebMvcTest(controllers = BookingController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@DisplayName("Booking Controller Tests")
public class BookingControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    private UserDto userDto;

    private ItemDto itemDto;

    private NewBookingDto newBookingDto;

    private BookingDto bookingDto;

    private BookingDto bookingDto2;


    private List<BookingDto> bookingDtoList;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .id(1L)
                .email("email@email.com")
                .name("name")
                .build();

        itemDto = ItemDto.builder()
                .id(1L)
                .name("itemName")
                .description("itemDescription")
                .available(true)
                .build();

        newBookingDto = NewBookingDto.builder()
                .itemId(itemDto.getId())
                .start(LocalDateTime.of(2025, 5, 1, 0, 0))
                .end(LocalDateTime.of(2025, 5, 3, 0, 0))
                .build();
        bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2025, 5, 1, 0, 0))
                .end(LocalDateTime.of(2025, 5, 3, 0, 0))
                .item(itemDto)
                .status("WAITING")
                .booker(userDto)
                .build();
        bookingDto2 = BookingDto.builder()
                .id(2L)
                .start(LocalDateTime.of(2025, 5, 1, 0, 0))
                .end(LocalDateTime.of(2025, 5, 3,0,0))
                .item(itemDto)
                .status("APPROVED")
                .booker(userDto)
                .build();
        bookingDtoList = List.of(bookingDto, bookingDto2);
    }

    @DisplayName("Создание бронирования")
    @Test
    public void shouldCreateBooking() throws Exception {
        when(bookingService.createBooking(userDto.getId(), newBookingDto)).thenReturn(bookingDto);
        mockMvc.perform(post("/bookings")
                .header(X_SHARER_USER_ID, userDto.getId())
                .content(objectMapper.writeValueAsString(newBookingDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.start").exists())
                .andExpect(jsonPath("$.end").exists())
                .andExpect(jsonPath("$.item.id").value(bookingDto.getItem().getId()))
                .andExpect(jsonPath("$.item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$.status").value("WAITING"))
                .andExpect(jsonPath("$.booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$.booker.email").value(bookingDto.getBooker().getEmail()));
    }

    @DisplayName("Подтверждение бронирования")
    @Test
    public void shouldApproveOrRejectBooking() throws Exception {
        bookingDto.setStatus("APPROVED");
        when(bookingService.approveOrRejectBooking(userDto.getId(), bookingDto.getId(), true))
                .thenReturn(bookingDto);
        mockMvc.perform(patch("/bookings/1?approved=true")
                        .header(X_SHARER_USER_ID, userDto.getId())
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.start").exists())
                .andExpect(jsonPath("$.end").exists())
                .andExpect(jsonPath("$.item.id").value(bookingDto.getItem().getId()))
                .andExpect(jsonPath("$.item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$.booker.email").value(bookingDto.getBooker().getEmail()));
    }
        @DisplayName("Получение подтверждения бронирования")
        @Test
        public void shouldGetBookingApproved() throws Exception {
        bookingDto.setStatus("APPROVED");
        when(bookingService.getBookingApproved(userDto.getId(), bookingDto.getId())).thenReturn(bookingDto);
        mockMvc.perform(get("/bookings/1").header(X_SHARER_USER_ID, userDto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.start").exists())
                .andExpect(jsonPath("$.end").exists())
                .andExpect(jsonPath("$.item.id").value(bookingDto.getItem().getId()))
                .andExpect(jsonPath("$.item.name").value(bookingDto.getItem().getName()))
                .andExpect(jsonPath("$.status").value("APPROVED"))
                .andExpect(jsonPath("$.booker.id").value(bookingDto.getBooker().getId()))
                .andExpect(jsonPath("$.booker.email").value(bookingDto.getBooker().getEmail()));
        }

        @DisplayName("Получение списка всех бронирований текущего пользователя без параметра state(all)")
        @Test
        public void shouldGetBookingStateAll() throws Exception {
            when(bookingService.getBookingState(userDto.getId(), State.ALL)).thenReturn(bookingDtoList);
            mockMvc.perform(get("/bookings").header(X_SHARER_USER_ID, userDto.getId())
            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").exists())
                    .andExpect(jsonPath("$", hasSize(2)));
        }

        @DisplayName("Получение списка всех бронирований текущего пользователя с корректным параметром state")
        @Test
        public void shouldGetBookingStateApproved() throws Exception {
        bookingDto.setStatus("APPROVED");
        when(bookingService.getBookingState(userDto.getId(), State.CURRENT)).thenReturn(bookingDtoList);
        mockMvc.perform(get("/bookings?state=CURRENT").header(X_SHARER_USER_ID, userDto.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", hasSize(2)));
        }

    @DisplayName("Получение списка всех бронирований текущего пользователя с некорректным параметром state")
    @Test
    public void shouldGetBookingStateUncorrect() throws Exception {
        mockMvc.perform(get("/bookings?state=UNCORRECT")).andExpect(status().isBadRequest());
    }

    @DisplayName("Получение списка бронирований для всех вещей текущего пользователя без параметра state(all)")
    @Test
    public void shouldGetOwnerBookingStateAll() throws Exception {
        when(bookingService.getOwnerBookingState(userDto.getId(), State.ALL)).thenReturn(bookingDtoList);
        mockMvc.perform(get("/bookings/owner").header(X_SHARER_USER_ID, userDto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @DisplayName("Получение списка бронирований для всех вещей текущего пользователя с корректным параметром state")
    @Test
    public void shouldGetOwnerBookingStateApproved() throws Exception {
        bookingDto.setStatus("APPROVED");
        when(bookingService.getOwnerBookingState(userDto.getId(), State.CURRENT)).thenReturn(bookingDtoList);
        mockMvc.perform(get("/bookings/owner?state=CURRENT").header(X_SHARER_USER_ID, userDto.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @DisplayName("Получение списка бронирований для всех вещей текущего пользователя с некорректным параметром state")
    @Test
    public void shouldGetOwnerBookingStateUncorrect() throws Exception {
        mockMvc.perform(get("/bookings/owner?state=UNCORRECT")).andExpect(status().isBadRequest());
    }
}
