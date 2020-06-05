package ru.otus.db.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.entities.User;
import ru.otus.db.hibernate.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class AdminUserServiceImp implements AdminUserService {
   private static Logger logger = LoggerFactory.getLogger(AdminUserServiceImp.class);
    private final AdminUserDao entityDao;

    public AdminUserServiceImp(AdminUserDao adminDao) {
        this.entityDao = adminDao;
    }

    @Override
    public void create(User objectData) {
        editBySessionWithTransaction(sessionManager -> {
            entityDao.create(objectData);
        });
    }

    @Override
    public void update(User objectData) {
        editBySessionWithTransaction(sessionManager -> { entityDao.update(objectData);});
    }

    @Override
    public void createOrUpdate(User objectData) {
        editBySessionWithTransaction(sessionManager -> {
            entityDao.createOrUpdate(objectData);
        });
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

    @Override
    public Optional<User> getUserByLogin(String login) {
        return queryBySessionWithTransaction(sessionManager -> {
            return entityDao.getUserByLogin(login);
        });
    }

    @Override
    public List<User> getUserList() {
        return queryBySessionWithTransaction(sessionManager -> {
            return entityDao.getUserList();
        });
    }
}
