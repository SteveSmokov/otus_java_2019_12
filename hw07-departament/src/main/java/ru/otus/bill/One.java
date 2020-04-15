package ru.otus.bill;

public class One implements Bill {
    private final int cost = 1;
    @Override
    public int getCost() {
        return this.cost;
    }

    @Override
    public String toString() {
        return "One";
    }

    @Override
    public int compareTo(Bill o) {
        return Integer.compare(this.cost, o.getCost());
    }
}
