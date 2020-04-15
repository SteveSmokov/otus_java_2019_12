package ru.otus.bill;

public interface Bill extends Comparable<Bill> {
    int getCost();
}
