package ru.otus.clients;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.messages.Message;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class FrontClient implements MsClient{
    private static Logger logger = LoggerFactory.getLogger(FrontClient.class);

    private Map<String,Socket> socketClients;

    @Override
    public void sendMessage(Message msg) throws InterruptedException {
        Socket socketClient = socketClients.get(msg.getTo());
        if (socketClient != null) {
            try {
                if (socketClient.isConnected()) {
                    PrintWriter out = new PrintWriter(socketClient.getOutputStream(), true);
                    String json = new Gson().toJson(msg);
                    logger.info("sending to frontendService {}", json);
                    out.println(json);
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        } else logger.error("Socket client not registered");
    }
    @Override
    public void setSocketClients(Map<String, Socket> socketClients) {
        this.socketClients = socketClients;
    }
}
