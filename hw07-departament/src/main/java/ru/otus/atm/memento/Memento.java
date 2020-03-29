package ru.otus.atm.memento;

import ru.otus.bill.IBill;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class Memento {
    private final Map<IBill, Integer> initState;

    public Memento(Map<IBill, Integer> initState) {
        this.initState = new TreeMap<>(Collections.reverseOrder(Comparator.comparing(IBill::getCost)));
        this.initState.putAll(initState);
    }

    public Map<IBill, Integer> getPrevBalanceState(){
        return this.initState;
    }
}
