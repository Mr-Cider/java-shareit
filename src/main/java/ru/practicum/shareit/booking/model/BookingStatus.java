package ru.practicum.shareit.booking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum BookingStatus {
    WAITING,
    APPROVED,
    REJECTED,
    CANCELLED;
}


