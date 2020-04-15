package ru.otus.bill;

public class Five implements Bill {
    private final int cost = 5;
    @Override
    public int getCost() {
        return this.cost;
    }

    @Override
    public String toString() {
        return "Five";
    }

    @Override
    public int compareTo(Bill o) {
        return Integer.compare(this.cost, o.getCost());
    }
}
