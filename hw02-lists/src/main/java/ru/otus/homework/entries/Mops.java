package ru.otus.homework.entries;

public class Mops extends Dog {
    private String name;

    public Mops(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Mops, name='" + name + '\'';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
