package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingApprovedDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingWithStatusDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {
    BookingWithStatusDto createBooking(Long userId, NewBookingDto newBookingDto);

    BookingWithStatusDto approveOrRejectBooking(Long userId, Long bookingId, Boolean approved);

    BookingWithStatusDto getBookingApproved(Long userId, Long bookingId);

    List<BookingWithStatusDto> getBookingState(Long userId, State state);

    List<BookingWithStatusDto> getOwnerBookingState(Long userId, State state);
}
