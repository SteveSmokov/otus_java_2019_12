package ru.otus.bill;

public class OneHundred implements Bill {
    private final int cost = 100;
    @Override
    public int getCost() {
        return this.cost;
    }

    @Override
    public String toString() {
        return "OneHundred";
    }

    @Override
    public int compareTo(Bill o) {
        return Integer.compare(this.cost, o.getCost());
    }
}
