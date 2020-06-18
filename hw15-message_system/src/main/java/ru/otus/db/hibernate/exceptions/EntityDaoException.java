package ru.otus.db.hibernate.exceptions;

public class EntityDaoException extends RuntimeException {
    public EntityDaoException(String message) {
        super(message);
    }

    public EntityDaoException(Throwable cause) {
        super(cause);
    }
}
