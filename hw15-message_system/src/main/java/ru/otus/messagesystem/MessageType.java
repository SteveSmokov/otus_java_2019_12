package ru.otus.messagesystem;

public enum MessageType {
    GET_USER_DATA("GetUserData"),
    AUTH_USER_DATA("AuthUserData"),
    GET_USERS_LIST("GetUsersList"),
    SAVE_USER_DATA("SaveUserData");

    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
