package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemStatus;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Data
@RequiredArgsConstructor
@Builder
public class BookingDto {
    Long id;
    LocalDateTime startBooking;
    LocalDateTime endBooking;
    Item item;
    User booker;
    ItemStatus bookingStatus;
}
