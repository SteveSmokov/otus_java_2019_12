package ru.otus.amount;

import ru.otus.bill.IBill;

public interface IAmount {
    int getSum();
    void addBill(IBill bill, int count);
}
