package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

@Service
public class UserDataTransformer {

    public User convertNewUser(NewUserDto newUserDto) {
        return User.builder()
                .email(newUserDto.getEmail())
                .name(newUserDto.getName())
                .build();
    }

    public User convertUpdateUser(UpdateUserDto updateUserDto) {
        return User.builder()
                .id(updateUserDto.getId())
                .email(updateUserDto.getEmail())
                .name(updateUserDto.getName())
                .build();
    }


    public UserDto convertToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
