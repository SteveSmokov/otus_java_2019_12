package ru.otus.atm;

import ru.otus.amount.Amount;
import ru.otus.amount.IAmount;
import ru.otus.bill.IBill;
import ru.otus.exceptions.NotEnoughMoneyException;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class ATM implements IATM {
    Map<IBill, Integer> balance =
            new TreeMap<>(Collections.reverseOrder(Comparator.comparing(IBill::getCost)));

    @Override
    public void loadCash(IBill cash, int quantity) {
        balance.put(cash, quantity);
    }

    @Override
    public IAmount getCash(int sum) throws NotEnoughMoneyException {
        if (sum > getBalance()){
            throw new NotEnoughMoneyException("Не достаточно средств в банкомате! Укажите пожалуйста другую сумму.");
        }
        IAmount amountIssued = new Amount();
        int rest = sum;
        int count;
        for(Map.Entry<IBill, Integer> e: balance.entrySet()) {
            if (sum > 0) {
                if (e.getValue() > 0) {
                    count = sum / e.getKey().getCost();
                    if (count > 0) {
                        if (count > e.getValue()) count = e.getValue();
                        amountIssued.addBill(e.getKey(), count);
                        sum = sum - (count * e.getKey().getCost());
                        count = e.getValue() - count;
                        e.setValue(count);
                    }
                }
            } else break;
        }
        return amountIssued;
    }

    @Override
    public int getBalance() {
        return balance.entrySet().stream().mapToInt(bill -> bill.getKey().getCost() * bill.getValue()).sum();
    }

    @Override
    public String toString() {
        return "ATM { " +
                "balance=" + balance +
                " }";
    }
}
