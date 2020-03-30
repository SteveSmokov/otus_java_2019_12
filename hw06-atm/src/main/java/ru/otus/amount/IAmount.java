package ru.otus.amount;

import ru.otus.bill.IBill;

import java.util.Map;
import java.util.Set;

public interface IAmount {
    int getSum();
    void addBill(IBill bill, int count);
    Set<Map.Entry<IBill, Integer>> entrySet();
    void clear();
    void putAll(Map<IBill, Integer> map);
}
