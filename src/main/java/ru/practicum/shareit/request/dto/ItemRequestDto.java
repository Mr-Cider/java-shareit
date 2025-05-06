package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@RequiredArgsConstructor
@Builder
public class ItemRequestDto {
    private Long id;
    private String description;
    private User requestor;
    private LocalDateTime requestDate;
}
