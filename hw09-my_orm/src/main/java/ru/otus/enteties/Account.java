package ru.otus.enteties;

import ru.otus.annotations.Column;
import ru.otus.annotations.Id;

public class Account {
    @Id
    @Column(FieldType = DBType.BIGINT, MaxLength = 20)
    private long no;
    @Column(FieldType = DBType.VARCHAR, MaxLength = 255)
    private String type;
    @Column(FieldType = DBType.NUMBER)
    private long rest;

    public Account() {
    }

    public Account(String type, long rest) {
        this.type = type;
        this.rest = rest;
    }

    public long getNo() {
        return no;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    @Override
    public String toString() {
        return "Account{" +
                "no=" + no +
                ", type='" + type + '\'' +
                ", rest=" + rest +
                '}';
    }
}
