package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.ItemDataTransformer;
import ru.practicum.shareit.user.UserDataTransformer;

public class BookingDataTransformer {
    public static Booking convertNewBookingDto(NewBookingDto newBookingDto) {
        return Booking.builder()
                .startDate(newBookingDto.getStart())
                .endDate(newBookingDto.getEnd())
                .bookingStatus(BookingStatus.WAITING)
                .build();
    }

    public static BookingDto convertToBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStartDate())
                .end(booking.getEndDate())
                .item(ItemDataTransformer.convertToItemDto(booking.getItem()))
                .booker(UserDataTransformer.convertToUserDto(booking.getBooker()))
                .status(booking.getBookingStatus().name())
                .build();
    }

    public static BookingDateDto convertToBookingDateDto(Booking booking) {
        return BookingDateDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .start(booking.getStartDate())
                .end(booking.getEndDate())
                .build();
    }
}
