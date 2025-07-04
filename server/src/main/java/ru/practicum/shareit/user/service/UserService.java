package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {

    UserDto getUser(long id);

    UserDto createUser(NewUserDto newUserDto);

    UserDto updateUser(Long userId, UpdateUserDto userDto);

    void deleteUser(Long userId);
}
