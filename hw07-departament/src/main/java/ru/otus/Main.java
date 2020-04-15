package ru.otus;

import ru.otus.amount.Amount;
import ru.otus.atm.ATMImpl;
import ru.otus.atm.ATM;
import ru.otus.bill.*;
import ru.otus.departament.DepartamentImpl;
import ru.otus.departament.Departament;
import ru.otus.exceptions.NotBillException;
import ru.otus.exceptions.NotEnoughMoneyException;

public class Main {
    private static Departament departament;
    public static void main(String[] args){
        departament = new DepartamentImpl("GosBank");
        for (int i=1; i<=5; i++) {
            departament.createATM(new ATMImpl.ATMBuilder()
                    .loadCash(new One(), 1000 * i)
                    .loadCash(new Two(), 1000 * i)
                    .loadCash(new Five(), 1000 * i)
                    .loadCash(new Ten(), 1000 * i)
                    .loadCash(new Twenty(), 1000 * i)
                    .loadCash(new Fifty(), 500 * i)
                    .loadCash(new OneHundred(), 500 * i)
                    .loadCash(new TwoHundred(), 500 * i)
                    .loadCash(new FiveHundred(), 200 * i)
                    .loadCash(new OneThousand(), 100 * i)
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
        ATM atm = departament.getATMByIndex(atmIndex);
        try {
            System.out.println();
            System.out.println("---------------------------------------------------------");
            System.out.println("Операция снятие наличных на сумму " + sum + " из банкомата - " + atm.getName());
            Amount amount = atm.getCash(sum);
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
