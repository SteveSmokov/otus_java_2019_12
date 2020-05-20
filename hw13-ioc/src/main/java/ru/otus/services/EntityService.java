package ru.otus.services;

public interface EntityService <T>{
    void create(T objectData);
    void update(T objectData);
    void createOrUpdate(T objectData);
    <T> T load(long id, Class<T> clazz);
}
