package ru.otus.messagesystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.clients.MsClient;
import ru.otus.messages.Message;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public final class MessageSystemImpl implements MessageSystem {
    private static final Logger logger = LoggerFactory.getLogger(MessageSystemImpl.class);
    private MsClient frontClient;

    private MsClient dbClient;

    private final ArrayBlockingQueue<Message> forFrontend = new ArrayBlockingQueue<>(10);
    private final ArrayBlockingQueue<Message> forDatabase = new ArrayBlockingQueue<>(10);

    private final ExecutorService executorFrontend =  Executors.newScheduledThreadPool(2);
    private final ExecutorService executorDatabase =  Executors.newScheduledThreadPool(2);

    @Override
    public void init() {
        executorDatabase.execute(() -> this.processMsgOutbox(forDatabase, dbClient));
        executorFrontend.execute(() -> this.processMsgOutbox(forFrontend, frontClient));
        executorFrontend.shutdown();
        executorDatabase.shutdown();
    }

    private void processMsgOutbox(ArrayBlockingQueue<Message> queue, MsClient client) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Message msg = queue.take();
                logger.info("processMsgOutbox take message:{}", msg);
                client.sendMessage(msg);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void addFrontMessage(Message msg) throws InterruptedException {
        forFrontend.put(msg);
    }

    @Override
    public void addDBMessage(Message msg) throws InterruptedException {
        forDatabase.put(msg);
    }

    @Override
    public void setFrontClient(MsClient frontClient) {
        this.frontClient = frontClient;
    }

    @Override
    public void setDBClient(MsClient dbClient) {
        this.dbClient = dbClient;
    }
}
