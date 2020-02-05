package ru.otus.homework;

import ru.otus.homework.collection.DIYArrayList;
import ru.otus.homework.entries.Dog;
import ru.otus.homework.entries.Haski;
import ru.otus.homework.entries.Laika;
import ru.otus.homework.entries.Mops;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Dog> dogList = Arrays.asList(new Haski("Stella"), new Haski("Max"), new Haski("Anabel"));
        System.out.println("Dogs list" + dogList);

        List<? super Dog> animalCopiedList = new ArrayList<>(dogList);
        Collections.copy(animalCopiedList, dogList);
        System.out.println("Copied dogs list: " + animalCopiedList);

        Collections.addAll(animalCopiedList,new Mops("Vendor"), new Laika("Elen"), new Mops("Den"));
        System.out.println("Large dogs list : " + animalCopiedList);

        Collections.sort(animalCopiedList, Collections.reverseOrder());
        System.out.println("Sorted dogs list : " + animalCopiedList);

    }
}
