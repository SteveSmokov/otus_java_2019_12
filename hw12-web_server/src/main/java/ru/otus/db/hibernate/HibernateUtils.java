package ru.otus.db.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;

import javax.persistence.Entity;
import java.util.Set;

public final class HibernateUtils {
    private static SessionFactory sessionFactory = null;

    static {
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        Set<Class<?>> entitySet;
        entitySet = new Reflections("ru.otus.entities").getTypesAnnotatedWith(Entity.class);
        entitySet.forEach(aClass -> configuration.addAnnotatedClass(aClass));
        sessionFactory = configuration.buildSessionFactory();
    }

    public static SessionFactory buildSessionFactory(){
        return sessionFactory;
    }
}

