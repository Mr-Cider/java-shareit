package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {

    ItemDto addItem(Long userId, NewItemDto newItemDto);

    ItemDto updateItem(Long userId, UpdateItemDto updateItemDto);

    ItemWithBookingsDto getItem(Long id, Long userId);

    List<ItemDto> getUserItems(Long id);

    List<ItemDto> searchItems(String text);

    CommentDto addComment(Long userId, Long itemId, NewCommentDto newCommentDto);
}
