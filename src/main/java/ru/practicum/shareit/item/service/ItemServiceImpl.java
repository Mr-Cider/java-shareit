package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemDataTransformer;
import ru.practicum.shareit.item.repository.ItemStorage;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    @Override
    public ItemDto addItem(Long userId, NewItemDto newItemDto) {
        return null;
    }

    @Override
    public ItemDto updateItem(Long userId, UpdateItemDto updateItemDto) {
        return null;
    }

    @Override
    public ItemDto getItem(Long id) {
        return null;
    }

    @Override
    public List<ItemDto> getUserItems(Long id) {
        return List.of();
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        return List.of();
    }
    //
//    private final ItemStorage itemStorage;
//    private final UserStorage userStorage;
//
//    @Override
//    public ItemDto addItem(Long userId, NewItemDto newItemDto) {
//        checkUser(userId);
//        Item item = ItemDataTransformer.convertNewItemDto(userId, newItemDto);
//        return ItemDataTransformer.convertToItemDto(itemStorage.addItem(item));
//    }
//
//    @Override
//    public ItemDto updateItem(Long userId, UpdateItemDto updateItemDto) {
//        checkUser(userId);
//        Item item = ItemDataTransformer.convertUpdateItemToItem(userId, updateItemDto);
//        return ItemDataTransformer.convertToItemDto(itemStorage.updateItem(item));
//    }
//
//    @Override
//    public ItemDto getItem(Long id) {
//        return itemStorage.getItem(id).stream().map(ItemDataTransformer::convertToItemDto).findFirst()
//                .orElseThrow(() -> new NotFoundException("Предмет с id " + id + "не найден"));
//    }
//
//    @Override
//    public List<ItemDto> getUserItems(Long id) {
//        return itemStorage.getUserItems(id).stream()
//                .map(ItemDataTransformer::convertToItemDto)
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<ItemDto> searchItems(String text) {
//        return itemStorage.searchItems(text).stream()
//                .map(ItemDataTransformer::convertToItemDto)
//                .collect(Collectors.toList());
//    }
//
//    private void checkUser(Long userId) {
//        if (userStorage.getUser(userId).isEmpty()) {
//            throw new NotFoundException("Пользователь с id " + userId + " не найден");
//        }
//    }
}
