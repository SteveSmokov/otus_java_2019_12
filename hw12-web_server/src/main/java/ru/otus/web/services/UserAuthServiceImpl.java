package ru.otus.web.services;

import ru.otus.db.services.AdminUserService;

public class UserAuthServiceImpl implements UserAuthService {
    private final AdminUserService adminUserService;

    public UserAuthServiceImpl(AdminUserService userService) {
        this.adminUserService = userService;
    }

    @Override
    public boolean authUser(String login, String password) {
       return this.adminUserService.getUserByLogin(login)
            .map(user -> user.getPassword().equals(password))
            .orElse(false);

    }
}
