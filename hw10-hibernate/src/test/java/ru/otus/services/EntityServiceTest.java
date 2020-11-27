package ru.otus.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.entities.AddressDataSet;
import ru.otus.entities.PhoneDataSet;
import ru.otus.entities.User;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.sql.SQLException;
import java.util.Arrays;

class EntityServiceTest {


    private static Logger logger = LoggerFactory.getLogger(EntityServiceTest.class);
    SessionManagerHibernate sessionManager = new SessionManagerHibernate(HibernateUtils.buildSessionFactory());
    private final EntityService<User> userService = new EntityServiceImp<>(new EntityDaoImpl<User>(sessionManager));
    private User user1;
    private User user2;

    @BeforeEach
    void init() {
        user1 = new User("User 1", 1);
        user1.setAddress(new AddressDataSet("Adress 1"));
        user1.setPhones(Arrays.asList(
                new PhoneDataSet("+1000000001"),
                new PhoneDataSet("+1000000002")));
        user2 = new User("User 2", 2);
        user2.setAddress(new AddressDataSet("Adress 2"));
        user2.setPhones(Arrays.asList(
                new PhoneDataSet("+2000000001"),
                new PhoneDataSet("+2000000002")));
    }

    @Test
    void createUser() throws SQLException {
        logger.info("User1 before save : " + user1.toString());
        userService.create(user1);
        logger.info("User1 after save : " + user1.toString());
        Assertions.assertThat(user1.getId()>0);
    }

    @Test
    void updateUser() throws SQLException {
        logger.info("User1 before save : " + user1.toString());
        userService.create(user1);
        logger.info("User1 after save : " + user1.toString());
        user1.setName("Modified user 1");
        user1.setAge(11);
        user1.setAddress(new AddressDataSet("Modified street 1"));
        user1.setPhones(Arrays.asList(
               new PhoneDataSet("+1000000003"),
               new PhoneDataSet("+1000000004")));
        logger.info("User1 before update : " + user1.toString());
        userService.update(user1);
        User selectedUser = (User) userService.load(user1.getId(), User.class);
        logger.info("User1 after update : " + selectedUser.toString());
        Assertions.assertThat(user1.equals(selectedUser));
    }

    @Test
    void findByID() throws SQLException {
        logger.info("User1 before save : " + user1.toString());
        userService.create(user1);
        logger.info("User1 after save : " + user1.toString());
        User selectedUser = (User) userService.load(user1.getId(), User.class);
        logger.info("Selected user after save : " + selectedUser.toString());
        Assertions.assertThat(user1.equals(selectedUser));
    }
}