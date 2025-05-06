package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final ItemDataTransformer dataTransformer;

    @Override
    public ItemDto addItem(Long userId, NewItemDto newItemDto) {
        Item item = dataTransformer.convertNewItemDto(userId, newItemDto);
        return dataTransformer.convertToItemDto(itemStorage.addItem(item));
    }

    @Override
    public ItemDto updateItem(Long userId, UpdateItemDto updateItemDto) {
        Item item = dataTransformer.convertUpdateItemToItem(userId, updateItemDto);
        return dataTransformer.convertToItemDto(itemStorage.updateItem(item));
    }

    @Override
    public Optional<ItemDto> getItem(Long id) {
//        return itemStorage.getItem(id).map(dataTransformer::convertToItemDto);
        return null;
    }

    @Override
    public List<ItemDto> getUserItems(Long id) {
        return itemStorage.getUserItems(id).stream()
                .map(dataTransformer::convertToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        return itemStorage.searchItems(text).stream()
                .map(dataTransformer::convertToItemDto)
                .collect(Collectors.toList());
    }
}
