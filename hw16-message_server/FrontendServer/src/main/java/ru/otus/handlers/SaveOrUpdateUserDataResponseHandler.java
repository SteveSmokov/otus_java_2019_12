package ru.otus.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.common.Serializers;
import ru.otus.entities.User;
import ru.otus.service.FrontendServiceMSClient;
import ru.otus.messages.Message;
import ru.otus.messages.RequestHandler;

import java.util.Optional;
import java.util.UUID;

public class SaveOrUpdateUserDataResponseHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetAuthUserDataResponseHandler.class);

    private final FrontendServiceMSClient frontendService;

    public SaveOrUpdateUserDataResponseHandler(FrontendServiceMSClient frontendService) {
        this.frontendService = frontendService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("new message:{}", msg);
        try {
            User userData = Serializers.deserialize(msg.getPayload(), User.class);
            UUID sourceMessageId = msg.getSourceMessageId().orElseThrow(() -> new RuntimeException("Not found sourceMsg for message:" + msg.getId()));
            frontendService.takeConsumer(sourceMessageId, User.class).ifPresent(consumer -> consumer.accept(userData));

        } catch (Exception ex) {
            logger.error("msg:" + msg, ex);
        }
        return Optional.empty();
    }
}
