package ru.otus.services;

import ru.otus.hibernate.sessionmanager.SessionManager;


public interface EntityDao<T>{
    void create(T objectData);
    void update(T objectData);
    void createOrUpdate(T objectData);
    <T> T load(long id, Class<T> clazz);
    SessionManager getSessionManager();
}
