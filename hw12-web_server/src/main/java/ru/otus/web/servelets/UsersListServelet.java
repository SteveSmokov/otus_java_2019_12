package ru.otus.web.servelets;

import ru.otus.entities.User;
import ru.otus.services.AdminUserService;
import ru.otus.web.services.TemplateProcessor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersListServelet extends HttpServlet {
    private static final String PARAM_EDIT_USER_NAME = "edit_user";
    private static final String USERS_PAGE_TEMPLATE = "users.html";
    private static final String TEMPLATE_ATTR_USER_LIST = "users";
    private static final int MAX_INACTIVE_INTERVAL = 30;
    private final String REDIRECT_PATH = "/edit";
    private final AdminUserService adminUserService;
    private final TemplateProcessor templateProcessor;

    public UsersListServelet(TemplateProcessor templateProcessor, AdminUserService adminUserService) {
        this.templateProcessor = templateProcessor;
        this.adminUserService = adminUserService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        List<User> usersList = adminUserService.getUserList();
        if (!usersList.isEmpty()) {
            paramsMap.put(TEMPLATE_ATTR_USER_LIST, usersList);
        }

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, paramsMap));

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String edit_value = req.getParameter(PARAM_EDIT_USER_NAME);
        if (edit_value != null) {
            HttpSession session = req.getSession();
            session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
            resp.sendRedirect(REDIRECT_PATH);
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
