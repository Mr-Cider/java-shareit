package ru.practicum.shareit.booking.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum BookingStatus {
    WAITING(1, "WAITING"),
    APPROVED(2, "APPROVED"),
    REJECTED(3, "REJECTED"),
    CANCELLED(4, "CANCELLED");

    private final long id;
    private final String name;

    public String getBookingStatus() {
        return name;
    }

    public static BookingStatus getBookingStatus(long id) {
        return Arrays.stream(values()).filter(status -> status.id == id)
                .findFirst().orElseThrow(() -> new NotFoundException("ID не найден"));
    }
}


