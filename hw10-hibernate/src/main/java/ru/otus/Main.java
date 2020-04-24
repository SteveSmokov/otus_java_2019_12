package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.entities.Account;
import ru.otus.entities.AddressDataSet;
import ru.otus.entities.PhoneDataSet;
import ru.otus.entities.User;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.EntityDao;
import ru.otus.hibernate.dao.EntityDaoImpl;

import java.sql.SQLException;
import java.util.Arrays;

public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws SQLException {
        User user1 = new User("Vasya", 21);
        user1.setAddress(new AddressDataSet("Street 1"));
        user1.setPhones(Arrays.asList(
                new PhoneDataSet("+1000000001"),
                new PhoneDataSet("+1000000002")));
        EntityDao<User> userDao = new EntityDaoImpl<>(HibernateUtils.buildSessionFactory());
        logger.info(user1.toString());
        userDao.create(user1);
        logger.info(user1.toString());

        user1.setName("Valera");
        user1.setAge(23);
        userDao.update(user1);
        logger.info(user1.toString());

        user1.setAge(32);
        userDao.createOrUpdate(user1);
        logger.info(user1.toString());

        User user2 = new User("Василий Пупкин", 45);
        logger.info(user2.toString());
        userDao.createOrUpdate(user2);
        logger.info(user2.toString());

        User selectedUser = userDao.load(user1.getId(), User.class);
        logger.info(selectedUser.toString());
        try {
            User selectedUser2 = userDao.load(129, User.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        Account account1 = new Account("Account 1", 35684);
        EntityDao<Account> accountDao = new EntityDaoImpl<Account>(HibernateUtils.buildSessionFactory());
        logger.info(account1.toString());
        accountDao.create(account1);
        logger.info(account1.toString());

        account1.setType("Changed account 1");
        account1.setRest(33);
        accountDao.update(account1);
        logger.info(account1.toString());

        account1.setRest(30);
        accountDao.createOrUpdate(account1);
        logger.info(account1.toString());

        Account account2 = new Account("Account 2", 45000);
        logger.info(account2.toString());
        accountDao.createOrUpdate(account2);
        logger.info(account2.toString());

        Account selectedAccount = accountDao.load(account1.getNo(), Account.class);
        logger.info(selectedAccount.toString());
        try {
            Account selectedAccount2 = accountDao.load(2, Account.class);
            logger.info(selectedAccount2.toString());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
