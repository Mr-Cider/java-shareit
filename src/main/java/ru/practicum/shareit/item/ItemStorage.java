package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    Item addItem(Item item);

    Item updateItem(Item item);

    Item getItem(Long id);

    List<Item> getUserItems(Long userId);

    List<Item> searchItems(String text);
}
