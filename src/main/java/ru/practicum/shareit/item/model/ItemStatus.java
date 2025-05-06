package ru.practicum.shareit.item.model;

import lombok.Getter;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Arrays;

@Getter
public enum ItemStatus {
    FREE(1, "FREE"),
    BUSY(2, "BUSY");

    private final long id;
    private final String name;

    ItemStatus(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getDisplayStatus() {
        return name;
    }

    public static ItemStatus getStatusById(long id) {
        return Arrays.stream(values())
                .filter(status -> status.getId() == id)
                .findFirst().orElseThrow(() -> new NotFoundException("ID не найден"));
    }
}
