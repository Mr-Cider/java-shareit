package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    Map<Long, User> users = new HashMap<>();

    @Override
    public Optional<User> getUser(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User addUser(User user) {
        generateId(user);
        checkEmail(user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId()))
            throw new NotFoundException("Пользователь c id " + user.getId() + " не найден");
        User updateUser = users.get(user.getId());
        if (user.getEmail() != null) {
            checkEmail(user);
            updateUser.setEmail(user.getEmail());
        }
        if (user.getName() != null) updateUser.setName(user.getName());
        return updateUser;
    }

    @Override
    public void deleteUser(Long userId) {
        users.remove(userId);
    }

    private void checkEmail(User user) {
        boolean duplicateEmail = users.values().stream().filter(u -> !u.equals(user)).map(User::getEmail)
                .anyMatch(email -> email.equalsIgnoreCase(user.getEmail()));
        if (duplicateEmail) {
            throw new DuplicateEmailException("Email должен быть уникальным");
        }
    }

    private void generateId(User user) {
        long id = users.keySet().stream().mapToLong(Long::longValue).max().orElse(0L) + 1;
        user.setId(id);
    }
}
