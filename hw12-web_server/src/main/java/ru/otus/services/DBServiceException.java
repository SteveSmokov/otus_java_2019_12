package ru.otus.services;

public class DBServiceException extends RuntimeException {
    public DBServiceException(String message) {
        super(message);
    }

    public DBServiceException(Throwable cause) {
        super(cause);
    }
}
