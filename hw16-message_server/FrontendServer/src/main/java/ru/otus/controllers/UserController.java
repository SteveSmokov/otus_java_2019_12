package ru.otus.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import ru.otus.entities.User;
import ru.otus.messages.AuthMessage;
import ru.otus.messages.ResultMessage;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;


@Controller
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
//    private FrontendServiceMSClient frontendService;


    @MessageMapping("/")
    public String getIndexPage() {
        return "index.html";
    }

    @MessageMapping("/login")
//    @SendTo("/topic/authresult")
    public void authorizationUser(AuthMessage message) throws InterruptedException {
        logger.info("Message {}",message);
        AtomicBoolean authorized = new AtomicBoolean(false);
//        frontendService.getUserByLogin(message.getLogin(), data -> {
//            authorized.set(data.getPassword().equals(message.getPassword()));
//            waitLatch.countDown();
//        });
//        if (authorized.get()) return new ResultMessage("OK");
//        else return new ResultMessage("Login or password is not correct");
    }

    @MessageMapping("/user/list")
    @SendTo("/topic/user/list")
    public void getUserListPage() throws InterruptedException {
//        List<User> users;
//        frontendService.getUserList(data -> {
//            logger.info("Список пользователей: {}", data);
//            users.set(data);
//
//        });
//        waitLatch.await();
//        return users.get();
    }

    @MessageMapping("/user/save")
    @SendTo("/topic/user/save")
    public ResultMessage saveUser(User user) {
        logger.info("User for save {}",user);
        try {
            CountDownLatch waitLatch = new CountDownLatch(1);
            AtomicBoolean savedUser = new AtomicBoolean(false);
//            frontendService.createOrUpdate(user, data -> {
//                logger.info("Saved user {}",data);
//                savedUser.set(data.getId()>0);
//                waitLatch.countDown();
//            });
            waitLatch.await();
            if (savedUser.get()) return new ResultMessage("OK");
            else return new ResultMessage("User is not saved");
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
            return new ResultMessage(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultMessage(e.getMessage());
        }
    }

    @MessageMapping("/user/edit.{userID}")
    @SendTo("/topic/user/edit.{userID}")
    public User getUserEditPage(@DestinationVariable String userID) throws InterruptedException {
        logger.info("Edit user id = "+userID);
        CountDownLatch waitLatch = new CountDownLatch(1);
        AtomicReference<User> user = new AtomicReference<User>();
//        frontendService.load(Long.parseLong(userID), data -> {
//            logger.info("Пользователь для редактирования {}", data);
//            user.set(data);
//            waitLatch.countDown();
//        });
        waitLatch.await();
        return user.get();
    }

}
