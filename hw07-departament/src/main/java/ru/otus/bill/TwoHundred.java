package ru.otus.bill;

public class TwoHundred implements Bill {
    private final int cost = 200;
    @Override
    public int getCost() {
        return this.cost;
    }

    @Override
    public String toString() {
        return "TwoHundred";
    }

    @Override
    public int compareTo(Bill o) {
        return Integer.compare(this.cost, o.getCost());
    }
}