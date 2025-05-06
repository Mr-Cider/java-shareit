package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class UpdateItemDto {
    @NotNull
    private Long id;
    private String name;
    private String description;
    private ItemStatusDto status;
}
