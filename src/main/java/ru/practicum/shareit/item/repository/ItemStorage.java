package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {

    Item addItem(Item item);

    Item updateItem(Item item);

    Optional<Item> getItem(Long id);

    List<Item> getAllItems();

    List<Item> getUserItems(Long userId);

    List<Item> searchItems(String text);
}
