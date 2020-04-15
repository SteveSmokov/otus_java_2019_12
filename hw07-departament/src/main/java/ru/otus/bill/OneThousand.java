package ru.otus.bill;

public class OneThousand implements Bill {
    private final int cost = 1000;
    @Override
    public int getCost() {
        return this.cost;
    }

    @Override
    public String toString() {
        return "OneThousand";
    }

    @Override
    public int compareTo(Bill o) {
        return Integer.compare(this.cost, o.getCost());
    }
}
