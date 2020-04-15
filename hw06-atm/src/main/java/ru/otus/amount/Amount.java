package ru.otus.amount;

import ru.otus.bill.Bill;

import java.util.Map;
import java.util.Set;

public interface Amount {
    int getSum();
    void addBill(Bill bill, int count);
    Set<Map.Entry<Bill, Integer>> entrySet();
    void clear();
    void putAll(Map<Bill, Integer> map);
}
