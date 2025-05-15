package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserStorage {

    Optional<User> getUser(long id);

    User addUser(User user);

    User updateUser(User user);

    void deleteUser(Long userId);
}
