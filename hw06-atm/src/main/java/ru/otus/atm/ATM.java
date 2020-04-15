package ru.otus.atm;

import ru.otus.amount.Amount;
import ru.otus.bill.Bill;
import ru.otus.exceptions.NotBillException;
import ru.otus.exceptions.NotEnoughMoneyException;

public interface ATM {
    void loadCash(Bill cash, int quantity);
    Amount getCash(int sum) throws NotEnoughMoneyException, NotBillException;
    int getBalance();
}
