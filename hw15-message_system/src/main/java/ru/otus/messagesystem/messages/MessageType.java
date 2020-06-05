package ru.otus.messagesystem.messages;

public enum MessageType {
    USER_DATA_ID("user_data_id"),
    USER_DATA_LOGIN("user_data_login"),
    ALL_USERS_LIST("all_user_list"),
    CREATE_USER_DATA("create_user_data"),
    UPDATE_USER_DATA("update_user_data"),
    CREATE_UPDATE_USER_DATA("create_update_user_data"),
    TECHNICAL_MESSAGE("technical_message");

    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
