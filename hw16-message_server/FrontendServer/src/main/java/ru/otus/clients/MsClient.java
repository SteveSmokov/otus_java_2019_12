package ru.otus.clients;

import ru.otus.messages.Message;
import ru.otus.messages.MessageType;
import ru.otus.messages.RequestHandler;

public interface MsClient {

    void addHandler(MessageType type, RequestHandler requestHandler);

    void sendMessage(Message msg);

    void handle(Message msg);

    String getName();

    <T> Message produceMessage(String to, T data, MessageType msgType);

}
