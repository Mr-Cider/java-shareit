package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingDataTransformer;
import ru.practicum.shareit.booking.dto.BookingDateDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingStorage;
import ru.practicum.shareit.exception.IncorrectAccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemDataTransformer;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
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
public class ItemServiceImpl implements ItemService {


    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final BookingStorage bookingStorage;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public ItemDto addItem(Long userId, NewItemDto newItemDto) {
        User owner = getUserOrThrow(userId);
        Item item = itemStorage.save(ItemDataTransformer.convertNewItemDto(owner, newItemDto));
        return ItemDataTransformer.convertToItemDto(item);
    }

    @Transactional
    @Override
    public ItemDto updateItem(Long userId, UpdateItemDto updateItemDto) {
        Item item = getItemOrThrow(updateItemDto.getId(), userId);
        updateFields(item, updateItemDto);
        return ItemDataTransformer.convertToItemDto(item);
    }

    @Override
    public ItemWithBookingsDto getItem(Long id, Long userId) {
        Item item = itemStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Предмет с id " + id + "не найден"));
        BookingDateDto lastBooking = null;
        BookingDateDto nextBooking = null;
        if (item.getOwner().getId().equals(userId)) {
            List<Booking> bookings = bookingStorage.findByItemId(id);
            lastBooking = getLastBooking(bookings);
            nextBooking = getNextBooking(bookings);
        }
        List<CommentDto> comments = commentRepository.findByItemId(id).stream().map(ItemDataTransformer::convertToCommentDto).collect(Collectors.toList());
        return ItemDataTransformer.convertToItemWithBookingsDto(item, lastBooking, nextBooking, comments);
    }

    @Override
        public List<ItemDto> getUserItems(Long id) {
        return itemStorage.findAllByOwnerId(id).stream()
                .map(ItemDataTransformer::convertToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        return itemStorage.searchItems(text).stream()
                .map(ItemDataTransformer::convertToItemDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentDto addComment(Long userId, Long itemId, NewCommentDto newCommentDto) {
        User user = getUserOrThrow(userId);
        checkItemByUserBookings(userId, itemId);
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет с id " + itemId + " не найден"));
        Comment comment = ItemDataTransformer.convertToComment(user, item, newCommentDto);
        commentRepository.save(comment);
        return ItemDataTransformer.convertToCommentDto(comment);
    }

    private User getUserOrThrow(Long userId) {
        return userStorage.findById(userId).stream().findFirst()
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
    }

    private void updateFields(Item item, UpdateItemDto updateItemDto) {
        if (updateItemDto.getName() != null) item.setName(updateItemDto.getName());
        if (updateItemDto.getDescription() != null) item.setDescription(updateItemDto.getDescription());
        if (updateItemDto.getAvailable() != null) item.setAvailable(updateItemDto.getAvailable());
    }

    private Item getItemOrThrow(Long itemId, Long userId) {
        return itemStorage.findByIdAndOwner_Id(itemId, userId)
                .orElseThrow(() -> new NotFoundException("У пользователя с id "
                        + userId + " не найден предмет с id " + itemId));
    }

    private void checkItemByUserBookings(Long userId, Long itemId) {
        List<Booking> bookings = bookingStorage.getUserPastBookingsAllStatus(userId, LocalDateTime.now());
        bookings.stream()
                .filter(booking -> booking.getItem().getId().equals(itemId))
                .findFirst()
                .orElseThrow(() ->
                        new IncorrectAccessException("Пользователь с id + " + userId +
                                " не брал в аренду предмет с id " + itemId));
    }

    private BookingDateDto getLastBooking(List<Booking> bookings) {
        return bookings.stream()
                .filter(b -> b.getBookingStatus() == BookingStatus.APPROVED)
                .filter(b -> b.getEndDate().isBefore(LocalDateTime.now()))
                .max(Comparator.comparing(Booking::getEndDate))
                .map(BookingDataTransformer::convertToBookingDateDto)
                .orElse(null);
    }

    private BookingDateDto getNextBooking(List<Booking> bookings) {
        return bookings.stream()
                .filter(b -> b.getBookingStatus() == BookingStatus.APPROVED)
                .filter(b -> b.getStartDate().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(Booking::getStartDate))
                .map(BookingDataTransformer::convertToBookingDateDto)
                .orElse(null);
    }
}
