package ru.otus.services;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hibernate.exceptions.EntityDaoException;
import ru.otus.hibernate.sessionmanager.*;

import java.util.function.Consumer;
import java.util.function.Function;

public class EntityDaoImpl<T> implements EntityDao<T> {
    private static Logger logger = LoggerFactory.getLogger(EntityDaoImpl.class);
    private final SessionManager sessionManager;

    public EntityDaoImpl(SessionManager sessionManager) {
        logger.info("Init entity DAO implementation");
        this.sessionManager = sessionManager;
        logger.info("Session factory created");
    }

    @Override
    public void create(T objectData) {
        logger.info("Call procedure for create entity");
        edit(session -> { session.persist(objectData);});
    }

    @Override
    public void update(T objectData) {
        logger.info("Call procedure for update entity");
        edit(session -> { session.merge(objectData);});
    }

    @Override
    public void createOrUpdate(T objectData) {
        logger.info("Call procedure for create or update entity");
        edit(session -> {session.saveOrUpdate(objectData);});
    }

    @Override
    public <T1> T1 load(long id, Class<T1> clazz) {
        logger.info("Call function for return entity by ID");
        return query(session -> {
            return session.get(clazz, id);
        });
    }

    @Override
    public SessionManager getSessionManager() {
        return this.sessionManager;
    }

    private void edit(Consumer<Session> consumer){
        DatabaseSession currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            consumer.accept(hibernateSession);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new EntityDaoException(e);
        }

    }

    private <T> T query(Function<Session, T> function){
        DatabaseSession currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            return function.apply(hibernateSession);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new EntityDaoException(e);
        }
    }
}
