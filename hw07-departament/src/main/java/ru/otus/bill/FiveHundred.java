package ru.otus.bill;

public class FiveHundred implements Bill {
    private final int cost = 500;
    @Override
    public int getCost() {
        return this.cost;
    }

    @Override
    public String toString() {
        return "FiveHundred";
    }

    @Override
    public int compareTo(Bill o) {
        return Integer.compare(this.cost, o.getCost());
    }
}
