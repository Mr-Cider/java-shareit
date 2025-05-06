package ru.practicum.shareit.item;


import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemStatusDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemStatus;

@Service
public class ItemDataTransformer {

    public Item convertToItem(Long userId, ItemDto itemDto) {
        return Item.builder()
                .ownerId(userId)
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .status(itemDto.getStatus().getId())
                .build();
    }

    public ItemDto convertToItemDto(Item item) {
        ItemStatusDto itemStatusDto = convertToItemStatusDto(item.getItemStatus());
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .status(itemStatusDto)
                .build();
    }

    public Item convertNewItemDto(Long userId, NewItemDto newItemDto) {
        return Item.builder()
                .ownerId(userId)
                .name(newItemDto.getName())
                .description(newItemDto.getDescription())
                .build();
    }

    public Item convertUpdateItemToItem(Long userId, UpdateItemDto updateItemDto) {
        return Item.builder()
                .id(updateItemDto.getId())
                .ownerId(userId)
                .name(updateItemDto.getName())
                .description(updateItemDto.getDescription())
                .build();
    }

    public ItemStatusDto convertToItemStatusDto(ItemStatus itemStatus) {
        return ItemStatusDto.builder()
                .id(itemStatus.getId())
                .name(itemStatus.getName())
                .build();
    }
}
