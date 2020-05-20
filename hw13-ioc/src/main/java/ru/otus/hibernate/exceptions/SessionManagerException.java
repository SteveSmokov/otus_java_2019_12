package ru.otus.hibernate.exceptions;

public class SessionManagerException extends RuntimeException {
    public SessionManagerException(String message) {
        super(message);
    }

    public SessionManagerException(Throwable cause) {
        super(cause);
    }
}
