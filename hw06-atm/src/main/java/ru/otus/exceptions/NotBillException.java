package ru.otus.exceptions;

public class NotBillException extends Exception {
    public NotBillException(String message) {
        super(message);
    }

    public NotBillException(String message, Throwable cause) {
        super(message, cause);
    }
}
