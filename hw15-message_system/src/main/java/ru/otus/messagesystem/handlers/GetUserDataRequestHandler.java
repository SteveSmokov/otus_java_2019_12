package ru.otus.messagesystem.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.db.services.AdminUserService;
import ru.otus.entities.User;
import ru.otus.messagesystem.common.Serializers;
import ru.otus.messagesystem.messages.Message;
import ru.otus.messagesystem.messages.MessageType;

import java.util.List;
import java.util.Optional;

public class GetUserDataRequestHandler implements RequestHandler {
    public static final Logger log = LoggerFactory.getLogger(GetUserDataResponseHandler.class);
    private final AdminUserService adminUserService;

    public GetUserDataRequestHandler(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        switch (msg.getType()) {
            case CREATE_USER_DATA:
            case CREATE_UPDATE_USER_DATA:
            case UPDATE_USER_DATA:
                User user = Serializers.deserialize(msg.getData(), User.class);
                if (msg.getType().equals(MessageType.CREATE_USER_DATA)) adminUserService.create(user);
                else if (msg.getType().equals(MessageType.CREATE_UPDATE_USER_DATA)) adminUserService.createOrUpdate(user);
                else adminUserService.update(user);
                return Optional.empty();
            case USER_DATA_ID:
                long userId = Serializers.deserialize(msg.getData(), Long.class);
                user = adminUserService.load(userId, User.class);
                return Optional.of(new Message(msg.getTo(),msg.getFrom(),msg.getId(),msg.getType(),Serializers.serialize(user)));
            case ALL_USERS_LIST:
                List<User> users = adminUserService.getUserList();
                return Optional.of(new Message(msg.getTo(),msg.getFrom(),msg.getId(),msg.getType(),Serializers.serialize(users)));
            case USER_DATA_LOGIN:
                String login = Serializers.deserialize(msg.getData(), String.class);
                Optional<User> authUser = adminUserService.getUserByLogin(login);
                if (authUser.isEmpty()) {
                    return Optional.empty();
                } else {
                    return Optional.of(new Message(msg.getTo(), msg.getFrom(), msg.getId(), msg.getType(), Serializers.serialize(authUser.get())));
                }
            default:  return Optional.empty();
        }
    }
}
