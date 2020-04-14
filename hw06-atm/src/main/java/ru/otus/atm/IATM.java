package ru.otus.atm;

import ru.otus.amount.IAmount;
import ru.otus.bill.IBill;
import ru.otus.exceptions.NotBillException;
import ru.otus.exceptions.NotEnoughMoneyException;

public interface IATM {
    void loadCash(IBill cash, int quantity);
    IAmount getCash(int sum) throws NotEnoughMoneyException, NotBillException;
    int getBalance();
}
