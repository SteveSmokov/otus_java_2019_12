package ru.otus.configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.clients.MsClient;
import ru.otus.clients.MsClientImpl;
import ru.otus.handlers.GetAuthUserDataResponseHandler;
import ru.otus.handlers.GetUserDataResponseHandler;
import ru.otus.handlers.GetUsersListDataResponseHandler;
import ru.otus.handlers.SaveOrUpdateUserDataResponseHandler;
import ru.otus.messages.MessageType;
import ru.otus.service.FrontendServiceMSClient;
import ru.otus.service.FrontendServiceMSClientImpl;
import ru.otus.sockets.SocketClient;
import ru.otus.sockets.SocketClientImpl;
import ru.otus.sockets.SocketManager;
import ru.otus.sockets.SocketManagerImpl;

@Configuration
public class FrontConfig {
    private static final Logger logger = LoggerFactory.getLogger(FrontConfig.class);

    private String frontendServiceName = "FRONTEND_1";

    private String dbServiceName = "DBServer_1";

    private String hostMS = "localhost";

    private int portMS = 5050;

    @Bean
    public SocketManager socketManager() {
        SocketManager socketManager = new SocketManagerImpl();
        socketManager.start();
        return socketManager;
    }

    @Bean
    public SocketClient socketClient() {
        SocketClient socketClient = new SocketClientImpl(dbServiceName, frontendServiceName, hostMS, portMS);
        socketClient.start();
        return socketClient;
    }

    @Bean
    public FrontendServiceMSClient frontEndAsynchronousService(SocketManager socketManager, SocketClient socketClient) {
        logger.debug("create frontEndAsynchronousService with frontEndAsyncName:{} backEndDBServiceName:{}",frontendServiceName,dbServiceName);
        MsClient frontendMsClient = new MsClientImpl(frontendServiceName, socketManager);
        FrontendServiceMSClient frontService = new FrontendServiceMSClientImpl(frontendMsClient, dbServiceName);
        frontendMsClient.addHandler(MessageType.AUTH_USER_DATA, new GetAuthUserDataResponseHandler(frontService));
        frontendMsClient.addHandler(MessageType.GET_USER_DATA, new GetUserDataResponseHandler(frontService));
        frontendMsClient.addHandler(MessageType.GET_USERS_LIST, new GetUsersListDataResponseHandler(frontService));
        frontendMsClient.addHandler(MessageType.SAVE_USER_DATA, new SaveOrUpdateUserDataResponseHandler(frontService));
        socketManager.addMsClient(frontendMsClient);
        socketManager.addSocketClient(socketClient);
        return frontService;
    }
}
