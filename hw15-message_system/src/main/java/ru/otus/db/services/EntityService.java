package ru.otus.db.services;

import ru.otus.entities.User;

import java.util.Optional;

public interface EntityService <T>{
    void create(T objectData);
    void update(T objectData);
    void createOrUpdate(T objectData);
    <T> T load(long id, Class<T> clazz);
}
