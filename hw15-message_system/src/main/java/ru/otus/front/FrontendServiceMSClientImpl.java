package ru.otus.front;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.entities.User;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.MsClient;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class FrontendServiceMSClientImpl implements FrontendServiceMSClient {
    private static final Logger logger = LoggerFactory.getLogger(FrontendServiceMSClientImpl.class);

    private final Map<UUID, Consumer<?>> consumerMap = new ConcurrentHashMap<>();
    private final MsClient msClient;
    private final String databaseServiceClientName;

    public FrontendServiceMSClientImpl(MsClient msClient, String databaseServiceClientName) {
        this.msClient = msClient;
        this.databaseServiceClientName = databaseServiceClientName;
    }

    @Override
    public void getUserByLogin(String login, Consumer<User> dataConsumer) {
        Message outMsg = msClient.produceMessage(databaseServiceClientName, login, MessageType.AUTH_USER_DATA);
        consumerMap.put(outMsg.getId(), dataConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    public void load(long id, Consumer<User> dataConsumer) {
        Message outMsg = msClient.produceMessage(databaseServiceClientName, id, MessageType.GET_USER_DATA);
        consumerMap.put(outMsg.getId(), dataConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    public void getUserList(Consumer<List<User>> dataConsumer) {
        Message outMsg = msClient.produceMessage(databaseServiceClientName, null, MessageType.GET_USERS_LIST);
        consumerMap.put(outMsg.getId(), dataConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    public void createOrUpdate(User objectData, Consumer<User> dataConsumer) {
        Message outMsg = msClient.produceMessage(databaseServiceClientName, objectData, MessageType.SAVE_USER_DATA);
        consumerMap.put(outMsg.getId(), dataConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    public <T> Optional<Consumer<T>> takeConsumer(UUID sourceMessageId, Class<T> tClass) {
        Consumer<T> consumer = (Consumer<T>) consumerMap.remove(sourceMessageId);
        if (consumer == null) {
            logger.warn("consumer not found for:{}", sourceMessageId);
            return Optional.empty();
        }
        return Optional.of(consumer);
    }
}
