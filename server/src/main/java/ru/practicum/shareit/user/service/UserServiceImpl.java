package ru.practicum.shareit.user.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserDataTransformer;
import ru.practicum.shareit.user.repository.UserStorage;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public UserDto getUser(long id) {
        return userStorage.findById(id)
                .map(UserDataTransformer::convertToUserDto).orElseThrow(() -> new NotFoundException("Пользователь с ID " + id + "не найден"));
    }

    @Transactional
    @Override
    public UserDto createUser(NewUserDto newUserDto) {
        checkEmail(newUserDto.getEmail());
        User user = userStorage.save(UserDataTransformer.convertNewUser(newUserDto));
        return UserDataTransformer.convertToUserDto(user);
    }

    @Transactional
    @Override
    public UserDto updateUser(Long userId, UpdateUserDto userDto) {
        checkEmail(userDto.getEmail());
        User user = getUserOrThrow(userId);
        updateFields(user, userDto);
        return UserDataTransformer.convertToUserDto(user);
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        userStorage.deleteById(userId);
    }

    private User getUserOrThrow(Long userId) {
        return userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
    }

    private void updateFields(User user, UpdateUserDto userDto) {
        if (userDto.getName() != null) user.setName(userDto.getName());
        if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
    }

    private void checkEmail(String email) {
        if (userStorage.existsByEmailIgnoreCase(email)) throw new DuplicateEmailException("Email уже занят");
    }
}
