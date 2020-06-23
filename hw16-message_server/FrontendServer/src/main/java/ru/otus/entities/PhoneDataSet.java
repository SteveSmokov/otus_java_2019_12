package ru.otus.entities;


import java.util.Objects;

public class PhoneDataSet {
    private long id;
    private String number;
   private User user;

    public PhoneDataSet() {
    }

    public PhoneDataSet(String number) {
        this.number = number;
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "PhoneDataSet{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneDataSet)) return false;
        PhoneDataSet that = (PhoneDataSet) o;
        return getId() == that.getId() &&
                getNumber().equals(that.getNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNumber());
    }
}
