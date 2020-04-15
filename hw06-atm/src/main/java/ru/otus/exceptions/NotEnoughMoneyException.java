package ru.otus.exceptions;

public class NotEnoughMoneyException extends ATMException {
    public NotEnoughMoneyException(String message) {
        super(message);
    }

    public NotEnoughMoneyException(String message, Throwable cause) {
        super(message, cause);
    }
}
