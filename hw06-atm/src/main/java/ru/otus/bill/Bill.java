package ru.otus.bill;


public enum Bill {
    ONE(1),
    TWO(2),
    FIVE(5),
    TEN(10),
    TWENTY(20),
    FIFTY(50),
    ONE_HUNDRED(100),
    TWO_HUNDRED(200),
    FIVE_HUNDRED(500),
    ONE_THOUSAND(1000);

    private final int cost;

    Bill(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return this.cost;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "cost=" + cost +
                '}';
    }
}
