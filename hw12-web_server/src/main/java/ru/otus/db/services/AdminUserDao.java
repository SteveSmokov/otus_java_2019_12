package ru.otus.db.services;

import ru.otus.entities.User;

import java.util.List;
import java.util.Optional;

public interface AdminUserDao extends EntityDao<User> {
    Optional<User> getUserByLogin(String login);
    List<User> getUserList();
}
