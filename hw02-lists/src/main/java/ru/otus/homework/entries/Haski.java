package ru.otus.homework.entries;

public class Haski extends Dog {
    private String name;

    public Haski(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Haski, name='" + name + '\'';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
