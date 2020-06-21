package ru.otus.db.hibernate.sessionmanager;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class DatabaseSessionHibernate implements DatabaseSession {
    private final Session session;
    private final Transaction transaction;

    public DatabaseSessionHibernate(Session session) {
        this.session = session;
        this.transaction = session.beginTransaction();
    }

    @Override
    public Session getHibernateSession() {
        return this.session;
    }

    @Override
    public Transaction getTransaction() {
        return this.transaction;
    }

    @Override
    public void close() {
        if(this.transaction.isActive()){
            this.transaction.commit();
        }
        this.session.close();
    }
}
