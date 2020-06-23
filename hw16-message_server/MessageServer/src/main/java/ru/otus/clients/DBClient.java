package ru.otus.clients;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.messages.Message;
import ru.otus.messagesystem.MessageSystem;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class DBClient implements MsClient {
    private static Logger logger = LoggerFactory.getLogger(DBClient.class);

    private Map<String,Socket> socketClients;

    public void setSocketClients(Map<String, Socket> socketClients) {
        this.socketClients = socketClients;
    }

    @Override
    public void sendMessage(Message message) {
        Socket socketClient = socketClients.get(message.getTo());
        if (socketClient != null) {
            try {
                if (socketClient.isConnected()) {
                    PrintWriter out = new PrintWriter(socketClient.getOutputStream(), true);

                    String json = new Gson().toJson(message);
                    logger.info("sending to dbService {}", json);
                    out.println(json);
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        } else logger.error("Socket client not registered");
    }
}
