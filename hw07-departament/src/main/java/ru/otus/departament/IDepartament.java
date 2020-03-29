package ru.otus.departament;

import ru.otus.atm.IATM;

public interface IDepartament {
    void resetATMsState();
    int getTotalBalance();
    IATM getATMByIndex(int index);
    void createATM(IATM atm);
    String getDepartName();
}
