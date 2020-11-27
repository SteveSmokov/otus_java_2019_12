package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.entities.Account;
import ru.otus.entities.AddressDataSet;
import ru.otus.entities.PhoneDataSet;
import ru.otus.entities.User;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.services.EntityDaoImpl;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.services.EntityService;
import ru.otus.services.EntityServiceImp;

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
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(HibernateUtils.buildSessionFactory());
        EntityService<User> userService = new EntityServiceImp<>(new EntityDaoImpl<User>(sessionManager));
        logger.info("***************************************************************************************************");
        logger.info(user1.toString());
        userService.create(user1);
        logger.info(user1.toString());

        user1.setName("Valera");
        user1.setAge(23);
        userService.update(user1);
        logger.info(user1.toString());

        user1.setAge(32);
        userService.createOrUpdate(user1);
        logger.info(user1.toString());

        User user2 = new User("Василий Пупкин", 45);
        logger.info(user2.toString());
        userService.createOrUpdate(user2);
        logger.info(user2.toString());

        User selectedUser = userService.load(user1.getId(), User.class);
        logger.info(selectedUser.toString());
        try {
            User selectedUser2 = userService.load(129, User.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        Account account1 = new Account("Account 1", 35684);
        EntityService<Account> accountService = new EntityServiceImp<Account>(new EntityDaoImpl<Account>(sessionManager));
        logger.info(account1.toString());
        accountService.create(account1);
        logger.info(account1.toString());

        account1.setType("Changed account 1");
        account1.setRest(33);
        accountService.update(account1);
        logger.info(account1.toString());

        account1.setRest(30);
        accountService.createOrUpdate(account1);
        logger.info(account1.toString());

        Account account2 = new Account("Account 2", 45000);
        logger.info(account2.toString());
        accountService.createOrUpdate(account2);
        logger.info(account2.toString());

        Account selectedAccount = accountService.load(account1.getNo(), Account.class);
        logger.info(selectedAccount.toString());
        try {
            Account selectedAccount2 = accountService.load(2, Account.class);
            logger.info(selectedAccount2.toString());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
