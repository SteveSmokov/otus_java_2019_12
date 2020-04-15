package ru.otus.departament;

import ru.otus.atm.ATM;

import java.util.ArrayList;
import java.util.List;

public class DepartamentImpl implements Departament {
    private final String departName;
    private final static List<ATM> listATM = new ArrayList<>();

    @Override
    public String getDepartName() {
        return departName;
    }

    @Override
    public void createATM(ATM atm) {
        listATM.add(atm);
    }

    @Override
    public void resetATMsState() {
        listATM.stream().forEach(ATM::resetState);
    }

    @Override
    public int getTotalBalance() {
        return listATM.stream().mapToInt(ATM::getBalance).sum();
    }

    @Override
    public ATM getATMByIndex(int index) {
        return (index<listATM.size())?listATM.get(index):null;
    }

    public DepartamentImpl(String departamentName){
        this.departName = departamentName;
    }
}
