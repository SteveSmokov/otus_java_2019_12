package ru.otus.clients;

import ru.otus.messages.Message;

import java.net.Socket;
import java.util.Map;

public interface MsClient {
    void setSocketClients(Map<String, Socket> socketClients);
    void sendMessage(Message msg) throws InterruptedException;
}
