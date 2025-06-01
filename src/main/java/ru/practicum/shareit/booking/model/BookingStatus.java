package ru.practicum.shareit.booking.model;

import lombok.*;

@Getter
@AllArgsConstructor
public enum BookingStatus {
    WAITING,
    APPROVED,
    REJECTED,
    CANCELLED;
}