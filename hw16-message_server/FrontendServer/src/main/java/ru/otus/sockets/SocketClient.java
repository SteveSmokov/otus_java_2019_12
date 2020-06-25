package ru.otus.sockets;

import ru.otus.messages.Message;

public interface SocketClient {
    String getFrontendServiceName();
    String getHost();
    int getPort();
    String getDbServiceName();

    void start();

    void stop();

    void sendMessage(Message message);

    Message receiveMessage();
}
