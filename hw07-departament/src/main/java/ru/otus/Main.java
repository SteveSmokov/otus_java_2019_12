package ru.otus;

import ru.otus.amount.IAmount;
import ru.otus.atm.ATM;
import ru.otus.atm.IATM;
import ru.otus.bill.Bill;
import ru.otus.departament.Departament;
import ru.otus.departament.IDepartament;
import ru.otus.exceptions.NotBillException;
import ru.otus.exceptions.NotEnoughMoneyException;

public class Main {
    private static IDepartament departament;
    public static void main(String[] args){
        departament = new Departament("GosBank");
        for (int i=1; i<=5; i++) {
            departament.createATM(new ATM.ATMBuilder()
                    .loadCash(Bill.ONE, 1000 * i)
                    .loadCash(Bill.TWO, 1000 * i)
                    .loadCash(Bill.FIVE, 1000 * i)
                    .loadCash(Bill.TEN, 1000 * i)
                    .loadCash(Bill.TWENTY, 1000 * i)
                    .loadCash(Bill.FIFTY, 500 * i)
                    .loadCash(Bill.ONE_HUNDRED, 500 * i)
                    .loadCash(Bill.TWO_HUNDRED, 500 * i)
                    .loadCash(Bill.FIVE_HUNDRED, 200 * i)
                    .loadCash(Bill.ONE_THOUSAND, 100 * i)
                    .build(departament.getDepartName() + " ATM_" + i));
        }

        checkDepBalance();

        getMoneyFromATM(1, 400000);
        checkDepBalance();
        getMoneyFromATM(2, 400080);
        checkDepBalance();

        System.out.println();
        System.out.println("---------------------------------------------------------");
        System.out.println("Воостановление сосотояния всех АТМ в департаменте - " + departament.getDepartName());
        departament.resetATMsState();

        checkDepBalance();
    }

    private static void getMoneyFromATM(int atmIndex, int sum) {
        IATM atm = departament.getATMByIndex(atmIndex);
        try {
            System.out.println();
            System.out.println("---------------------------------------------------------");
            System.out.println("Операция снятие наличных на сумму " + sum + " из банкомата - " + atm.getName());
            IAmount amount = atm.getCash(sum);
            System.out.println(amount);
            System.out.println();
            System.out.println("Остаток средств в банкомате - " + atm.getBalance());
        } catch (NotEnoughMoneyException e) {
            System.out.println("Не удалось снять наличные. Ошибка: " + e.getMessage());
        } catch (NotBillException e) {
            System.out.println("Не удалось снять наличные. Ошибка: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Не удалось найти банкомат по индексу. Ошибка: " + e.getMessage());
        } finally {
            System.out.println("---------------------------------------------------------");
        }
    }

    private static void checkDepBalance() {
        System.out.println();
        System.out.println("---------------------------------------------------------");
        System.out.println("Сумма денег в департаменте - " + departament.getTotalBalance());
        System.out.println("---------------------------------------------------------");
    }
}
