package ru.otus.messagesystem;

import ru.otus.clients.MsClient;
import ru.otus.messages.Message;

public interface MessageSystem {
    void init();
    void addFrontMessage(Message msg) throws InterruptedException;
    void addDBMessage(Message msg) throws InterruptedException;
    void setFrontClient(MsClient frontClient);
    void setDBClient(MsClient dbClient);

}

