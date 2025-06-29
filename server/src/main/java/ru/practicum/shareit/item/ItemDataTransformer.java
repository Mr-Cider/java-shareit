package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.dto.BookingDateDto;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class ItemDataTransformer {

    public static ItemDto convertToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static Item convertNewItemDto(User owner, NewItemDto newItemDto) {
        return Item.builder()
                .name(newItemDto.getName())
                .owner(owner)
                .description(newItemDto.getDescription())
                .available(newItemDto.getAvailable())
                .build();
    }

    public static Comment convertToComment(User author, Item item, NewCommentDto newCommentDto) {
        return Comment.builder()
                .text(newCommentDto.getText())
                .author(author)
                .item(item)
                .created(LocalDateTime.now())
                .build();
    }

    public static CommentDto convertToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .item(convertToItemDto(comment.getItem()))
                .build();
    }

    public static ItemWithBookingsDto convertToItemWithBookingsDto(Item item, BookingDateDto lastBooking,
                                                                   BookingDateDto nextBooking,
                                                                   List<CommentDto> comments) {
        return ItemWithBookingsDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(comments)
                .request(item.getRequest() != null ? item.getRequest().getId() : null)
                .available(item.getAvailable())
                .build();
    }

    public static ItemForRequestDto convertToItemForRequestDto(Item item) {
        return ItemForRequestDto.builder()
                .id(item.getId())
                .name(item.getName())
                .ownerId(item.getOwner().getId())
                .build();
    }
}
