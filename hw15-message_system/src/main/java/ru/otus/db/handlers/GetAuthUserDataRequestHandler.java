package ru.otus.db.handlers;

import ru.otus.common.Serializers;
import ru.otus.db.services.AdminUserService;
import ru.otus.entities.User;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.RequestHandler;

import java.util.Optional;


public class GetAuthUserDataRequestHandler implements RequestHandler {
    private final AdminUserService userDBService;

    public GetAuthUserDataRequestHandler(AdminUserService dbService) {
        this.userDBService = dbService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        String login = Serializers.deserialize(msg.getPayload(), String.class);
        Optional<User> authUser = userDBService.getUserByLogin(login);
        return Optional.of(new Message(msg.getTo(), msg.getFrom(), msg.getId(), MessageType.AUTH_USER_DATA.getValue(), Serializers.serialize(authUser.get())));
    }
}
