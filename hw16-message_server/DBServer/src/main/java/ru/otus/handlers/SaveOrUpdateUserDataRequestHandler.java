package ru.otus.handlers;

import ru.otus.common.Serializers;
import ru.otus.messages.RequestHandler;
import ru.otus.services.AdminUserService;
import ru.otus.entities.User;
import ru.otus.messages.Message;
import ru.otus.messages.MessageType;

import java.util.Optional;

public class SaveOrUpdateUserDataRequestHandler implements RequestHandler {
    private final AdminUserService userDBService;

    public SaveOrUpdateUserDataRequestHandler(AdminUserService dbService) {
        this.userDBService = dbService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        User user = Serializers.deserialize(msg.getPayload(), User.class);
        userDBService.createOrUpdate(user);
        return Optional.of(new Message(msg.getTo(), msg.getFrom(), msg.getId(), MessageType.SAVE_USER_DATA.getValue(), Serializers.serialize(user)));
    }
}
