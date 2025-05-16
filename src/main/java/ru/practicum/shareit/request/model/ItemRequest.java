package ru.practicum.shareit.request.model;

import lombok.*;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Builder
@AllArgsConstructor
public class ItemRequest {
    private Long id;
    private String description;
    private Long requestor;
    private LocalDateTime requestDate;
}
