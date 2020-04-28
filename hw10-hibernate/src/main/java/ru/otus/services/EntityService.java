package ru.otus.services;

import java.sql.SQLException;

public interface EntityService <T>{
    void create(T objectData);
    void update(T objectData);
    void createOrUpdate(T objectData);
    <T> T load(long id, Class<T> clazz);
}
