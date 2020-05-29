package ru.otus.spring.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;
import ru.otus.entities.User;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.sessionmanager.SessionManager;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.services.AdminUserDao;
import ru.otus.services.AdminUserDaoImpl;
import ru.otus.services.AdminUserService;
import ru.otus.services.AdminUserServiceImp;

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
