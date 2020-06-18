package ru.otus.db.hibernate.sessionmanager;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.otus.db.hibernate.exceptions.SessionManagerException;

public class SessionManagerHibernate implements SessionManager {
    private final SessionFactory sessionFactory;
    private DatabaseSession databaseSession;

    public SessionManagerHibernate(SessionFactory sessionFactory) {
        if(sessionFactory == null){
           throw new SessionManagerException("SessionFactory is null!");
        }
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void beginSession() {
        try {
            databaseSession = new DatabaseSessionHibernate(sessionFactory.openSession());
        } catch (Exception e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public void commitSession() {
        checkSessionAndTransaction();
        try {
            databaseSession.getTransaction().commit();
            databaseSession.getHibernateSession().close();
        } catch (Exception e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public void rollbackSession() {
        checkSessionAndTransaction();
        try {
            databaseSession.getTransaction().rollback();
            databaseSession.getHibernateSession().close();
        } catch (Exception e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public void close() {

        if (databaseSession == null){
            return;
        }

        Session session = databaseSession.getHibernateSession();
        if (session == null || !session.isConnected()){
            return;
        }

        Transaction transaction = databaseSession.getTransaction();
        if (transaction == null || !transaction.isActive()){
            return;
        }

        try {
            databaseSession.close();
            databaseSession = null;
        } catch (Exception e){
            throw new SessionManagerException(e);
        }
    }

    @Override
    public DatabaseSession getCurrentSession() {
        checkSessionAndTransaction();
        return this.databaseSession;
    }

    private void checkSessionAndTransaction(){
        if (databaseSession  == null){
            throw new SessionManagerException("DatabaseSession is null!");
        }
        Session session = databaseSession.getHibernateSession();
        if (session == null || !session.isConnected()){
            throw new SessionManagerException("HibernateSession is null!");
        }
        Transaction transaction = databaseSession.getTransaction();
        if (transaction == null || !transaction.isActive()){
            throw new SessionManagerException("Transaction is null!");
        }
    }
}
