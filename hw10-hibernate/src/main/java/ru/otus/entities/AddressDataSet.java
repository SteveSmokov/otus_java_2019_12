package ru.otus.entities;

import javax.persistence.*;

@Entity
@Table(name = "address")
public class AddressDataSet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "street")
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
