package ru.practicum.shareit.user;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    List<User> getUsers();

    Optional<User> getUser(long id);

    User addUser(User user);

    User updateUser(User user);
}
