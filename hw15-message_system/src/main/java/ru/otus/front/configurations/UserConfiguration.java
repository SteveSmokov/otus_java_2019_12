package ru.otus.front.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.db.cachehw.HwCache;
import ru.otus.db.cachehw.MyCache;
import ru.otus.db.handlers.GetAuthUserDataRequestHandler;
import ru.otus.db.handlers.GetUserDataRequestHandler;
import ru.otus.db.handlers.GetUsersListDataRequestHandler;
import ru.otus.db.handlers.SaveOrUpdateUserDataRequestHandler;
import ru.otus.db.hibernate.HibernateUtils;
import ru.otus.db.hibernate.sessionmanager.SessionManager;
import ru.otus.db.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.db.services.AdminUserDao;
import ru.otus.db.services.AdminUserDaoImpl;
import ru.otus.db.services.AdminUserService;
import ru.otus.db.services.AdminUserServiceImp;
import ru.otus.entities.User;
import ru.otus.front.FrontendServiceMSClient;
import ru.otus.front.FrontendServiceMSClientImpl;
import ru.otus.front.handlers.GetAuthUserDataResponseHandler;
import ru.otus.front.handlers.GetUserDataResponseHandler;
import ru.otus.front.handlers.GetUsersListDataResponseHandler;
import ru.otus.front.handlers.SaveOrUpdateUserDataResponseHandler;
import ru.otus.messagesystem.*;

@Configuration
public class UserConfiguration {
    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";
    private static final Boolean START_PROCESSING = true;

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
    public MessageSystem getMessageSystem(){
        MessageSystem messageSystem = new MessageSystemImpl(START_PROCESSING);
        return messageSystem;
    }

    @Bean
    public MsClient getDBClient(AdminUserService adminUserService, MessageSystem messageSystem){
        MsClient databaseMsClient = new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystem);
        databaseMsClient.addHandler(MessageType.AUTH_USER_DATA, new GetAuthUserDataRequestHandler(adminUserService));
        databaseMsClient.addHandler(MessageType.GET_USER_DATA, new GetUserDataRequestHandler(adminUserService));
        databaseMsClient.addHandler(MessageType.GET_USERS_LIST, new GetUsersListDataRequestHandler(adminUserService));
        databaseMsClient.addHandler(MessageType.SAVE_USER_DATA, new SaveOrUpdateUserDataRequestHandler(adminUserService));
        messageSystem.addClient(databaseMsClient);

        return databaseMsClient;
    }

    @Bean
    public FrontendServiceMSClient getFrontendService(MessageSystem messageSystem){
        MsClient frontendMsClient = new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystem);
        FrontendServiceMSClient frontendService = new FrontendServiceMSClientImpl(frontendMsClient, DATABASE_SERVICE_CLIENT_NAME);
        frontendMsClient.addHandler(MessageType.AUTH_USER_DATA, new GetAuthUserDataResponseHandler(frontendService));
        frontendMsClient.addHandler(MessageType.GET_USER_DATA, new GetUserDataResponseHandler(frontendService));
        frontendMsClient.addHandler(MessageType.GET_USERS_LIST, new GetUsersListDataResponseHandler(frontendService));
        frontendMsClient.addHandler(MessageType.SAVE_USER_DATA, new SaveOrUpdateUserDataResponseHandler(frontendService));
        messageSystem.addClient(frontendMsClient);

        return frontendService;
    }


}
