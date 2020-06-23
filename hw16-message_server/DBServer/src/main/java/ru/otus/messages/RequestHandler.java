package ru.otus.messages;


import java.util.Optional;

public interface RequestHandler {
    Optional<Message> handle(Message msg);
}
