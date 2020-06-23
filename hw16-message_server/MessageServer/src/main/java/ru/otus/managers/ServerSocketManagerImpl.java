package ru.otus.managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.clients.DBClient;
import ru.otus.clients.FrontClient;
import ru.otus.clients.MsClient;
import ru.otus.messages.Message;
import ru.otus.messagesystem.MessageSystem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ServerSocketManagerImpl implements ServerSocketManager {
    private final static Logger logger = LoggerFactory.getLogger(ServerSocketManagerImpl.class);
    private final int socketPort;
    private final MessageSystem messageSystem;

    private final ExecutorService executorServer = Executors.newScheduledThreadPool(5);
    private boolean running = false;
    private final Object monitor = new Object();

    private final Map<String, Socket> socketClientDBService = new ConcurrentHashMap<>();
    private final Map<String,Socket> socketClientFrontendService = new ConcurrentHashMap<>();

    public ServerSocketManagerImpl(int socketPort, MessageSystem messageSystem) {
        this.socketPort = socketPort;
        this.messageSystem = messageSystem;
    }

    @Override
    public void init() {
        MsClient dbClient = new DBClient();
        dbClient.setSocketClients(socketClientDBService);
        messageSystem.setDBClient(dbClient);
        MsClient fClient = new FrontClient();
        fClient.setSocketClients(socketClientFrontendService);
        messageSystem.setFrontClient(fClient);
        messageSystem.init();
        executorServer.execute(this::run);
    }

    private void run() {
        logger.info("ServerSocket port: {}",socketPort);
        try (ServerSocket serverSocket = new ServerSocket(socketPort)) {
            while (running) {
                Socket clientSocket = serverSocket.accept();
                executorServer.execute(() -> clientHandler(clientSocket));
            }
        } catch (Exception ex) {
            logger.error("error", ex);
        }
    }

    private void clientHandler(Socket clientSocket) {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                logger.debug("input message: {} ", inputLine);

                if (!socketClientDBService.containsValue(clientSocket) && !socketClientFrontendService.containsValue(clientSocket)) {
                    synchronized (monitor) {
                        Type listType = new TypeToken<ArrayList<String>>() {
                        }.getType();
                        List<String> regParams = new Gson().fromJson(inputLine, listType);
                        for (String key : regParams) {
                            if (regParams.size()==1)
                                socketClientDBService.put(key, clientSocket);
                            else
                                socketClientFrontendService.put(key, clientSocket);
                        }
                        String responseMessage = String.format("client registered with param: %s",regParams.toString());
                        logger.debug(responseMessage);
                        out.println(true);
                    }
                } else {
                    Message message = new Gson().fromJson(inputLine, Message.class);
                    logger.info("messageTransport: {}", message);

                    if (socketClientDBService.containsKey(message.getTo())) {
                        messageSystem.addDBMessage(message);
                    }
                    if (socketClientFrontendService.containsKey(message.getTo())) {
                        messageSystem.addFrontMessage(message);
                    }
                }
            }
        } catch(Exception ex){
            logger.error(ex.getMessage(), ex);
        }
    }

    @Override
    public void start() {
        running = true;
    }

    @Override
    public void stop() {
        running = false;
    }
}
