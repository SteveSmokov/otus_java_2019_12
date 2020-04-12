package ru.otus.enteties;

public enum DBType {
    BIGINT("bigint"),
    VARCHAR("varchar"),
    NUMBER("number"),
    INT("int");
    private String type;

    DBType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
