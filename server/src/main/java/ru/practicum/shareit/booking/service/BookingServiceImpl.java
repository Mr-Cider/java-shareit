package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingDataTransformer;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.repository.BookingStorage;
import ru.practicum.shareit.exception.ForbiddenAccessException;
import ru.practicum.shareit.exception.IncorrectStatusException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserStorage;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingStorage bookingStorage;
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Transactional
    @Override
    public BookingDto createBooking(Long userId, NewBookingDto newBookingDto) {
        User user = checkUser(userId);
        Item item = checkItem(newBookingDto.getItemId());
        if (!item.getAvailable()) throw new IncorrectStatusException("Предмет с id " +
                newBookingDto.getItemId() + " недоступен для бронирования");
        Booking booking = BookingDataTransformer.convertNewBookingDto(newBookingDto);
        booking.setBooker(user);
        booking.setItem(item);
        return BookingDataTransformer.convertToBookingDto(bookingStorage.save(booking));
    }

    @Transactional
    @Override
    public BookingDto approveOrRejectBooking(Long userId, Long bookingId, Boolean approved) {
        int updated = bookingStorage.approveOrRejectBooking(userId, bookingId, approvedStatus(approved));
        if (updated == 0) {
            throw new ForbiddenAccessException("У владельца + " + userId +
                    " нет бронирования для подтверждения с id " + bookingId);
        }
        return getBookingApproved(userId, bookingId);
    }

    @Override
    public BookingDto getBookingApproved(Long userId, Long bookingId) {
        Booking booking = bookingStorage.getBookingApproved(userId, bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));
        return BookingDataTransformer.convertToBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookingState(Long userId, State state) {
        switch (state) {
            case CURRENT -> {
                List<Booking> bookings = bookingStorage
                        .getUserCurrentBookings(userId, BookingStatus.APPROVED, LocalDateTime.now());
                return convertToDtoList(bookings);
            }
            case PAST -> {
                List<Booking> bookings = bookingStorage
                        .getUserPastBookings(userId, BookingStatus.APPROVED, LocalDateTime.now());
                return convertToDtoList(bookings);
            }
            case FUTURE -> {
                List<Booking> bookings = bookingStorage
                        .getUserFutureBookings(userId, BookingStatus.APPROVED, LocalDateTime.now());
                return convertToDtoList(bookings);
            }
            case WAITING -> {
                return getBookingWithStatus(userId, BookingStatus.WAITING);
            }
            case REJECTED -> {
                return getBookingWithStatus(userId, BookingStatus.REJECTED);
            }
        }
        List<Booking> bookings = bookingStorage.findByBooker_Id(userId);
        return convertToDtoList(bookings);
    }

    @Override
    public List<BookingDto> getOwnerBookingState(Long ownerId, State state) {
        itemStorage.findAllByOwnerId(ownerId).stream().findFirst()
                .orElseThrow(() -> new NotFoundException("У пользователя с id " + " нет ни одной вещи"));
        switch (state) {
            case CURRENT -> {
                List<Booking> bookings = bookingStorage
                        .getOwnerCurrentBookings(ownerId, BookingStatus.APPROVED, LocalDateTime.now());
                return convertToDtoList(bookings);
            }
            case PAST -> {
                List<Booking> bookings = bookingStorage
                        .getOwnerPastBookings(ownerId, BookingStatus.APPROVED, LocalDateTime.now());
                return convertToDtoList(bookings);
            }
            case FUTURE -> {
                List<Booking> bookings = bookingStorage
                        .getOwnerFutureBookings(ownerId, BookingStatus.APPROVED, LocalDateTime.now());
                return convertToDtoList(bookings);
            }
            case WAITING -> {
                return getOwnerBookingWithStatus(ownerId, BookingStatus.WAITING);
            }
            case REJECTED -> {
                return getOwnerBookingWithStatus(ownerId, BookingStatus.REJECTED);
            }
        }
        List<Booking> bookings = bookingStorage
                .getOwnerAllBookings(ownerId);
        return convertToDtoList(bookings);
    }

    private Item checkItem(Long itemId) {
        return itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена"));
    }

    private User checkUser(Long userId) {
        return userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
    }

    private List<BookingDto> getBookingWithStatus(Long userId, BookingStatus status) {
        List<Booking> bookings = bookingStorage.findByBookerIdAndBookingStatus(userId, status);
        return convertToDtoList(bookings);
    }

    private List<BookingDto> getOwnerBookingWithStatus(Long ownerId, BookingStatus status) {
        List<Booking> bookings = bookingStorage.getOwnerBookingsWithStatus(ownerId, status);
        return convertToDtoList(bookings);
    }

    private List<BookingDto> convertToDtoList(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingDataTransformer::convertToBookingDto)
                .sorted(Comparator.comparing(BookingDto::getStart))
                .collect(Collectors.toList());
    }

    private BookingStatus approvedStatus(Boolean approved) {
        if (approved) {
            return BookingStatus.APPROVED;
        }
        return BookingStatus.REJECTED;
    }
}
