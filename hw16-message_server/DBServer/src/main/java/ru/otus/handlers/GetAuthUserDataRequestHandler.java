package ru.otus.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.messages.RequestHandler;
import ru.otus.services.AdminUserService;
import ru.otus.entities.User;
import ru.otus.messages.Message;
import ru.otus.messages.MessageType;
import ru.otus.common.Serializers;

import java.util.Optional;


public class GetAuthUserDataRequestHandler implements RequestHandler {
    private final Logger logger = LoggerFactory.getLogger(GetAuthUserDataRequestHandler.class);
    private final AdminUserService userDBService;

    public GetAuthUserDataRequestHandler(AdminUserService dbService) {
        this.userDBService = dbService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("Handle message {}", msg);
        String login = Serializers.deserialize(msg.getPayload(), String.class);
        Optional<User> authUser = userDBService.getUserByLogin(login);
        logger.info("Auth user {}", authUser.get());
        return Optional.of(new Message(msg.getTo(), msg.getFrom(), msg.getId(), MessageType.AUTH_USER_DATA.getValue(), Serializers.serialize(authUser.get())));
    }
}
