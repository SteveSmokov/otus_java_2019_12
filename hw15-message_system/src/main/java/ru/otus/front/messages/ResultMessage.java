package ru.otus.front.messages;

import java.util.Objects;

public class ResultMessage {
    private String message;

    public ResultMessage(String message) {
        this.message = message;
    }

    public ResultMessage() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResultMessage)) return false;
        ResultMessage that = (ResultMessage) o;
        return Objects.equals(getMessage(), that.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMessage());
    }

    @Override
    public String toString() {
        return "ResultMessage{" +
                "message='" + message + '\'' +
                '}';
    }
}
