package ru.otus.front.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.common.Serializers;
import ru.otus.entities.User;
import ru.otus.front.FrontendServiceMSClient;
import ru.otus.messagesystem.Message;
import ru.otus.messagesystem.RequestHandler;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GetUsersListDataResponseHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetAuthUserDataResponseHandler.class);

    private final FrontendServiceMSClient frontendService;

    public GetUsersListDataResponseHandler(FrontendServiceMSClient frontendService) {
        this.frontendService = frontendService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("new message:{}", msg);
        try {
            List<User> users = (List<User>)Serializers.deserializeObjectList(msg.getPayload());
            UUID sourceMessageId = msg.getSourceMessageId().orElseThrow(() -> new RuntimeException("Not found sourceMsg for message:" + msg.getId()));
            frontendService.takeConsumer(sourceMessageId, List.class).ifPresent(consumer -> consumer.accept(users));

        } catch (Exception ex) {
            logger.error("msg:" + msg, ex);
        }
        return Optional.empty();
    }
}
