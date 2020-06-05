package ru.otus.messagesystem.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.entities.User;
import ru.otus.messagesystem.clients.MSClient;
import ru.otus.messagesystem.messages.Message;
import ru.otus.messagesystem.messages.MessageType;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class FrontendServiceMSClientImpl implements FrontendServiceMSClient {
    private static final Logger logger = LoggerFactory.getLogger(FrontendServiceMSClientImpl.class);

    private final Map<UUID, Consumer<?>> consumerMap = new ConcurrentHashMap<>();
    private final MSClient msClient;
    private final String databaseServiceClientName;

    public FrontendServiceMSClientImpl(MSClient msClient, String databaseServiceClientName) {
        this.msClient = msClient;
        this.databaseServiceClientName = databaseServiceClientName;
    }


    @Override
    public void create(User objectData, Consumer<String> dataConsumer) {
        Message outMessage = msClient.produceMessage(this.databaseServiceClientName, objectData, MessageType.CREATE_USER_DATA);
        consumerMap.put(outMessage.getId(), dataConsumer);
        msClient.sendMessage(outMessage);
    }

    @Override
    public void update(User objectData) {

    }

    @Override
    public void createOrUpdate(User objectData) {

    }

    @Override
    public void getUserByLogin(String login) {

    }

    @Override
    public void getUserList() {

    }

    @Override
    public void load(long id) {

    }

    @Override
    public <T> Optional<Consumer<T>> takeConsumer(UUID sourceMessageId, Class<T> tClass) {
        return Optional.empty();
    }
}
