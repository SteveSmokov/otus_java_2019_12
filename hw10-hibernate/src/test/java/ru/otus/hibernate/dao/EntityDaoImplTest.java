package ru.otus.hibernate.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.entities.AddressDataSet;
import ru.otus.entities.PhoneDataSet;
import ru.otus.entities.User;

import java.util.Arrays;
import java.util.List;

class EntityDaoImplTest {
    private static Logger logger = LoggerFactory.getLogger(EntityDaoImplTest.class);
    private final EntityDao userEntity = new EntityDaoImpl();
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
    void createUser() {
        logger.info("User1 before save : " + user1.toString());
        userEntity.create(user1);
        logger.info("User1 after save : " + user1.toString());
        Assertions.assertThat(user1.getId()>0);
    }

    @Test
    void updateUser() {
        logger.info("User1 before save : " + user1.toString());
        userEntity.create(user1);
        logger.info("User1 after save : " + user1.toString());
        user1.setName("Modified user 1");
        user1.setAge(11);
        user1.setAddress(new AddressDataSet("Modified street 1"));
        user1.setPhones(Arrays.asList(
               new PhoneDataSet("+1000000003"),
               new PhoneDataSet("+1000000004")));
        logger.info("User1 before update : " + user1.toString());
        userEntity.update(user1);
        User selectedUser = (User) userEntity.findByID(user1.getId(), User.class);
        logger.info("User1 after update : " + selectedUser.toString());
        Assertions.assertThat(user1.equals(selectedUser));
    }

    @Test
    void findByID() {
        logger.info("User1 before save : " + user1.toString());
        userEntity.create(user1);
        logger.info("User1 after save : " + user1.toString());
        User selectedUser = (User) userEntity.findByID(user1.getId(), User.class);
        logger.info("Selected user after save : " + selectedUser.toString());
        Assertions.assertThat(user1.equals(selectedUser));
    }

    @Test
    void getCount() {
        logger.info("User1 before save : " + user1.toString());
        userEntity.create(user1);
        logger.info("User1 after save : " + user1.toString());
        logger.info("User2 before save : " + user2.toString());
        userEntity.create(user2);
        logger.info("User2 after save : " + user2.toString());
        Assertions.assertThat(userEntity.getCount(User.class) == 2);
    }

    @Test
    void selectAll() {
        logger.info("User1 before save : " + user1.toString());
        userEntity.create(user1);
        logger.info("User1 after save : " + user1.toString());
        logger.info("User2 before save : " + user2.toString());
        userEntity.create(user2);
        logger.info("User2 after save : " + user2.toString());
        List<User> users = userEntity.selectAll(User.class);
        logger.info("Println all users");
        logger.info(users.toString());
        Assertions.assertThat(users.size() == 2);
    }

    @Test
    void delete() {
        logger.info("User1 before save : " + user1.toString());
        userEntity.create(user1);
        logger.info("User1 after save : " + user1.toString());
        logger.info("User2 before save : " + user2.toString());
        userEntity.create(user2);
        logger.info("User2 after save : " + user2.toString());
        userEntity.delete(user2);
        Assertions.assertThat(userEntity.getCount(User.class) == 1);
    }

    @Test
    void deleteAll() {
        logger.info("User1 before save : " + user1.toString());
        userEntity.create(user1);
        logger.info("User1 after save : " + user1.toString());
        logger.info("User2 before save : " + user2.toString());
        userEntity.create(user2);
        logger.info("User2 after save : " + user2.toString());
        userEntity.deleteAll(User.class);
        Assertions.assertThat(userEntity.getCount(User.class) == 0);
    }
}