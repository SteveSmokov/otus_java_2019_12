package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.enteties.Account;
import ru.otus.enteties.User;
import ru.otus.jdbc.dao.EntetyException;
import ru.otus.jdbc.dbservice.DataSourceH2;
import ru.otus.jdbc.templates.JdbcTemplate;
import ru.otus.jdbc.templates.JdbcTemplateImpl;

import javax.sql.DataSource;
import java.sql.SQLException;

public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws SQLException {
        User user1 = new User("Vasya", 21);
        DataSource dataSource = new DataSourceH2();
        JdbcTemplate<User> userJdbc = new JdbcTemplateImpl<>(dataSource, User.class);
        userJdbc.deleteTable();
        userJdbc.createTable();
        logger.info(user1.toString());
        userJdbc.create(user1);
        logger.info(user1.toString());

        user1.setName("Valera");
        user1.setAge(23);
        userJdbc.update(user1);
        logger.info(user1.toString());

        user1.setAge(32);
        userJdbc.createOrUpdate(user1);
        logger.info(user1.toString());

        User user2 = new User("Василий Пупкин", 45);
        logger.info(user2.toString());
        userJdbc.createOrUpdate(user2);
        logger.info(user2.toString());

        User selectedUser = userJdbc.load(user1.getId(), User.class);
        logger.info(selectedUser.toString());
        try {
            User selectedUser2 = userJdbc.load(129, User.class);
        } catch (EntetyException e) {
            logger.error(e.getMessage());
        }

        Account account1 = new Account("Account 1", 35684);
        JdbcTemplate<Account> accountJdbc = new JdbcTemplateImpl<>(dataSource, Account.class);
        accountJdbc.deleteTable();
        accountJdbc.createTable();
        logger.info(account1.toString());
        accountJdbc.create(account1);
        logger.info(account1.toString());

        account1.setType("Changed account 1");
        account1.setRest(33);
        accountJdbc.update(account1);
        logger.info(account1.toString());

        account1.setRest(30);
        accountJdbc.createOrUpdate(account1);
        logger.info(account1.toString());

        Account account2 = new Account("Account 2", 45000);
        logger.info(account2.toString());
        accountJdbc.createOrUpdate(account2);
        logger.info(account2.toString());

        Account selectedAccount = accountJdbc.load(account1.getNo(), Account.class);
        logger.info(selectedAccount.toString());
        try {
            Account selectedAccount2 = accountJdbc.load(2, Account.class);
            logger.info(selectedAccount2.toString());
        } catch (EntetyException e) {
            logger.error(e.getMessage());
        }
    }
}
