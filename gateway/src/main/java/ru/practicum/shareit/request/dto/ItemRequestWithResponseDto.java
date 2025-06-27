package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemForRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ItemRequestWithResponseDto {
    private Long id;
    private String description;
    private UserDto requestor;
    private LocalDateTime requestDate;
    List<ItemForRequestDto> items;
}
