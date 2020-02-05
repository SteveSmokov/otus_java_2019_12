package ru.otus.homework.entries;

public class Laika extends Dog {
    private String name;

    public Laika(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Laika, name='" + name + '\'';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
