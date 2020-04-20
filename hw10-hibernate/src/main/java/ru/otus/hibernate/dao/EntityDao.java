package ru.otus.hibernate.dao;

import java.io.Serializable;
import java.util.List;

public interface EntityDao<T> extends AutoCloseable{
    void create(T entity);
    void update(T entity);
    T findByID(Serializable id, Class clazz);
    long getCount(Class clazz);
    List<T> selectAll(Class clazz);
    void delete(T entity);
    void deleteAll(Class clazz);
}
