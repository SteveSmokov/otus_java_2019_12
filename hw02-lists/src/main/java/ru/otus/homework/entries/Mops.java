package ru.otus.homework.entries;

import java.util.Comparator;

public class Mops extends Dog {
    public Mops(String name) {
        this.setName(name);
    }

    @Override
    public String toString() {
        return "Mops, name='" + this.getName() + '\'';
    }

    public static class SortByNames implements Comparator<Dog> {

        public int compare(Dog dog1, Dog dog2) {
            return dog1.getName().compareTo(dog2.getName());
        }

    }
}
