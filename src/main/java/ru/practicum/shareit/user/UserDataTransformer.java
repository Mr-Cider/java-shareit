package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserDataTransformer {

    public static User convertNewUser(NewUserDto newUserDto) {
        return User.builder()
                .email(newUserDto.getEmail())
                .name(newUserDto.getName())
                .build();
    }

    public static User convertUpdateUser(Long userId, UpdateUserDto updateUserDto) {
        return User.builder()
                .id(userId)
                .email(updateUserDto.getEmail())
                .name(updateUserDto.getName())
                .build();
    }


    public static UserDto convertToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
