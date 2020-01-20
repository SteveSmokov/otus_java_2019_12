package ru.otus.homework;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class HelloOtus {
    public static void main(String[] args){
        List<String> list = ImmutableList.of("First", "Second", "Third");
        System.out.println(list);
    }
}