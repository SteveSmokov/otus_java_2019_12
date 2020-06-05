package ru.otus.db.services;

import ru.otus.db.hibernate.sessionmanager.SessionManager;


public interface EntityDao<T>{
    void create(T objectData);
    void update(T objectData);
    void createOrUpdate(T objectData);
    <T> T load(long id, Class<T> clazz);
    SessionManager getSessionManager();
}
