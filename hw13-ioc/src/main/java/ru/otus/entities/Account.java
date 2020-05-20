package ru.otus.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private long no;
    @Column(name = "type")
    private String type;
    @Column(name = "rest")
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

    public void setNo(long no) {
        this.no = no;
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

    public void setRest(long rest) {
        this.rest = rest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return getNo() == account.getNo() &&
                getRest() == account.getRest() &&
                getType().equals(account.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNo(), getType(), getRest());
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
