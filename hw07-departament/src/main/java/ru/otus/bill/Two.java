package ru.otus.bill;

public class Two implements Bill {
    private final int cost = 2;
    @Override
    public int getCost() {
        return this.cost;
    }

    @Override
    public String toString() {
        return "Two";
    }

    @Override
    public int compareTo(Bill o) {
        return Integer.compare(this.cost, o.getCost());
    }
}