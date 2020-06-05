package ru.otus.db.services;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.entities.AddressDataSet;
import ru.otus.entities.PhoneDataSet;
import ru.otus.entities.User;
import ru.otus.db.hibernate.HibernateUtils;
import ru.otus.db.hibernate.sessionmanager.SessionManagerHibernate;

import java.sql.SQLException;
import java.util.Arrays;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EntityServiceTest_2 {


    private static Logger logger = LoggerFactory.getLogger(EntityServiceTest_2.class);
    SessionManagerHibernate sessionManager = new SessionManagerHibernate(HibernateUtils.buildSessionFactory());
    private final EntityService<User> userService = new EntityServiceImp<>(new EntityDaoImpl<User>(sessionManager));

    @Test
    @Order(1)
    void createUser() throws SQLException {
        User user1 = new User("User 1", "user1", "test");
        user1.setAddress(new AddressDataSet("Adress 1"));
        user1.setPhones(Arrays.asList(
                new PhoneDataSet("+1000000001"),
                new PhoneDataSet("+1000000002")));
        logger.info("User1 before save : " + user1.toString());
        userService.create(user1);
        logger.info("User1 after save : " + user1.toString());
        Assertions.assertThat(user1.getId()>0);
    }

    @Test
    @Order(2)
    void updateUser() throws SQLException {
        User user2 = new User("User 2", "user2", "test");
        user2.setAddress(new AddressDataSet("Adress 1"));
        user2.setPhones(Arrays.asList(
                new PhoneDataSet("+1000000001"),
                new PhoneDataSet("+1000000002")));
        logger.info("User2 before save : " + user2.toString());
        userService.create(user2);
        logger.info("User2 after save : " + user2.toString());
        user2.setName("Modified user 2");
        user2.setAge(11);
        user2.setAddress(new AddressDataSet("Modified street 1"));
        user2.setPhones(Arrays.asList(
               new PhoneDataSet("+1000000003"),
               new PhoneDataSet("+1000000004")));
        logger.info("User2 before update : " + user2.toString());
        userService.update(user2);
        User selectedUser = (User) userService.load(user2.getId(), User.class);
        logger.info("User2 after update : " + selectedUser.toString());
        Assertions.assertThat(user2.equals(selectedUser));
    }

    @Test
    @Order(3)
    void findByID() throws SQLException {
        User user3 = new User("User 3", "user3", "test");
        user3.setAddress(new AddressDataSet("Adress 1"));
        user3.setPhones(Arrays.asList(
                new PhoneDataSet("+1000000001"),
                new PhoneDataSet("+1000000002")));
        logger.info("User3 before save : " + user3.toString());
        userService.create(user3);
        logger.info("User3 after save : " + user3.toString());
        User selectedUser = (User) userService.load(1, User.class);
        logger.info("Selected user after save : " + selectedUser.toString());
        Assertions.assertThat(user3.equals(selectedUser));
    }
}