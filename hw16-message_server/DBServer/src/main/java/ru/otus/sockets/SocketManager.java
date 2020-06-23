package ru.otus.sockets;

import ru.otus.clients.MsClient;
import ru.otus.messages.Message;

public interface SocketManager {

    void addMsClient(MsClient msClient);

    void addSocketClient(SocketClient socketClient);

    boolean newMessage(Message msg);

    void receiveMessage(Message msg);

    void start();

    void dispose();
}
