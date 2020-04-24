package ru.otus.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

public class EntityDaoImpl<T> implements EntityDao<T>{
    private static Logger logger = LoggerFactory.getLogger(EntityDaoImpl.class);
    private final SessionFactory sessionFactory;

    public EntityDaoImpl(SessionFactory sessionFactory) {
        logger.info("Init entity DAO implementation");
        this.sessionFactory = sessionFactory;
        logger.info("Session factory created");
    }

    @Override
    public void create(T objectData) throws SQLException {
        logger.info("Call procedure for create entity");
        editBySessionWithTransaction(session -> { session.save(objectData);});
    }

    @Override
    public void update(T objectData) throws SQLException {
        logger.info("Call procedure for update entity");
        editBySessionWithTransaction(session -> { session.update(objectData);});
    }

    @Override
    public void createOrUpdate(T objectData) throws SQLException {
        logger.info("Call procedure for create or update entity");
        editBySessionWithTransaction(session -> {session.saveOrUpdate(objectData);});
    }

    @Override
    public <T1> T1 load(long id, Class<T1> clazz) throws SQLException {
        logger.info("Call function for return entity by ID");
        return queryBySessionWithTransaction(session -> {
            return session.get(clazz, id);
        });
    }

    @Override
    public void close() throws Exception {
        this.sessionFactory.close();
    }

    private void editBySessionWithTransaction(Consumer<Session> consumer){
        try(Session session = this.sessionFactory.openSession()){
            try {
                session.getTransaction().begin();
                consumer.accept(session);
                session.getTransaction().commit();
            } catch (Exception e) {
                logger.error(e.getMessage());
                session.getTransaction().rollback();;
                throw new RuntimeException(e);
            }
        }
    }

    private <T> T queryBySessionWithTransaction(Function<Session, T> function){
        try(Session session = this.sessionFactory.openSession()){
            try {
                session.getTransaction().begin();
                final T result = function.apply(session);
                session.getTransaction().commit();
                return result;
            } catch (Exception e) {
                logger.error(e.getMessage());
                session.getTransaction().rollback();;
                throw new RuntimeException(e);
            }
        }
    }
}
