package ru.otus.messagesystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.messagesystem.clients.MSClient;
import ru.otus.messagesystem.messages.Message;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageSystemImpl implements MessageSystem {
    private static final Logger logger = LoggerFactory.getLogger(MessageSystemImpl.class);
    private static final int MESSAGE_QUEUE_SIZE = 100_000;
    private static final int MSG_HANDLER_THREAD_LIMIT = 2;

    private final AtomicBoolean runFlag = new AtomicBoolean(true);

    private final Map<String, MSClient> clientMap = new ConcurrentHashMap<>();
    private final BlockingQueue<Message> messageQueue = new ArrayBlockingQueue<>(MESSAGE_QUEUE_SIZE);

    private Runnable disposeCallback;

    private final ExecutorService msgProcessor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable);
        thread.setName("msg-processor-thread");
        return thread;
    });

    private final ExecutorService msgHandler = Executors.newFixedThreadPool(MSG_HANDLER_THREAD_LIMIT,
            new ThreadFactory() {

                private final AtomicInteger threadNameSeq = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable runnable) {
                    Thread thread = new Thread(runnable);
                    thread.setName("msg-handler-thread-" + threadNameSeq.incrementAndGet());
                    return thread;
                }
            });

    public MessageSystemImpl() {
        start();
    }

    public MessageSystemImpl(boolean startProcessing) {
        if (startProcessing){
            start();
        }
    }

    @Override
    public void addClient(MSClient msClient) {
        logger.info("Add new client {}", msClient);
        if (clientMap.containsKey(msClient.getName())){
            throw new IllegalArgumentException("Error. Client with name "+msClient.getName()+" already exists");
        }
        clientMap.put(msClient.getName(), msClient);
    }

    @Override
    public void removeClient(String clientId) {
        MSClient removedClient = clientMap.remove(clientId);
        if (removedClient == null) {
            logger.warn("Client not found: {}", clientId);
        } else {
            logger.info("Removed client: {}", removedClient);
        }
    }

    @Override
    public boolean newMessage(Message msg) {
        if (runFlag.get()){
            return messageQueue.offer(msg);
        } else {
            logger.warn("MS is being shutting down... rejected: {}", msg);
            return false;
        }
    }

    @Override
    public void dispose() throws InterruptedException {
        logger.info("Now in the messageQueue {} messages", currentQueueSize());
        runFlag.set(false);
        insertStopMessage();
        msgProcessor.shutdown();
        msgHandler.awaitTermination(60, TimeUnit.SECONDS);
    }

    @Override
    public void dispose(Runnable callback) throws InterruptedException {
        disposeCallback = callback;
        dispose();
    }

    @Override
    public void start() {
        logger.info("Message system start");
        msgProcessor.submit(this::processMessages);
    }

    @Override
    public int currentQueueSize() {
        return this.messageQueue.size();
    }

    private void processMessages(){
        logger.info("Process messages, {}", currentQueueSize());
        while (runFlag.get() || messageQueue.isEmpty()) {
            try {
                Message message = messageQueue.take();

            } catch (InterruptedException ex) {
                logger.error(ex.getMessage(), ex);
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                Thread.currentThread().interrupt();
            }
        }

        if (disposeCallback != null){
            msgHandler.submit(disposeCallback);
        }
        msgHandler.submit(this::messageHandlerShutdown);
    }

    private void messageHandlerShutdown() {
        msgHandler.shutdown();
        logger.info("msgHandler has been shut down");
    }

    private void insertStopMessage() throws InterruptedException {
        boolean result = messageQueue.offer(Message.VOID_TECHNICAL_MESSAGE);
        while (!result) {
            Thread.sleep(100);
            result = messageQueue.offer(Message.VOID_TECHNICAL_MESSAGE);
        }
    }
}
