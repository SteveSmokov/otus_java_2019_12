package ru.otus.messagesystem.handlers;

import ru.otus.messagesystem.messages.Message;

import java.util.Optional;

public interface RequestHandler {
    Optional<Message> handle(Message msg);
}
