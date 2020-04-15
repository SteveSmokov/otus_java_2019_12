package ru.otus.bill;

public class Fifty implements Bill {
    private final int cost = 50;
    @Override
    public int getCost() {
        return this.cost;
    }

    @Override
    public String toString() {
        return "Fifty";
    }

    @Override
    public int compareTo(Bill o) {
        return Integer.compare(this.cost, o.getCost());
    }
}
