package common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import ru.otus.front.components.AdminUser;
import ru.otus.front.handlers.GetAuthUserDataResponseHandler;
import ru.otus.front.handlers.GetUserDataResponseHandler;
import ru.otus.front.handlers.GetUsersListDataResponseHandler;
import ru.otus.front.handlers.SaveOrUpdateUserDataResponseHandler;
import ru.otus.messagesystem.*;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class IntegrationTest {
    private static final Logger logger = LoggerFactory.getLogger(IntegrationTest.class);

    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    private MessageSystem messageSystem;
    private FrontendServiceMSClient frontendService;
    private MsClient databaseMsClient;
    private MsClient frontendMsClient;

    @DisplayName("Базовый сценарий авторизации")
    @RepeatedTest(100)
    public void getDataByLogin() throws InterruptedException {
        createMessageSystem(true);
        CountDownLatch waitLatch = new CountDownLatch(1);

       frontendService.getUserByLogin("admin", data -> {
                    assertThat(data.getLogin()).isEqualTo("admin");
                    waitLatch.countDown();
                });

        waitLatch.await();
        messageSystem.dispose();
        logger.info("done");
    }

    @DisplayName("Базовый сценарий заппроса пользователя")
    @RepeatedTest(100)
    public void getDataByID() throws InterruptedException {
        createMessageSystem(true);
        CountDownLatch waitLatch = new CountDownLatch(1);

        frontendService.load(1, data -> {
            assertThat(data.getLogin()).isEqualTo("admin");
            waitLatch.countDown();
        });

        waitLatch.await();
        messageSystem.dispose();
        logger.info("done");
    }

    @DisplayName("Базовый сценарий заппроса списка пользователей")
    @RepeatedTest(100)
    public void getUsersList() throws InterruptedException {
        createMessageSystem(true);
        CountDownLatch waitLatch = new CountDownLatch(1);

        frontendService.getUserList(data -> {
            logger.info("Users list {}",data);
            assertThat(data.isEmpty()).isFalse();
            waitLatch.countDown();
        });

        waitLatch.await();
        messageSystem.dispose();
        logger.info("done");
    }

    @DisplayName("Базовый сценарий добавления пользователя")
    @RepeatedTest(100)
    public void addUser() throws InterruptedException {
        createMessageSystem(true);
        CountDownLatch waitLatch = new CountDownLatch(1);
        User user = new User("User1", "user1", "User1");
        frontendService.createOrUpdate(user, data -> {
            logger.info("Saved user {}",data);
            assertThat(data.getId()).isNotEqualTo(0);
            waitLatch.countDown();
        });

        waitLatch.await();
        messageSystem.dispose();
        logger.info("done");
    }

    @DisplayName("Выполнение запроса после остановки сервиса")
    @RepeatedTest(2)
    public void getDataAfterShutdown() throws Exception {
        createMessageSystem(true);
        messageSystem.dispose();

        CountDownLatch waitLatchShutdown = new CountDownLatch(1);

        when(frontendMsClient.sendMessage(any(Message.class))).
                thenAnswer(invocation -> {
                    waitLatchShutdown.countDown();
                    return null;
                });

        frontendService.getUserByLogin("admin", data -> logger.info("data:{}", data));
        waitLatchShutdown.await();
        boolean result = verify(frontendMsClient).sendMessage(any(Message.class));
        assertThat(result).isFalse();

        logger.info("done");
    }

    @DisplayName("Тестируем остановку работы MessageSystem")
    @RepeatedTest(100)
    public void stopMessageSystem() throws Exception {
        createMessageSystem(false);
        int counter = 100;
        CountDownLatch messagesSentLatch = new CountDownLatch(counter);
        CountDownLatch messageSystemDisposed = new CountDownLatch(1);

        IntStream.range(0, counter).forEach(id -> {
                frontendService.getUserByLogin("admin", data -> {
                        logger.info("User {}", data);
                    });
                    messagesSentLatch.countDown();
                }
        );
        messagesSentLatch.await();
        assertThat(messageSystem.currentQueueSize()).isEqualTo(counter);

        messageSystem.start();
        disposeMessageSystem(messageSystemDisposed::countDown);

        messageSystemDisposed.await();
        assertThat(messageSystem.currentQueueSize()).isEqualTo(0);

        logger.info("done");
    }


    private void createMessageSystem(boolean startProcessing) {
        logger.info("setup");
        messageSystem = new MessageSystemImpl(startProcessing);

        databaseMsClient = new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystem);
        HibernateUtils hibernateUtils = new HibernateUtils("ru.otus.entities");
        SessionManager sessionManager = new SessionManagerHibernate(hibernateUtils.buildSessionFactory());
        AdminUserDao userDao = new AdminUserDaoImpl(sessionManager);
        HwCache<Long, User> userCache = new MyCache<>(10);
        AdminUserService dbService = new AdminUserServiceImp(userDao, userCache);
        AdminUser adminUser = new AdminUser(dbService);
        adminUser.onApplicationEvent(null);

        databaseMsClient.addHandler(MessageType.AUTH_USER_DATA, new GetAuthUserDataRequestHandler(dbService));
        databaseMsClient.addHandler(MessageType.GET_USER_DATA, new GetUserDataRequestHandler(dbService));
        databaseMsClient.addHandler(MessageType.GET_USERS_LIST, new GetUsersListDataRequestHandler(dbService));
        databaseMsClient.addHandler(MessageType.SAVE_USER_DATA, new SaveOrUpdateUserDataRequestHandler(dbService));
        messageSystem.addClient(databaseMsClient);

        frontendMsClient = spy(new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystem));
        frontendService = new FrontendServiceMSClientImpl(frontendMsClient, DATABASE_SERVICE_CLIENT_NAME);
        frontendMsClient.addHandler(MessageType.AUTH_USER_DATA, new GetAuthUserDataResponseHandler(frontendService));
        frontendMsClient.addHandler(MessageType.GET_USER_DATA, new GetUserDataResponseHandler(frontendService));
        frontendMsClient.addHandler(MessageType.GET_USERS_LIST, new GetUsersListDataResponseHandler(frontendService));
        frontendMsClient.addHandler(MessageType.SAVE_USER_DATA, new SaveOrUpdateUserDataResponseHandler(frontendService));
        messageSystem.addClient(frontendMsClient);

        logger.info("setup done");
    }

    private void disposeMessageSystem(Runnable callback) {
        try {
            messageSystem.dispose(callback);
        } catch (InterruptedException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
