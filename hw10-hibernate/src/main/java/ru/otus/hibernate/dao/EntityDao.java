package ru.otus.hibernate.dao;

import java.sql.SQLException;

public interface EntityDao<T> extends AutoCloseable{
    void create(T objectData) throws SQLException;
    void update(T objectData) throws SQLException;
    void createOrUpdate(T objectData) throws SQLException;
    <T> T load(long id, Class<T> clazz) throws SQLException;
}
