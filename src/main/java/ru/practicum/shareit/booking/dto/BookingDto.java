package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class BookingDto {
    LocalDate startBooking;
    LocalDate endBooking;
    ItemDto item;
    BookingStatus bookingStatus;
}
