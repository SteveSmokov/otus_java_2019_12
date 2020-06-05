package ru.otus.web.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.entities.PhoneDataSet;
import ru.otus.entities.User;
import ru.otus.db.hibernate.HibernateUtils;
import ru.otus.db.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.db.services.AdminUserDaoImpl;
import ru.otus.db.services.AdminUserService;
import ru.otus.db.services.AdminUserServiceImp;

import java.util.Arrays;

class UserAuthServiceTest {
    private static Logger logger = LoggerFactory.getLogger(UserAuthServiceTest.class);
    SessionManagerHibernate sessionManager = new SessionManagerHibernate(HibernateUtils.buildSessionFactory());
    private final AdminUserService userService = new AdminUserServiceImp(new AdminUserDaoImpl(sessionManager));
    private final UserAuthService userAuthService =  new UserAuthServiceImpl(userService);
    private User admin;
    @BeforeEach
    void init() {
        admin = new User("Administrator", "admin", "admin");
        admin.setPhones(Arrays.asList(new PhoneDataSet("(495) 937-99-92")));
        userService.create(admin);
    }

    @Test
    void authUserTest(){
        Assertions.assertTrue(userAuthService.authUser("admin", "admin"));
    }

}