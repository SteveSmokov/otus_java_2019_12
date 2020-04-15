package ru.otus.exceptions;

public class NotBillException extends ATMException {
    public NotBillException(String message) {
        super(message);
    }

    public NotBillException(String message, Throwable cause) {
        super(message, cause);
    }
}
