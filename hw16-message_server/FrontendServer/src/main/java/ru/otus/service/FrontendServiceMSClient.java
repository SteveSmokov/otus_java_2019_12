package ru.otus.service;

import ru.otus.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface FrontendServiceMSClient {
    void getUserByLogin(String login, Consumer<User> dataConsumer);
    void getUserList(Consumer<List<User>> dataConsumer);
    void createOrUpdate(User objectData, Consumer<User> dataConsumer);
    void load(long id, Consumer<User> dataConsumer);
    <T> Optional<Consumer<T>> takeConsumer(UUID sourceMessageId, Class<T> tClass);
}
