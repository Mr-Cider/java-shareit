package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class UserDto {
    private Long id;
    @Email
    private String email;
    private String name;
}
