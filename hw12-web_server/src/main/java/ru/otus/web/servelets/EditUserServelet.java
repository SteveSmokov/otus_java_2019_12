package ru.otus.web.servelets;

import ru.otus.entities.User;
import ru.otus.db.services.AdminUserService;
import ru.otus.web.services.TemplateProcessor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditUserServelet  extends HttpServlet {
    private static final String PARAM_SAVE_NAME = "save_button";
    private static final String PARAM_CANCEL_NAME = "close_button";
    private static final String PARAM_USER_NAME = "user_name";
    private static final String PARAM_LOGIN = "user_login";
    private static final String PARAM_PASSWORD = "user_password";
    private static final String USERS_PAGE_TEMPLATE = "userEdit.html";
    private static final int MAX_INACTIVE_INTERVAL = 30;
    private final String REDIRECT_PATH = "/users";
    private final AdminUserService adminUserService;
    private final TemplateProcessor templateProcessor;
    private User user;


    public EditUserServelet(TemplateProcessor templateProcessor, AdminUserService adminUserService) {
        this.templateProcessor = templateProcessor;
        this.adminUserService = adminUserService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();

        String userID = req.getParameter("Id");
        if (userID != null){
            this.user = (User) adminUserService.load(Long.parseLong(userID), User.class);
            if (this.user != null){
                paramsMap.put(PARAM_USER_NAME, this.user.getName());
                paramsMap.put(PARAM_LOGIN, this.user.getLogin());
                paramsMap.put(PARAM_PASSWORD, this.user.getPassword());
            } else {
                this.user = new User("user", "user", "user");
            }
        } else {
            this.user = new User("user", "user", "user");
        }
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String save_button_value = req.getParameter(PARAM_SAVE_NAME);
        String cancel_button_value = req.getParameter(PARAM_CANCEL_NAME);
        String user_name = req.getParameter(PARAM_USER_NAME);
        String user_login = req.getParameter(PARAM_LOGIN);
        String user_password = req.getParameter(PARAM_PASSWORD);
        this.user.setName(user_name);
        this.user.setLogin(user_login);
        this.user.setPassword(user_password);
        if (save_button_value != null){
            adminUserService.createOrUpdate(user);
            HttpSession session = req.getSession();
            session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
            resp.sendRedirect(REDIRECT_PATH);
        } else if (cancel_button_value != null){
            HttpSession session = req.getSession();
            session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
            resp.sendRedirect(REDIRECT_PATH);
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
