package ru.otus.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hibernate.HibernateUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class EntityDaoImpl implements EntityDao{
    private static Logger logger = LoggerFactory.getLogger(EntityDaoImpl.class);
    private final SessionFactory sessionFactory;

    public EntityDaoImpl() {
        logger.info("Init entity DAO implementation");
        this.sessionFactory = HibernateUtils.buildSessionFactory();
        logger.info("Session factory created");
    }

    @Override
    public void create(Object entity) {
        logger.info("Call procedure for create entity");
        editBySessionWithTransaction(session -> { session.save(entity);});
    }

    @Override
    public void update(Object entity) {
        logger.info("Call procedure for update entity");
        editBySessionWithTransaction(session -> { session.update(entity);});
    }

    @Override
    public Object findByID(Serializable id, Class clazz) {
        logger.info("Call function for return entity by ID");
        return queryBySessionWithTransaction(session -> {
            return session.get(clazz, id);
        });
    }

    @Override
    public long getCount(Class clazz) {
        logger.info(String.format("Call function for return entity %s count", clazz.getSimpleName()));
        return queryBySessionWithTransaction(session -> {
            Query query = session.createQuery(String.format("select count(e) from %s e",clazz.getSimpleName()));
            return (long)query.getSingleResult();
        });
    }

    @Override
    public List selectAll(Class clazz) {
        logger.info("Call function for return all entities in DB");
        return queryBySessionWithTransaction(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            final CriteriaQuery criteria = builder.createQuery(clazz);
            criteria.from(clazz);
            final Query query = session.createQuery(criteria);
            return query.getResultList();
        });
    }

    @Override
    public void delete(Object entity) {
        logger.info("Call procedure for delete entity");
        editBySessionWithTransaction(session -> { session.delete(entity);});
    }

    @Override
    public void deleteAll(Class clazz) {
        logger.info("Call procedure for delete all entities in DB");
        editBySessionWithTransaction(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            final CriteriaDelete criteria = builder.createCriteriaDelete(clazz);
            criteria.from(clazz);
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
