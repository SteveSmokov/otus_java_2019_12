package ru.otus.handlers;

import ru.otus.common.Serializers;
import ru.otus.messages.RequestHandler;
import ru.otus.services.AdminUserService;
import ru.otus.entities.User;
import ru.otus.messages.Message;
import ru.otus.messages.MessageType;

import java.util.List;
import java.util.Optional;

public class GetUsersListDataRequestHandler  implements RequestHandler {
    private final AdminUserService userDBService;

    public GetUsersListDataRequestHandler(AdminUserService dbService) {
        this.userDBService = dbService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        List<User> users = userDBService.getUserList();
        return Optional.of(new Message(msg.getTo(), msg.getFrom(), msg.getId(), MessageType.GET_USERS_LIST.getValue(), Serializers.serialize(users)));
    }
}
