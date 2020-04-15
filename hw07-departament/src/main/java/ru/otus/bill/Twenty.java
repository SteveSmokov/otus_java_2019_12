package ru.otus.bill;

public class Twenty implements Bill {
    private final int cost = 20;
    @Override
    public int getCost() {
        return this.cost;
    }

    @Override
    public String toString() {
        return "Twenty";
    }

    @Override
    public int compareTo(Bill o) {
        return Integer.compare(this.cost, o.getCost());
    }
}