package ru.practicum.shareit.booking.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingWithStatusDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("bookings")
@Validated
@Slf4j
@RequiredArgsConstructor
public class BookingController {

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    public BookingWithStatusDto createBooking(@RequestHeader (HEADER_USER_ID) Long userId,
                                    @Valid @RequestBody NewBookingDto newBookingDto) {
        log.info("Создаем бронирование");
        log.info("Валидация прошла успешно");
        return bookingService.createBooking(userId, newBookingDto);
    }

    @PatchMapping("/{itemId}")
    public BookingWithStatusDto approveOrRejectBooking(@RequestHeader (HEADER_USER_ID) Long ownerId,
                                             @Positive @PathVariable Long itemId,
                                             @RequestParam(name = "approved") Boolean approved) {
        log.info("Подтверждаем или отклоняем бронирование");
        return bookingService.approveOrRejectBooking(ownerId, itemId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingWithStatusDto getBookingApproved(@RequestHeader (HEADER_USER_ID) Long userId,
                                                 @PathVariable Long bookingId) {
        log.info("Проверяем подтверждение бронирования");
        return bookingService.getBookingApproved(userId, bookingId);
    }

    @GetMapping
    public List<BookingWithStatusDto> getBookingState(@RequestHeader (HEADER_USER_ID) Long userId,
                                  @RequestParam (name = "state", defaultValue = "ALL") State state) {
        log.info("Получаем список бронирований пользователя с id {}", userId);
        return bookingService.getBookingState(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingWithStatusDto> getOwnerBookingState(@RequestHeader (HEADER_USER_ID) Long userId,
                                                           @RequestParam (name = "state", defaultValue = "ALL")
                                                           State state) {
        log.info("Получаем список бронирований для вещей пользователя c id {}", userId);
        return bookingService.getOwnerBookingState(userId, state);
    }
}
