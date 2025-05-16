package ru.practicum.shareit.item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemDataTransformer {

    public static Item convertToItem(Long userId, ItemDto itemDto) {
        return Item.builder()
                .ownerId(userId)
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .itemRequest(itemDto.getRequest() != null ? itemDto.getRequest() : null)
                .available(itemDto.getAvailable())
                .build();
    }

    public static ItemDto convertToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .request(item.getItemRequest() != null ? item.getItemRequest() : null)
                .available(item.getAvailable())
                .build();
    }

    public static Item convertNewItemDto(Long userId, NewItemDto newItemDto) {
        return Item.builder()
                .ownerId(userId)
                .name(newItemDto.getName())
                .description(newItemDto.getDescription())
                .available(newItemDto.getAvailable())
                .build();
    }

    public static Item convertUpdateItemToItem(Long userId, UpdateItemDto updateItemDto) {
        return Item.builder()
                .id(updateItemDto.getId())
                .ownerId(userId)
                .name(updateItemDto.getName())
                .description(updateItemDto.getDescription())
                .itemRequest(updateItemDto.getRequest() != null ? updateItemDto.getRequest() : null)
                .available(updateItemDto.getAvailable())
                .build();
    }
}
