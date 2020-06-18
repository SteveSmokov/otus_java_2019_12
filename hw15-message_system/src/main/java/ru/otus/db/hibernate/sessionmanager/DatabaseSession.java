package ru.otus.db.hibernate.sessionmanager;

import org.hibernate.Session;
import org.hibernate.Transaction;

public interface DatabaseSession {

    Session getHibernateSession();
    Transaction getTransaction();
    void close();
}
