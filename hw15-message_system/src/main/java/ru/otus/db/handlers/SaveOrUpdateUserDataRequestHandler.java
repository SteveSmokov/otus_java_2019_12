package ru.otus.db.handlers;

import ru.otus.common.Serializers;
import ru.otus.db.services.AdminUserService;
import ru.otus.entities.User;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.MessageType;
import ru.otus.messagesystem.RequestHandler;

import java.util.Optional;

public class SaveOrUpdateUserDataRequestHandler implements RequestHandler {
    private final AdminUserService userDBService;

    public SaveOrUpdateUserDataRequestHandler(AdminUserService dbService) {
        this.userDBService = dbService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        User user = Serializers.deserialize(msg.getPayload(), User.class);
        Optional<User> authUser = userDBService.createOrUpdate(user);
        return Optional.of(new Message(msg.getTo(), msg.getFrom(), msg.getId(), MessageType.SAVE_USER_DATA.getValue(), Serializers.serialize(authUser.get())));
    }
}
