package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers();

    UserDto getUser(long id);

    UserDto createUser(NewUserDto newUserDto);

    UserDto updateUser(UpdateUserDto userDto);
}
