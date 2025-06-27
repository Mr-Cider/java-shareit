package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.NewBookingDto;

@RestController
@RequestMapping("bookings")
@Validated
@RequiredArgsConstructor
public class BookingController {

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader (HEADER_USER_ID) Long userId,
                                                @Valid @RequestBody NewBookingDto newBookingDto) {
        return bookingClient.createBooking(userId, newBookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveOrRejectBooking(@RequestHeader (HEADER_USER_ID) Long ownerId,
                                                         @Positive @PathVariable Long bookingId,
                                                         @RequestParam(name = "approved") Boolean approved) {
        return bookingClient.approveOrRejectBooking(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingApproved(@RequestHeader (HEADER_USER_ID) Long userId,
                                                     @PathVariable Long bookingId) {
        return bookingClient.getBookingApproved(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingState(@RequestHeader (HEADER_USER_ID) Long userId,
                                            @RequestParam (name = "state", defaultValue = "ALL") BookingState state) {
        return bookingClient.getBookingState(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookingState(@RequestHeader (HEADER_USER_ID) Long userId,
                                                       @RequestParam (name = "state", defaultValue = "ALL")
                                                           BookingState state) {
        return bookingClient.getOwnerBookingState(userId, state);
    }
}
