package ru.otus.atm.memento;

import ru.otus.bill.Bill;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class Memento {
    private final Map<Bill, Integer> initState;

    public Memento(Map<Bill, Integer> initState) {
        this.initState = new TreeMap<>(Collections.reverseOrder(Comparator.comparing(Bill::getCost)));
        this.initState.putAll(initState);
    }

    public Map<Bill, Integer> getPrevBalanceState(){
        return this.initState;
    }
}
