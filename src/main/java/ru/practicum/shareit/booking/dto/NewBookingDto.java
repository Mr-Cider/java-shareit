package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class NewBookingDto {
    @NotNull
    LocalDate startBooking;
    @NotNull
    LocalDate endBooking;
    @NotNull
    Long itemId;
}
