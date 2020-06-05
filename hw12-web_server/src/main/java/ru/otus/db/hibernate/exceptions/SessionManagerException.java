package ru.otus.db.hibernate.exceptions;

public class SessionManagerException extends RuntimeException {
    public SessionManagerException(String message) {
        super(message);
    }

    public SessionManagerException(Throwable cause) {
        super(cause);
    }
}
