package ru.otus.homework.entries;

import java.util.Comparator;

public class Dog extends Animal implements Comparator<Dog>, Comparable<Dog> {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compare(Dog dog1, Dog dog2) {
        return dog1.getName().compareTo(dog2.getName());
    }

    @Override
    public int compareTo(Dog o) {
        return this.getName().compareTo(o.getName());
    }
}
