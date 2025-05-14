package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;

import java.util.List;

public interface ItemService {

    ItemDto addItem(Long userId, NewItemDto newItemDto);

    ItemDto updateItem(Long userId, UpdateItemDto updateItemDto);

    ItemDto getItem(Long id);

    List<ItemDto> getUserItems(Long id);

    List<ItemDto> searchItems(String text);
}
