package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.enteties.User;
import ru.otus.jdbc.dbservice.DataSourceH2;
import ru.otus.service.ServiceDB;
import ru.otus.service.ServiceDBImpl;

import javax.sql.DataSource;
import java.sql.SQLException;

public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws SQLException {
        User userTest;
        DataSource dataSource = new DataSourceH2();
        ServiceDB<User> userService = new ServiceDBImpl<>(dataSource, User.class);
        userService.deleteTable();
        userService.createTable();
        logger.info("*                  Add users                          *");
        for(int i=1; i<11; i++) {
            userTest = new User("User_"+String.valueOf(i), i);
            logger.info(userTest.toString());
            userService.create(userTest);
            logger.info(userTest.toString());
        }
        logger.info("*                Select users                         *");
        for(int i=1; i<11; i++) {
            userTest = userService.load(i, User.class);
            logger.info(userTest.toString());
        }

    }
}
