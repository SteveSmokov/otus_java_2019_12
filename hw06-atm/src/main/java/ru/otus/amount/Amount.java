package ru.otus.amount;

import ru.otus.bill.IBill;

import java.util.Map;
import java.util.TreeMap;

public class Amount implements IAmount {
    private final Map<IBill, Integer> money = new TreeMap<>();

    @Override
    public int getSum() {
        return money.entrySet().stream().mapToInt(bill -> bill.getKey().getCost() * bill.getValue()).sum();
    }

    @Override
    public void addBill(IBill bill, int count) {
        money.put(bill, count);
    }

    @Override
    public String toString() {
        return "Amount { " +
                "money = " + money +
                " }";
    }
}
