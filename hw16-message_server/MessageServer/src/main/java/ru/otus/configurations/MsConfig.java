package ru.otus.configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.managers.ServerSocketManager;
import ru.otus.managers.ServerSocketManagerImpl;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MessageSystemImpl;
import ru.otus.runner.ProcessRunnerImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Configuration
//@PropertySource("config.yml")
public class MsConfig {
    private static final Logger logger = LoggerFactory.getLogger(MsConfig.class);

//    @Value("${SocketPort}")
//    private int socketPort;
//
//    @Value("${frontend1StartCommand}")
//    private String frontend1StartCommand;
//
//    @Value("${DBService1StartCommand}")
//    private String dbService1StartCommand;
//
//    @Value("${frontend2StartCommand}")
//    private String frontend2StartCommand;
//
//    @Value("${DBService2StartCommand}")
//    private String dbService2StartCommand;
//
//    @Value("${ClientsCount}")
//    private int clientsCount;
//
//    @Value("${StartDelaySec}")
//    private int startDelaySec;
    private int socketPort = 5050;

    private String frontend1StartCommand = "";

    private String dbService1StartCommand = "java -jar java -jar ./hw16-message_server/DBServer/target/DBServer-1.0-SNAPSHOT.jar --host localhost -port 5050 -servicename BACKEND_1";

    private String frontend2StartCommand = "";

    private String dbService2StartCommand = "java -jar java -jar ./hw16-message_server/DBServer/target/DBServer-1.0-SNAPSHOT.jar --host localhost -port 5050 -servicename BACKEND_2";

    private int clientsCount = 2;

    private int startDelaySec = 5;

    @Bean
    public MessageSystem messageSystem() {

        return new MessageSystemImpl();
    }

    @Bean
    public ServerSocketManager socketServer(MessageSystem messageSystem) {
        ServerSocketManager server = new ServerSocketManagerImpl(socketPort, messageSystem);
        server.start();
        return server;
    }

//    @Bean
//    public void runClients() {
//        logger.debug("frontend1StartCommand: {}", frontend1StartCommand);
//        logger.debug("frontend2StartCommand: {}", frontend2StartCommand);
//        logger.debug("dbService1StartCommand: {}", dbService1StartCommand);
//        logger.debug("dbService2StartCommand: {}", dbService2StartCommand);
//        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(clientsCount);
//        startClient(executorService, getCommands());
//        executorService.shutdown();
//    }

    private void startClient(ScheduledExecutorService executorService, List<String> commands) {
        for (String command : commands) {
            executorService.schedule(() -> {
                if (!command.isEmpty()) {
                    new ProcessRunnerImpl().start(command);
                    logger.debug("run command: {}",command);
                }
            }, startDelaySec, TimeUnit.SECONDS);
        }
    }

    private  List<String> getCommands() {
        List<String> commands = new ArrayList<>();
        commands.add(frontend1StartCommand);
        commands.add(frontend2StartCommand);
        commands.add(dbService1StartCommand);
        commands.add(dbService2StartCommand);
        return commands;
    }
}
