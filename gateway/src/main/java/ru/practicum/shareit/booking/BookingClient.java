package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public RestTemplate getRestTemplate() {
        return super.rest;
    }

    public ResponseEntity<Object> getBookingState(long userId, BookingState state) {
        Map<String, Object> parameters = Map.of(
                "state", state.name()
        );
        return get("?state={state}", userId, parameters);
    }

    public ResponseEntity<Object> getOwnerBookingState(Long ownerId, BookingState state) {
        Map<String, Object> parameters = Map.of(
                "state", state.name()
        );
        return get("/owner?state={state}", ownerId, parameters);
    }


    public ResponseEntity<Object> createBooking(Long userId, NewBookingDto requestDto) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> getBookingApproved(long userId, Long bookingId) {
        Map<String, Object> parameters = Map.of(
                "bookingId", bookingId
        );
        return get("/{bookingId}", userId, parameters);
    }

    public ResponseEntity<Object> approveOrRejectBooking(Long ownerId, Long bookingId, Boolean approved) {
        Map<String, Object> bookingParameters = Map.of(
                "bookingId", bookingId,
                "approved", approved
        );
        return patch("/{bookingId}?approved={approved}", ownerId, bookingParameters, null);
    }
}
