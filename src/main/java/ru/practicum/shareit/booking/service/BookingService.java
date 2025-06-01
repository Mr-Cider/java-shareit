package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(Long userId, NewBookingDto newBookingDto);

    BookingDto approveOrRejectBooking(Long userId, Long bookingId, Boolean approved);

    BookingDto getBookingApproved(Long userId, Long bookingId);

    List<BookingDto> getBookingState(Long userId, State state);

    List<BookingDto> getOwnerBookingState(Long userId, State state);
}
