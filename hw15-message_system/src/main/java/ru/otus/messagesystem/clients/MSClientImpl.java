package ru.otus.messagesystem.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.common.Serializers;
import ru.otus.messagesystem.handlers.RequestHandler;
import ru.otus.messagesystem.messages.Message;
import ru.otus.messagesystem.messages.MessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MSClientImpl implements MSClient {
    private static final Logger logger = LoggerFactory.getLogger(MSClientImpl.class);

    private final String name;
    private final MessageSystem messageSystem;
    private final Map<String, RequestHandler> handlers = new ConcurrentHashMap<>();

    public MSClientImpl(String name, MessageSystem messageSystem) {
        this.name = name;
        this.messageSystem = messageSystem;
    }

    @Override
    public void addHandler(MessageType type, RequestHandler requestHandler) {
        this.handlers.put(type.getValue(), requestHandler);
    }

    @Override
    public boolean sendMessage(Message msg) {
        boolean result = messageSystem.newMessage(msg);
        if (!result){
            logger.error("The last message was rejected: {}", msg);
        }
        return result;
    }

    @Override
    public void handle(Message msg) {
        logger.info("Handle message {}", msg);
        try {
            RequestHandler requestHandler = handlers.get(msg.getType());
            if (requestHandler != null){
                requestHandler.handle(msg).ifPresent(this::sendMessage);
            } else {
                logger.error("Handle not found for the message type: {}.", msg.getType());
            }
        } catch (Exception ex) {
            logger.error("Message " + msg.toString(), ex);
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public <T> Message produceMessage(String to, T data, MessageType msgType) {
        return new Message(name, to, null, msgType, Serializers.serialize(data));
    }
}
