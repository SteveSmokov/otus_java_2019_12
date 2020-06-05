package ru.otus.messagesystem.clients;

import ru.otus.messagesystem.handlers.RequestHandler;
import ru.otus.messagesystem.messages.Message;
import ru.otus.messagesystem.messages.MessageType;

public interface MSClient {
    void addHandler(MessageType type, RequestHandler requestHandler);

    boolean sendMessage(Message msg);

    void handle(Message msg);

    String getName();

    <T> Message produceMessage(String to, T data, MessageType msgType);
}
