package ru.otus.configurations;

import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;
import ru.otus.clients.MsClient;
import ru.otus.clients.MsClientImpl;
import ru.otus.entities.User;
import ru.otus.handlers.GetAuthUserDataRequestHandler;
import ru.otus.handlers.GetUserDataRequestHandler;
import ru.otus.handlers.GetUsersListDataRequestHandler;
import ru.otus.handlers.SaveOrUpdateUserDataRequestHandler;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.sessionmanager.SessionManager;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.messages.MessageType;
import ru.otus.services.AdminUserDao;
import ru.otus.services.AdminUserDaoImpl;
import ru.otus.services.AdminUserService;
import ru.otus.services.AdminUserServiceImp;
import ru.otus.sockets.SocketClient;
import ru.otus.sockets.SocketClientImpl;
import ru.otus.sockets.SocketManager;
import ru.otus.sockets.SocketManagerImpl;

@Configuration
public class UserConfig {
    @Bean
    public AdminUserDao getAdminUserDao(){
        HibernateUtils hibernateUtils = new HibernateUtils("ru.otus.entities");
        SessionManager sessionManager = new SessionManagerHibernate(hibernateUtils.buildSessionFactory());
        AdminUserDao userDao = new AdminUserDaoImpl(sessionManager);
        return userDao;
    }

    @Bean
    public HwCache<Long, User> getCacheByUser(){
        HwCache<Long, User> userHwCache = new MyCache<>(10);
        return userHwCache;
    }

    @Bean
    public AdminUserService getAdminUserService(AdminUserDao adminDao, HwCache<Long, User> cache){
        AdminUserService adminUserService = new AdminUserServiceImp(adminDao, cache);
        return adminUserService;
    }

    @Bean
    public SocketManager socketManager() {
        SocketManager socketManager = new SocketManagerImpl();
        socketManager.start();
        return socketManager;
    }

    @Bean
    public SocketClient socketClient(ApplicationArguments arguments) {
        SocketClient socketClient = new SocketClientImpl(arguments.getSourceArgs());
        socketClient.start();
        return socketClient;
    }

    @Bean
    public MsClient msDataBaseClientImpl(SocketManager socketManager, SocketClient socketClient, AdminUserService adminUserService) {
        MsClient databaseMsClient = new MsClientImpl(socketClient.getServiceName(), socketManager);
        databaseMsClient.addHandler(MessageType.AUTH_USER_DATA, new GetAuthUserDataRequestHandler(adminUserService));
        databaseMsClient.addHandler(MessageType.GET_USER_DATA, new GetUserDataRequestHandler(adminUserService));
        databaseMsClient.addHandler(MessageType.GET_USERS_LIST, new GetUsersListDataRequestHandler(adminUserService));
        databaseMsClient.addHandler(MessageType.SAVE_USER_DATA, new SaveOrUpdateUserDataRequestHandler(adminUserService));
        socketManager.addMsClient(databaseMsClient);
        socketManager.addSocketClient(socketClient);
        return databaseMsClient;
    }
}
