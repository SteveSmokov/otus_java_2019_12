package ru.otus.sockets;

import ru.otus.messages.Message;

public interface SocketClient {

    void start();

    void stop();

    void sendMessage(Message message);

    Message getMessageFromMS();

    String getServiceName();
}
