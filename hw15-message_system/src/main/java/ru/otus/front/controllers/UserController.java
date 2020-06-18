package ru.otus.front.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import ru.otus.entities.User;
import ru.otus.front.FrontendServiceMSClient;
import ru.otus.front.messages.AuthMessage;
import ru.otus.front.messages.ResultMessage;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;


@Controller
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private FrontendServiceMSClient frontendService;


    @MessageMapping("/")
    public String getIndexPage() {
        return "index.html";
    }

    @MessageMapping("/login")
    @SendTo("/topic/authresult")
    public ResultMessage authorizationUser(AuthMessage message) throws InterruptedException {
        logger.info("Message {}",message);
        CountDownLatch waitLatch = new CountDownLatch(1);
        AtomicBoolean authorized = new AtomicBoolean(false);
        frontendService.getUserByLogin(message.getLogin(), data -> {
            authorized.set(data.getPassword().equals(message.getPassword()));
            waitLatch.countDown();
        });
        waitLatch.await();
        if (authorized.get()) return new ResultMessage("OK");
        else return new ResultMessage("Login or password is not correct");
    }

    @MessageMapping("/user/list.{userLogin}")
    @SendTo("/topic/user/list.{userLogin}")
    public List<User> getUserListPage(@DestinationVariable String userLogin) throws InterruptedException {
        CountDownLatch waitLatch = new CountDownLatch(1);
        AtomicReference<List<User>> users = new AtomicReference<List<User>>();
        frontendService.getUserList(data -> {
            logger.info("Список пользователей: {}", data);
            users.set(data);
            waitLatch.countDown();
        });
        waitLatch.await();
        return users.get();
    }

//    @MessageMapping("/user/save")
//    public void saveUser(@ModelAttribute User user) {
//        logger.info("Сохранение пользователя: {}", user);
//        adminUserService.createOrUpdate(user);
//        return  new RedirectView("/user/list", true);
//    }

//    @GetMapping("/user/edit")
//    public String getUserEditPage(Model model
//            , @RequestParam(name = WEB_PAR_USER_ID) String id){
//        logger.info("Edit user id = "+id);
//        User user = adminUserService.load(Long.parseLong(id), User.class);
//        model.addAttribute("user", user);
//        logger.info("Редактирование пользователя: {}", user);
//        return "userEdit.html";
//    }

//    @GetMapping("/user/create")
//    public String getCreateUserPage(Model model){
//        model.addAttribute("user", new User());
//        return "userEdit.html";
//    }

}
