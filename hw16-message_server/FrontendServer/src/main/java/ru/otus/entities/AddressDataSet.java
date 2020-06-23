package ru.otus.entities;

public class AddressDataSet {
    private long id;
    private String street;

    public AddressDataSet(String street) {
        this.street = street;
    }

    public AddressDataSet() {
    }

    public long getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "AddressDataSet{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }
}
