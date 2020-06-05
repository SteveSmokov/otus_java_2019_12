package ru.otus.messagesystem.messages;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Message {
    public static final Message VOID_TECHNICAL_MESSAGE = new Message();

    private final UUID id = UUID.randomUUID();
    private final String from;
    private final String to;
    private final UUID sourceMessageID;
    private final MessageType type;
    private final int length;
    private final byte[] data;

    public Message() {
        this.from = null;
        this.to = null;
        this.sourceMessageID = null;
        this.type = MessageType.TECHNICAL_MESSAGE;
        this.length = 0;
        this.data = null;
    }

    public Message(String from, String to, UUID sourceMessageID, MessageType type, byte[] data) {
        this.from = from;
        this.to = to;
        this.sourceMessageID = sourceMessageID;
        this.type = type;
        this.length = data.length;
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return length == message.length &&
                Objects.equals(id, message.id) &&
                Objects.equals(from, message.from) &&
                Objects.equals(to, message.to) &&
                Objects.equals(sourceMessageID, message.sourceMessageID) &&
                Objects.equals(type, message.type) &&
                Arrays.equals(data, message.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, from, to, sourceMessageID, type, length);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", sourceMessageID=" + sourceMessageID +
                ", type='" + type + '\'' +
                ", length=" + length +
                ", data=" + Arrays.toString(data) +
                '}';
    }

    public UUID getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Optional<UUID> getSourceMessageID() {
        return Optional.ofNullable(sourceMessageID);
    }

    public MessageType getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

    public byte[] getData() {
        return data;
    }


}
