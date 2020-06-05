package ru.otus.db.services;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.entities.User;
import ru.otus.db.hibernate.exceptions.EntityDaoException;
import ru.otus.db.hibernate.sessionmanager.DatabaseSession;
import ru.otus.db.hibernate.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class AdminUserDaoImpl extends EntityDaoImpl<User> implements AdminUserDao {
    private static Logger logger = LoggerFactory.getLogger(EntityDaoImpl.class);
    private final SessionManager sessionManager;

    public AdminUserDaoImpl(SessionManager sessionManager) {
        super(sessionManager);
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        logger.info("Call function for return User by login");
        return query(session -> {
            Query<User> query = session.createQuery("select u from User u where u.login=:login", User.class)
                    .setParameter("login", login);
            return Optional.of(query.getSingleResult());
        });
    }

    @Override
    public List<User> getUserList() {
        return query(session -> {
            Query<User> query = session.createQuery("select u from User u", User.class);
            return query.getResultList();
        });
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
