package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserDataTransformer;
import ru.practicum.shareit.user.repository.UserStorage;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public UserDto getUser(long id) {
        return userStorage.getUser(id).map(UserDataTransformer::convertToUserDto).orElseThrow(() -> new NotFoundException("Пользователь с ID " + id + "не найден"));
    }

    @Override
    public UserDto createUser(NewUserDto newUserDto) {
        User user = userStorage.addUser(UserDataTransformer.convertNewUser(newUserDto));
        return UserDataTransformer.convertToUserDto(user);
    }

    @Override
    public UserDto updateUser(Long userId, UpdateUserDto userDto) {
        User user = userStorage.updateUser(UserDataTransformer.convertUpdateUser(userId, userDto));
        return UserDataTransformer.convertToUserDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userStorage.deleteUser(userId);
    }
}
