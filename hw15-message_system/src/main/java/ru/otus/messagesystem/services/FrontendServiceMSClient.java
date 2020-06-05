package ru.otus.messagesystem.services;

import ru.otus.entities.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface FrontendServiceMSClient {
    void getUserByLogin(String login);
    void getUserList();
    void create(User objectData, Consumer<String> dataConsumer);
    void update(User objectData);
    void createOrUpdate(User objectData);
    void load(long id);
    <T> Optional<Consumer<T>> takeConsumer(UUID sourceMessageId, Class<T> tClass);
}
