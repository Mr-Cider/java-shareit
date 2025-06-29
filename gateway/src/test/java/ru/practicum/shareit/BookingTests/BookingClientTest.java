package ru.practicum.shareit.BookingTests;

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
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static ru.practicum.shareit.ShareItGatewayTest.X_SHARER_USER_ID;

@DisplayName("Booking Client Tests")
@SpringBootTest
public class BookingClientTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    BookingClient bookingClient;

    private MockRestServiceServer mockServer;

    private UserDto userDto;

    NewBookingDto correctNewBookingDto;

    BookingDto correctBookingDto;

    ItemDto itemDto;

    List<BookingDto> bookingDtoList;



    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = bookingClient.getRestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);
        userDto = UserDto.builder()
                .id(1L)
                .email("test@test.com")
                .name("name")
                .build();
        correctBookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .status("APPROVED")
                .build();


        bookingDtoList = List.of(
                BookingDto.builder()
                        .id(1L)
                        .start(LocalDateTime.now())
                        .end(LocalDateTime.now().plusDays(1))
                        .booker(userDto)
                        .status("WAITING")
                        .item(itemDto)
                        .build(),
                BookingDto.builder()
                        .id(2L)
                        .start(LocalDateTime.now().plusDays(2))
                        .end(LocalDateTime.now().plusDays(3))
                        .booker(userDto)
                        .status("WAITING")
                        .item(itemDto)
                        .build()
        );
    }

    @DisplayName("Получение списка бронирований по статусу")
    @Test
    public void shouldGetBookingState() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/bookings?state=WAITING"))
                .andExpect(header(X_SHARER_USER_ID, userDto.getId().toString()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(objectMapper.writeValueAsString(bookingDtoList), MediaType.APPLICATION_JSON));
        ResponseEntity<Object> response = bookingClient.getBookingState(userDto.getId(), BookingState.WAITING);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<BookingDto> responseBody = objectMapper.readValue(objectMapper.writeValueAsString(bookingDtoList),
                new TypeReference<List<BookingDto>>() {});
        assertThat(responseBody).hasSize(2)
                .isEqualTo(bookingDtoList);
    }

    @DisplayName("Получение списка бронирований хозяина вещей по статусу")
    @Test
    public void shouldGetOwnerBookingState() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/bookings/owner?state=WAITING"))
                .andExpect(header(X_SHARER_USER_ID, userDto.getId().toString()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(objectMapper.writeValueAsString(bookingDtoList), MediaType.APPLICATION_JSON));
        ResponseEntity<Object> response = bookingClient.getOwnerBookingState(userDto.getId(), BookingState.WAITING);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<BookingDto> responseBody = objectMapper.readValue(objectMapper.writeValueAsString(bookingDtoList),
                new TypeReference<List<BookingDto>>() {});
        assertThat(responseBody).hasSize(2)
                .isEqualTo(bookingDtoList);
    }

    @DisplayName("Создание бронирования")
    @Test
    public void shouldCreateBooking() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/bookings"))
                .andExpect(header(X_SHARER_USER_ID, userDto.getId().toString()))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(objectMapper.writeValueAsString(correctBookingDto), MediaType.APPLICATION_JSON));
        ResponseEntity<Object> response = bookingClient.createBooking(userDto.getId(), correctNewBookingDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        BookingDto responseBody = objectMapper.convertValue(response.getBody(), BookingDto.class);
        assertThat(responseBody).isEqualTo(correctBookingDto);
    }

    @DisplayName("Подтверждение бронирования")
    @Test
    public void shouldApprovedOrRejectedBooking() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/bookings/1?approved=true"))
                .andExpect(header(X_SHARER_USER_ID, userDto.getId().toString()))
                .andExpect(method(HttpMethod.PATCH))
                .andRespond(withSuccess(objectMapper.writeValueAsString(correctBookingDto),
                        MediaType.APPLICATION_JSON));
        ResponseEntity<Object> response = bookingClient.approveOrRejectBooking(
                userDto.getId(), correctBookingDto.getId(), true);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        BookingDto responseBody = objectMapper.convertValue(response.getBody(), BookingDto.class);
        assertThat(responseBody).isEqualTo(correctBookingDto);
    }

    @DisplayName("Проверка подтверждения бронирования")
    @Test
    public void shouldGetBookingApproved() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/bookings/1"))
                .andExpect(header(X_SHARER_USER_ID, userDto.getId().toString()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(objectMapper.writeValueAsString(correctBookingDto),
                        MediaType.APPLICATION_JSON));
        ResponseEntity<Object> response = bookingClient.getBookingApproved(userDto.getId(), correctBookingDto.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        BookingDto responseBody = objectMapper.convertValue(response.getBody(), BookingDto.class);
        assertThat(responseBody).isEqualTo(correctBookingDto);
    }
}
