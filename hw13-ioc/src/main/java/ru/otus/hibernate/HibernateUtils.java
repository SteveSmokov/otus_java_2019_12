package ru.otus.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.reflections.Reflections;

import javax.persistence.Entity;
import java.util.Set;

public class HibernateUtils {
    private final Configuration configuration;

    public HibernateUtils(String entitiesPath) {
        this.configuration = new Configuration().configure("hibernate.cfg.xml");
        Set<Class<?>> entitySet;
        entitySet = new Reflections(entitiesPath).getTypesAnnotatedWith(Entity.class);
        entitySet.forEach(aClass -> configuration.addAnnotatedClass(aClass));
    }

    public SessionFactory buildSessionFactory(){
        return this.configuration.buildSessionFactory();
    }
}

