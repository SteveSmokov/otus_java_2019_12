package ru.otus;

import ru.otus.amount.IAmount;
import ru.otus.atm.ATM;
import ru.otus.atm.IATM;
import ru.otus.bill.Bill;
import ru.otus.exceptions.NotBillException;
import ru.otus.exceptions.NotEnoughMoneyException;

public class Main {
    private static IATM atm;
    public static void main(String[] args){
        atm = new ATM();
        atm.loadCash(Bill.ONE,1000);
        atm.loadCash(Bill.TWO,1000);
        atm.loadCash(Bill.FIVE,1000);
        atm.loadCash(Bill.TEN,1000);
        atm.loadCash(Bill.TWENTY,1000);
        atm.loadCash(Bill.FIFTY,500);
        atm.loadCash(Bill.ONE_HUNDRED,500);
        atm.loadCash(Bill.TWO_HUNDRED,500);
        atm.loadCash(Bill.FIVE_HUNDRED,200);
        atm.loadCash(Bill.ONE_THOUSAND,100);

        System.out.println(atm);
        System.out.println();
        System.out.println("---------------------------------------------------------");
        System.out.println("Сумма денег в банкомате после пополнения - " + atm.getBalance());
        System.out.println("---------------------------------------------------------");

        getMoneyFromATM(4567);

        getMoneyFromATM(408490);

        getMoneyFromATM(400360);
        getMoneyFromATM(480);
        getMoneyFromATM(4434);
    }

    private static void getMoneyFromATM(int sum) {
        try {
            System.out.println();
            System.out.println("---------------------------------------------------------");
            System.out.println("Операция снятие наличных на сумму " + sum + " из банкомата" );
            IAmount amount = atm.getCash(sum);
            System.out.println(amount);
        } catch (NotEnoughMoneyException | NotBillException e) {
            System.out.println("Не удалось снять наличные. Ошибка: " + e.getMessage());
            System.out.println("Не удалось снять наличные. Ошибка: " + e.getMessage());
        } finally {
            System.out.println();
            System.out.println("Остаток средств в банкомате - " + atm.getBalance());
            System.out.println("---------------------------------------------------------");
        }
    }
}
