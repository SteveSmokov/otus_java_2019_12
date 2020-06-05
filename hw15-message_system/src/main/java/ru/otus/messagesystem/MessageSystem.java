package ru.otus.messagesystem;

import ru.otus.messagesystem.clients.MSClient;
import ru.otus.messagesystem.messages.Message;

public interface MessageSystem {
    void addClient(MSClient msClient);

    void removeClient(String clientId);

    boolean newMessage(Message msg);

    void dispose() throws InterruptedException;

    void dispose(Runnable callback) throws InterruptedException;

    void start();

    int currentQueueSize();
}
