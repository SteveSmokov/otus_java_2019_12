package ru.otus.homework;

import ru.otus.homework.collection.DIYArrayList;
import ru.otus.homework.collection.MyArrList;
import ru.otus.homework.entries.Dog;
import ru.otus.homework.entries.Haski;
import ru.otus.homework.entries.Laika;
import ru.otus.homework.entries.Mops;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<Dog> dogList = Arrays.asList(new Haski("Stella"), new Mops("Max"), new Haski("Anabel"));
        System.out.println("Dogs list: " + dogList);

        List<Dog> animalCopiedList = new DIYArrayList<>(dogList.size());
        Collections.copy(animalCopiedList, dogList);
        System.out.println("Copied dogs list: " + animalCopiedList);

        Collections.addAll(animalCopiedList,new Mops("Vendor"), new Laika("Elen"), new Mops("Den"));
        System.out.println("Large dogs list : " + animalCopiedList);

        Collections.sort(dogList, Comparator.naturalOrder());
        System.out.println("Sorted dogs list : " + dogList);

        Collections.sort(animalCopiedList, Comparator.naturalOrder());
        System.out.println("Sorted large dogs list : " + animalCopiedList);
    }
}
