package ru.otus.web.servelets;

import ru.otus.web.services.UserAuthService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";
    private static final int MAX_INACTIVE_INTERVAL = 30;
    private final String REDIRECT_PATH = "/users";

    private final UserAuthService userAuthService;

    public LoginServlet(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter(PARAM_LOGIN);
        String password = req.getParameter(PARAM_PASSWORD);
        if (this.userAuthService.authUser(login,password)){
            HttpSession session = req.getSession();
            session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
            resp.sendRedirect(REDIRECT_PATH);
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
