package ru.otus.service;

import java.sql.SQLException;

public interface ServiceDB<T> {
    void createTable() throws SQLException;
    void deleteTable() throws SQLException;
    void create(T objectData) throws SQLException;
    void update(T objectData) throws SQLException;
    void createOrUpdate(T objectData) throws SQLException;
    <T> T load(long id, Class<T> clazz) throws SQLException;
}