package ru.otus.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
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
    public SessionManager getSessionManager(){
        return new SessionManagerHibernate(HibernateUtils.buildSessionFactory());
    }

    @Bean
    public AdminUserDao getAdminUserDao(SessionManager sessionManager){
        return new AdminUserDaoImpl(sessionManager);
    }

    @Bean
    public HwCache<Long, User> getCacheByUser(){
        HwCache<Long, User> userHwCache = new MyCache<>(10);
        return userHwCache;
    }

    @Bean
    public AdminUserService getAdminUserService(AdminUserDao adminDao, HwCache<Long, User> cache){
        return new AdminUserServiceImp(adminDao, cache);
    }

    @Bean
    public ThymeleafViewResolver viewResolver(SpringTemplateEngine templateEngine) {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine);
        viewResolver.setOrder(1);
        viewResolver.setCharacterEncoding("UTF-8");
        return viewResolver;
    }
}
