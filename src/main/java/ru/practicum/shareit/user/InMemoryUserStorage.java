package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getUser(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User addUser(User user) {
        generateId(user);
        users.put(user.getId(), user);
    }

    @Override
    public User updateUser(User user) {
        return users.put(user.getId(), user); //ДОДОДЕЛАТЬ
    }

    private void generateId(User user) {
        long id = users.keySet().stream().mapToLong(Long::longValue).max().orElse(0L) + 1;
        user.setId(id);
    }
}
