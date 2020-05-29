package ru.otus.spring.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.entities.User;
import ru.otus.services.AdminUserService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Controller
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    private final String WEB_PAR_USERNAME = "login";
    private final String WEB_PAR_PASWORD = "password";
    private final String WEB_PAR_USER_ID = "id";

    private final AdminUserService adminUserService;

    public UserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping({"/","/login"})
    public String getLoginPage() {
        return "login.html";
    }

    @PostMapping("/login")
    public RedirectView authorizationUser(
            @RequestParam(name = WEB_PAR_USERNAME) String login
            , @RequestParam(name = WEB_PAR_PASWORD) String password
            , HttpServletResponse resp) {
        RedirectView redirectView = new RedirectView("user/list", true);
        if (!adminUserService.getUserByLogin(login).map(user -> user.getPassword().equals(password))
            .orElse(false)){
            redirectView.setUrl("login");
            redirectView.setStatusCode(HttpStatus.UNAUTHORIZED);
        }
        return redirectView;
    }

    @GetMapping("/user/list")
    public String getUserListPage(Model model) {
        List<User> users = adminUserService.getUserList();
        logger.info("Список пользователей: {}", users);
        model.addAttribute("users", users);
        return "users.html";
    }

    @PostMapping("/user/save")
    public RedirectView saveUser(@ModelAttribute User user) {
        logger.info("Сохранение пользователя: {}", user);
        adminUserService.createOrUpdate(user);
        return  new RedirectView("/user/list", true);
    }

    @GetMapping("/user/edit")
    public String getUserEditPage(Model model
            , @RequestParam(name = WEB_PAR_USER_ID) String id){
        logger.info("Edit user id = "+id);
        User user = adminUserService.load(Long.parseLong(id), User.class);
        model.addAttribute("user", user);
        logger.info("Редактирование пользователя: {}", user);
        return "userEdit.html";
    }

    @GetMapping("/user/create")
    public String getCreateUserPage(Model model){
        model.addAttribute("user", new User());
        return "userEdit.html";
    }

}
