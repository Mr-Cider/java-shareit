package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public class InMemoryItemStorage implements ItemStorage {

    private final Map<Long, List<Item>> items = new HashMap<>();

    @Override
    public Item addItem(Item item) {
        generateId(item);
        List<Item> itemList = items.computeIfAbsent(item.getOwnerId(), k -> new ArrayList<>());
        itemList.add(item);
        items.put(item.getOwnerId(), itemList);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        if (item == null || item.getId() == null || item.getOwnerId() == null) {
            throw new NotFoundException("Предмет не найден");
        }
        List<Item> itemList = getUserItems(item.getOwnerId());
        Item newItem = itemList.stream().filter(item1 -> item1.getId().equals(item.getId()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Предмет с id " + item.getId() + "не найден"));
        if (item.getName() != null) newItem.setName(item.getName());
        if (item.getDescription() != null) newItem.setDescription(item.getDescription());
        if (item.getAvailable() != null) newItem.setAvailable(item.getAvailable());
        return newItem;
    }

    @Override
    public Optional<Item> getItem(Long id) {
        return getAllItems().stream().filter(o -> o.getId().equals(id)).findFirst();
    }

    @Override
    public List<Item> getAllItems() {
        return items.values().stream().flatMap(Collection::stream).toList();
    }

    @Override
    public List<Item> getUserItems(Long userId) {
        return items.getOrDefault(userId, Collections.emptyList());
    }

    @Override
    public List<Item> searchItems(String text) {
        return getAllItems().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()) ||
                item.getDescription().toLowerCase().contains(text.toLowerCase())).toList();
    }

    private void generateId(Item item) {
        long id = getAllItems().stream().mapToLong(Item::getId).max().orElse(0L) + 1;
        item.setId(id);
    }
}
