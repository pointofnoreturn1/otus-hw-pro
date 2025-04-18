package ru.otus.dao;

import java.util.Optional;
import ru.otus.model.User;

public interface UserDao {
    Optional<User> findByLogin(String login);
}
