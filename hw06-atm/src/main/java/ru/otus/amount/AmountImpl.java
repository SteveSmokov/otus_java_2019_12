package ru.otus.amount;

import ru.otus.bill.Bill;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class AmountImpl implements Amount {
    private final Map<Bill, Integer> money = new TreeMap<>();

    @Override
    public int getSum() {
        return money.entrySet().stream().mapToInt(bill -> bill.getKey().getCost() * bill.getValue()).sum();
    }

    @Override
    public void addBill(Bill bill, int count) {
        money.put(bill, count);
    }

    @Override
    public Set<Map.Entry<Bill, Integer>> entrySet() {
        return money.entrySet();
    }

    @Override
    public void clear(){
        money.clear();
    }

    @Override
    public void putAll(Map<Bill, Integer> map) {
        money.clear();
        money.putAll(map);
    }

    @Override
    public String toString() {
        return "Amount { " +
                "money = " + money +
                " }";
    }
}
