package ru.otus.web.services;

public interface UserAuthService {
    boolean authUser(String login, String password);
}
