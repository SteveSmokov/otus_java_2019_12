package ru.otus.atm;

import ru.otus.amount.Amount;
import ru.otus.amount.IAmount;
import ru.otus.atm.memento.Memento;
import ru.otus.bill.IBill;
import ru.otus.exceptions.NotBillException;
import ru.otus.exceptions.NotEnoughMoneyException;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class ATM implements IATM {
    private final String nameATM;

    private Map<IBill, Integer> balance;
    private Memento memento;

    @Override
    public void resetState() {
        balance.clear();
        balance.putAll(memento.getPrevBalanceState());
    }

    @Override
    public String getName() {
        return nameATM;
    }

    public ATM(String nameATM, ATMBuilder builder) {
        this.nameATM = nameATM;
        this.balance = builder.balance;
        memento = new Memento(builder.balance);
    }

    @Override
    public void loadCash(IBill cash, int quantity) {
        balance.put(cash, quantity);
    }

    private void getCasheATBalance(IAmount amaunt) {
        Integer value;
        for(Map.Entry<IBill,Integer> e: amaunt.entrySet()){
            value = balance.get(e.getKey()) - e.getValue();
            balance.put(e.getKey(), value);
        }
    }

    @Override
    public IAmount getCash(int sum) throws NotEnoughMoneyException, NotBillException {
        if (sum > getBalance()){
            throw new NotEnoughMoneyException("Не достаточно средств в банкомате!");
        }
        IAmount amountIssued = new Amount();
        int count;
        for(Map.Entry<IBill, Integer> e: balance.entrySet()) {
            if (sum > 0) {
                if (e.getValue() > 0) {
                    count = sum / e.getKey().getCost();
                    if (count > 0) {
                        if (count > e.getValue()) count = e.getValue();
                        amountIssued.addBill(e.getKey(), count);
                        sum = sum - (count * e.getKey().getCost());
                    }
                }
            } else break;
        }
        if (sum>0){
            amountIssued.clear();
            throw new NotBillException("Отсуствуют некоторые номиналы купюр!");
        } else getCasheATBalance(amountIssued);
        return amountIssued;
    }

    @Override
    public int getBalance() {
        return balance.entrySet().stream().mapToInt(bill -> bill.getKey().getCost() * bill.getValue()).sum();
    }

    @Override
    public void clearBalance() {
        balance.keySet().forEach(bill -> balance.compute(bill, (bill2, oldCount) -> 0));
    }

    @Override
    public String toString() {
        return nameATM + " { " +
                "balance=" + balance +
                " }";
    }

    public static class ATMBuilder {
        private Map<IBill, Integer> balance =
                new TreeMap<>(Collections.reverseOrder(Comparator.comparing(IBill::getCost)));

        public ATMBuilder loadCash(IBill bill, int quantity){
            this.balance.put(bill, quantity);
            return this;
        }

        public ATM build(String nameATM){
            return new ATM(nameATM, this);
        }
    }
}
