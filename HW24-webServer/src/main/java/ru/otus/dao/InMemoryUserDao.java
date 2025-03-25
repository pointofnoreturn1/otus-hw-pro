package ru.otus.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import ru.otus.model.User;

public class InMemoryUserDao implements UserDao {
    private final Map<String, User> users;

    public InMemoryUserDao() {
        users = new HashMap<>();
        users.put("admin", new User(1L, "admin", "admin", "11111"));
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return Optional.of(users.get(login));
    }
}
