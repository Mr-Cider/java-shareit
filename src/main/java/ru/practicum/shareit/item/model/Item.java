package ru.practicum.shareit.item.model;

import lombok.*;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
@AllArgsConstructor
public class Item {
    private Long id;
    private Long ownerId;
    private String name;
    private String description;
    private Long itemRequest;
    private Boolean available;
}
