package ru.otus.messagesystem.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.db.cachehw.HwCache;
import ru.otus.db.cachehw.MyCache;
import ru.otus.entities.User;
import ru.otus.db.hibernate.HibernateUtils;
import ru.otus.db.hibernate.sessionmanager.SessionManager;
import ru.otus.db.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.db.services.AdminUserDao;
import ru.otus.db.services.AdminUserDaoImpl;
import ru.otus.db.services.AdminUserService;
import ru.otus.db.services.AdminUserServiceImp;

@Configuration
public class Config {
    @Bean
    public AdminUserDao getAdminUserDao(){
        HibernateUtils hibernateUtils = new HibernateUtils("ru.otus.entities");
        SessionManager sessionManager = new SessionManagerHibernate(hibernateUtils.buildSessionFactory());
        AdminUserDao userDao = new AdminUserDaoImpl(sessionManager);
        return userDao;
    }

    @Bean
    public HwCache<Long, User> getCacheByUser(){
        HwCache<Long, User> userHwCache = new MyCache<>(10);
        return userHwCache;
    }

    @Bean
    public AdminUserService getAdminUserService(AdminUserDao adminDao, HwCache<Long, User> cache){
        AdminUserService adminUserService = new AdminUserServiceImp(adminDao, cache);
        return adminUserService;
    }

}
