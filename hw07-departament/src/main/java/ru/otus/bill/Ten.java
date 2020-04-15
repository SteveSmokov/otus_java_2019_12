package ru.otus.bill;

public class Ten  implements Bill {
    private final int cost = 10;
    @Override
    public int getCost() {
        return this.cost;
    }

    @Override
    public String toString() {
        return "Ten";
    }

    @Override
    public int compareTo(Bill o) {
        return Integer.compare(this.cost, o.getCost());
    }
}
