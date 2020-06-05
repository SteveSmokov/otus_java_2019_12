package ru.otus.messagesystem.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.entities.User;
import ru.otus.messagesystem.common.Serializers;
import ru.otus.messagesystem.messages.Message;
import ru.otus.messagesystem.services.FrontendServiceMSClient;

import java.util.Optional;
import java.util.UUID;

public class GetUserDataResponseHandler implements RequestHandler {
    public static final Logger log = LoggerFactory.getLogger(GetUserDataResponseHandler.class);
    private final FrontendServiceMSClient frontendService;

    public GetUserDataResponseHandler(FrontendServiceMSClient frontendService) {
        this.frontendService = frontendService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        log.info("New message {}", msg);
        try {
            User userData = Serializers.deserialize(msg.getData(), User.class);
            UUID sourceMessageID = msg.getSourceMessageID()
                    .orElseThrow(() -> new RuntimeException("Not found sourcemessage for messge: " +msg.getId()));
            frontendService.takeConsumer(sourceMessageID, User.class).ifPresent(userConsumer -> userConsumer.accept(userData));
        } catch (Exception ex) {
            log.error("Msg: "+ex.getMessage(),ex);
        }
        return Optional.empty();
    }
}
