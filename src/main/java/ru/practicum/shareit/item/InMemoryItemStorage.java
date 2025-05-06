package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemStorage implements ItemStorage {

    List<Item> items = new ArrayList<>();

    @Override
    public Item addItem(Item item) {
        generateId(item);
        items.add(item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        Item newItem = items.stream().filter(i -> i.getId().equals(item.getId())).findFirst()
                .orElseThrow(() -> new RuntimeException("Вещь с id + " + item.getId() + " не найдена"));
        newItem.setName(item.getName());
        newItem.setDescription(item.getDescription());
        newItem.setStatus(item.getStatus());
        return newItem;
    }

    @Override
    public Item getItem(Long id) {
        return items.stream().filter(item -> item.getId().equals(id)).findFirst().orElseThrow(() -> new NotFoundException("Вещь с id + " + id + " не найдена"));
    }

    @Override
    public List<Item> getUserItems(Long userId) {
        return items.stream()
                .filter(item -> Objects.equals(item.getOwnerId(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItems(String text) {
        return items.stream().filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())).collect(Collectors.toList());
    }

    private void generateId(Item item) {
        long id = items.stream().mapToLong(Item::getId).max().orElse(0L) + 1;
        item.setId(id);
    }
}
