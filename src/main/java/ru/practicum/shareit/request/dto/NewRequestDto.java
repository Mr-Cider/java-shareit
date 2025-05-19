package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Data
@AllArgsConstructor
public class NewRequestDto {
    @NotBlank
    private String description;
}
