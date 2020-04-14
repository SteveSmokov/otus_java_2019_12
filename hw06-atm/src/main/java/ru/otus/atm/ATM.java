package ru.otus.atm;

import ru.otus.amount.Amount;
import ru.otus.amount.IAmount;
import ru.otus.bill.IBill;
import ru.otus.exceptions.NotBillException;
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

    private void getCasheATBalance(IAmount amaunt) {
        Integer value;
        for(Map.Entry<IBill,Integer> e: amaunt.entrySet()){
            value = balance.get(e.getKey()) - e.getValue();
            balance.put(e.getKey(), value);
        }
    }

    private Boolean reverseCreateAmount(IAmount amaunt, int sum){
        Boolean result = false;
        Map<IBill, Integer> bal1 = new TreeMap<IBill, Integer>(Comparator.comparing(IBill::getCost));
        amaunt.entrySet().stream()
                .forEachOrdered(x -> bal1.put(x.getKey(), x.getValue()));
        IBill minKey = balance.keySet().stream().min(Comparator.comparing(IBill::getCost)).get();
        int count;
        for(Map.Entry<IBill,Integer> e: bal1.entrySet()){
            sum = sum + e.getKey().getCost() * e.getValue();
            bal1.replace(e.getKey(), e.getValue(), 0);
            count = sum / minKey.getCost();
            if (((sum - count * minKey.getCost()) == 0) && (count<=balance.get(minKey))){
                bal1.entrySet().removeIf(entry -> entry.getValue()==0);
                amaunt.clear();
                amaunt.putAll(bal1);
                amaunt.addBill(minKey, count);
                result = true;
                break;
            }
        }
        return result;
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
