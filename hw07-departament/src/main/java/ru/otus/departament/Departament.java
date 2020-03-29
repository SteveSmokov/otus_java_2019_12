package ru.otus.departament;

import ru.otus.atm.IATM;

import java.util.ArrayList;
import java.util.List;

public class Departament implements IDepartament {
    private final String departName;
    private final static List<IATM> listATM = new ArrayList<>();

    @Override
    public String getDepartName() {
        return departName;
    }

    @Override
    public void createATM(IATM atm) {
        listATM.add(atm);
    }

    @Override
    public void resetATMsState() {
        listATM.stream().forEach(IATM::resetState);
    }

    @Override
    public int getTotalBalance() {
        return listATM.stream().mapToInt(IATM::getBalance).sum();
    }

    @Override
    public IATM getATMByIndex(int index) {
        return (index<listATM.size())?listATM.get(index):null;
    }

    public Departament(String departamentName){
        this.departName = departamentName;
    }
}
