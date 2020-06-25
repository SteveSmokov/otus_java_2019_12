package ru.otus.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.otus.entities.User;
import ru.otus.messages.AuthMessage;
import ru.otus.messages.ResultMessage;
import ru.otus.service.FrontendServiceMSClient;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;


@Controller
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private FrontendServiceMSClient frontendService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public UserController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/")
    public String getIndexPage() {
        return "index.html";
    }

    @MessageMapping("/login")
    public void authorizationUser(AuthMessage message) throws InterruptedException {
        logger.info("Message {}",message);
        frontendService.getUserByLogin(message.getLogin(), data -> {
            if (data.getPassword().equals(message.getPassword())) {
                logger.info("Result is OK");
                simpMessagingTemplate.convertAndSend("/topic/authresult", new ResultMessage("OK"));
            } else {
                logger.info("Login or password is not correct");
                simpMessagingTemplate.convertAndSend("/topic/authresult",
                        new ResultMessage("Login or password is not correct"));
            }
        });
    }

    @MessageMapping("/user/list")
    public void getUserListPage() throws InterruptedException {
        frontendService.getUserList(data -> {
            logger.info("Список пользователей: {}", data);
            simpMessagingTemplate.convertAndSend("/topic/user/list", data);
        });
    }

    @MessageMapping("/user/save")
    public void saveUser(User user) {
        logger.info("User for save {}",user);
        try {
            frontendService.createOrUpdate(user, data -> {
                logger.info("Saved user {}",data);
                if (data.getId()>0) {
                    logger.info("User saved successeful");
                    simpMessagingTemplate.convertAndSend("/topic/user/save", new ResultMessage("OK"));
                } else {
                    logger.info("User saved successeful");
                    simpMessagingTemplate.convertAndSend("/topic/user/save", new ResultMessage("User is not saved")); }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            simpMessagingTemplate.convertAndSend("/topic/user/save", new ResultMessage(e.getMessage()));
        }
    }

    @MessageMapping("/user/edit.{userID}")
    public void getUserEditPage(@DestinationVariable String userID) throws InterruptedException {
        logger.info("Edit user id = "+userID);
        frontendService.load(Long.parseLong(userID), data -> {
            logger.info("Пользователь для редактирования {}", data);
            simpMessagingTemplate.convertAndSend("/topic/user/edit."+userID,data);
        });
    }

}
