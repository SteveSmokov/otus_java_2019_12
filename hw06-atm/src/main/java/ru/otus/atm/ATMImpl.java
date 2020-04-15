package ru.otus.atm;

import ru.otus.amount.AmountImpl;
import ru.otus.amount.Amount;
import ru.otus.bill.Bill;
import ru.otus.exceptions.NotBillException;
import ru.otus.exceptions.NotEnoughMoneyException;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class ATMImpl implements ATM {
    private final Map<Bill, Integer> balance =
            new TreeMap<>(Collections.reverseOrder(Comparator.comparing(Bill::getCost)));

    @Override
    public void loadCash(Bill cash, int quantity) {
        balance.put(cash, quantity);
    }

    private void getCasheATBalance(Amount amaunt) {
        Integer value;
        for(Map.Entry<Bill,Integer> e: amaunt.entrySet()){
            value = balance.get(e.getKey()) - e.getValue();
            balance.put(e.getKey(), value);
        }
    }
    private Boolean reverseCreateAmount(Amount amount, int sum){
        Boolean result = false;
        Map<Bill, Integer> reverseAmount = new TreeMap<Bill, Integer>(Comparator.comparing(Bill::getCost));
        amount.entrySet().stream()
                .forEachOrdered(x -> reverseAmount.put(x.getKey(), x.getValue()));
        Bill minKey = balance.keySet().stream().min(Comparator.comparing(Bill::getCost)).get();
        int count;
        for(Map.Entry<Bill,Integer> e: reverseAmount.entrySet()){
            sum = sum + e.getKey().getCost() * e.getValue();
            reverseAmount.replace(e.getKey(), e.getValue(), 0);
            count = sum / minKey.getCost();
            if (((sum - count * minKey.getCost()) == 0) && (count<=balance.get(minKey))){
                reverseAmount.entrySet().removeIf(entry -> entry.getValue()==0);
                amount.clear();
                amount.putAll(reverseAmount);
                amount.addBill(minKey, count);
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public Amount getCash(int sum) throws NotEnoughMoneyException, NotBillException {
        if (sum > getBalance()){
            throw new NotEnoughMoneyException("Не достаточно средств в банкомате!");
        }
        Amount amountIssued = new AmountImpl();
        int count;
        for(Map.Entry<Bill, Integer> e: balance.entrySet()) {
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
        if (sum>0 && !reverseCreateAmount(amountIssued, sum)){
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
    public String toString() {
        return "ATM { " +
                "balance=" + balance +
                " }";
    }
}
