package ru.otus.handlers;

import ru.otus.common.Serializers;
import ru.otus.messages.RequestHandler;
import ru.otus.services.AdminUserService;
import ru.otus.entities.User;
import ru.otus.messages.Message;
import ru.otus.messages.MessageType;

import java.util.Optional;

public class GetUserDataRequestHandler implements RequestHandler {
    private final AdminUserService userDBService;

    public GetUserDataRequestHandler(AdminUserService dbService) {
        this.userDBService = dbService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        long userID = Serializers.deserialize(msg.getPayload(), Long.class);
        User user = userDBService.load(userID, User.class);
        return Optional.of(new Message(msg.getTo(), msg.getFrom(), msg.getId(), MessageType.GET_USER_DATA.getValue(), Serializers.serialize(user)));
    }
}
