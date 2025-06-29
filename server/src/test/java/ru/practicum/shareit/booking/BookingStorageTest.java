package ru.practicum.shareit.booking;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingStorage;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

@ActiveProfiles("test")
@DataJpaTest
@Sql(scripts = "/data.sql")
@DisplayName("Booking Storage Tests")
public class BookingStorageTest {
    @Autowired
    BookingStorage bookingStorage;

    @DisplayName("Подтверждение бронирования")
    @Test
    public void shouldApproveOrRejectBooking() {
        int updated = bookingStorage.approveOrRejectBooking(1L, 1L, BookingStatus.APPROVED);
        assertThat(updated).isEqualTo(1);
    }

    @DisplayName("Подтверждение бронирования сторонним пользователем")
    @Test
    public void shouldApproveOrRejectBookingWithUserId() {
        int updated = bookingStorage.approveOrRejectBooking(2L, 1L, BookingStatus.APPROVED);
        assertThat(updated).isEqualTo(0);
    }

    //
    @DisplayName("Получение подтверждения бронирования")
    @Test
    public void shouldGetBookingApproved() {
        Booking booking = bookingStorage.getBookingApproved(1L, 1L).orElse(null);
        assertThat(booking)
                .isNotNull()
                .hasFieldOrPropertyWithValue("item.id", 1L)
                .hasFieldOrPropertyWithValue("booker.id", 2L);
    }

    @DisplayName("Получение подтверждения бронирования")
    @Test
    public void shouldGetBookingApprovedByOtherUser() {
        Booking booking = bookingStorage.getBookingApproved(3L, 1L).orElse(null);
        assertThat(booking).isNull();
    }

    @DisplayName("Найти бронирования по id автора")
    @Test
    public void shouldFindByBookerId() {
        List<Booking> bookings = bookingStorage.findByBooker_Id(2L);
        assertThat(bookings).hasSize(6);
    }

    @DisplayName("Найти бронирование по id автора и статусу")
    @Test
    public void shouldFindByBookerIdAndBookingStatus() {
        List<Booking> bookings = bookingStorage.findByBookerIdAndBookingStatus(2L, BookingStatus.APPROVED);
        assertThat(bookings).hasSize(3);
    }

    @DisplayName("Получить список будущих бронирований")
    @Test
    public void shouldGetUserFutureBookings() {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingStorage
                .getUserFutureBookings(2L, BookingStatus.APPROVED, now);
        assertThat(bookings).hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("booker.id", 2L)
                .hasFieldOrPropertyWithValue("bookingStatus", BookingStatus.APPROVED);
    }

    @DisplayName("Получить список прошедших бронирований")
    @Test
    public void shouldGetUserPastBookings() {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingStorage
                .getUserPastBookings(2L, BookingStatus.APPROVED, now);
        assertThat(bookings)
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("booker.id", 2L)
                .hasFieldOrPropertyWithValue("bookingStatus", BookingStatus.APPROVED);
    }

    @DisplayName("Получить список прошедших бронирований (все статусы)")
    @Test
    public void shouldGetUserPastBookingsAllStatus() {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingStorage
                .getUserPastBookingsAllStatus(2L, now);
        assertThat(bookings)
                .hasSize(2)
                .anyMatch(b -> b.getBookingStatus() == BookingStatus.APPROVED)
                .anyMatch(b -> b.getBookingStatus() == BookingStatus.REJECTED);
    }

    @DisplayName("Получить список текущих бронирований")
    @Test
    public void shouldGetUserCurrentBookings() {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingStorage.getUserCurrentBookings(2L, BookingStatus.APPROVED, now);
        assertThat(bookings)
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("booker.id", 2L)
                .hasFieldOrPropertyWithValue("bookingStatus", BookingStatus.APPROVED);
    }

    @DisplayName("Получить список текущих бронирований вещей владельца")
    @Test
    public void shouldGetOwnerCurrentBookings() {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingStorage.getOwnerCurrentBookings(1L, BookingStatus.APPROVED, now);
        assertThat(bookings)
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("booker.id", 2L)
                .hasFieldOrPropertyWithValue("bookingStatus", BookingStatus.APPROVED);
    }

    @DisplayName("Получить список прошедших бронирований вещей владельца")
    @Test
    public void shouldGetOwnerPastBookings() {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingStorage.getOwnerPastBookings(1L, BookingStatus.APPROVED, now);
        assertThat(bookings)
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("booker.id", 2L)
                .hasFieldOrPropertyWithValue("bookingStatus", BookingStatus.APPROVED);
    }

    @DisplayName("Получить список будущих бронирований вещей владельца")
    @Test
    public void shouldGetOwnerFutureBookings() {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingStorage.getOwnerFutureBookings(1L, BookingStatus.APPROVED, now);
        assertThat(bookings)
                .hasSize(1)
                .first()
                .hasFieldOrPropertyWithValue("booker.id", 2L)
                .hasFieldOrPropertyWithValue("bookingStatus", BookingStatus.APPROVED);
    }

    @DisplayName("Получить список будущих бронирований вещей владельца со статусом REJECTED")
    @Test
    public void shouldGetOwnerBookingsWithStatus() {
        List<Booking> bookings = bookingStorage.getOwnerBookingsWithStatus(1L, BookingStatus.REJECTED);
        assertThat(bookings)
                .hasSize(2)
                .first()
                .hasFieldOrPropertyWithValue("booker.id", 2L)
                .hasFieldOrPropertyWithValue("bookingStatus", BookingStatus.REJECTED);
    }

    @DisplayName("Получить список всех бронирований вещей владельца")
    @Test
    public void shouldGetOwnerAllBookings() {
        List<Booking> bookings = bookingStorage
                .getOwnerAllBookings(1L);
        assertThat(bookings)
                .hasSize(6)
                .anyMatch(b -> b.getBookingStatus() == BookingStatus.APPROVED)
                .anyMatch(b -> b.getBookingStatus() == BookingStatus.REJECTED)
                .anyMatch(b -> b.getBookingStatus() == BookingStatus.WAITING);
    }

    @DisplayName("Найти бронирование по id вещи")
    @Test
    public void shouldFindByItemId() {
        List<Booking> bookings = bookingStorage.findByItemId(1L);
        assertThat(bookings)
                .hasSize(6);
    }
}


