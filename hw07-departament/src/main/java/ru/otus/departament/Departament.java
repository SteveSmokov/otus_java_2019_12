package ru.otus.departament;

import ru.otus.atm.ATM;

public interface Departament {
    void resetATMsState();
    int getTotalBalance();
    ATM getATMByIndex(int index);
    void createATM(ATM atm);
    String getDepartName();
}
