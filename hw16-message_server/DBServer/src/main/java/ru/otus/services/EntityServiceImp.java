package ru.otus.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hibernate.sessionmanager.SessionManager;

import java.util.function.Consumer;
import java.util.function.Function;

public class EntityServiceImp<T> implements EntityService<T> {
    private static Logger logger = LoggerFactory.getLogger(EntityServiceImp.class);
    private final EntityDao<T> entityDao;

    public EntityServiceImp(EntityDao<T> entityDao) {
        this.entityDao = entityDao;
    }

    @Override
    public void create(T objectData) {
        editBySessionWithTransaction(sessionManager -> { entityDao.create(objectData);});
    }

    @Override
    public void update(T objectData) {
        editBySessionWithTransaction(sessionManager -> { entityDao.update(objectData);});
    }

    @Override
    public void createOrUpdate(T objectData) {
        editBySessionWithTransaction(sessionManager -> { entityDao.createOrUpdate(objectData);});
    }

    @Override
    public <T> T load(long id, Class<T> clazz) {
        return queryBySessionWithTransaction(sessionManager -> {
            return entityDao.load(id, clazz);
        });
    }

    private void editBySessionWithTransaction(Consumer<SessionManager> consumer){
        try (SessionManager sessionManager = entityDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                consumer.accept(sessionManager);
                sessionManager.commitSession();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DBServiceException(e);
            }
        }
    }

    private <T> T queryBySessionWithTransaction(Function<SessionManager, T> function){
        try (SessionManager sessionManager = entityDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                T result = function.apply(sessionManager);
                logger.info("user: {}", result);
                return result;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DBServiceException(e);
            }
        }
    }
}
