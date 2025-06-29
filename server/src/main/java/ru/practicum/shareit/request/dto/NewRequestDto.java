package ru.practicum.shareit.request.dto;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewRequestDto {
    private String description;
}
