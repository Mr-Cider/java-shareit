package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UpdateItemDto {
    private Long id;
    private String name;
    private String description;
    private Long request;
    private Boolean available;
}
