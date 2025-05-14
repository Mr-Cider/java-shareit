package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

/**
 * TODO Sprint add-bookings.
 */

@Data
@Builder
@AllArgsConstructor
public class Booking {
    Long id;
    LocalDate startBooking;
    LocalDate endBooking;
    Item item;
    User booker;
    BookingStatus bookingStatus;
}
