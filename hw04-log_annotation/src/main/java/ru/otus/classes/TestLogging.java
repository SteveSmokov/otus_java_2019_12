package ru.otus.classes;

import ru.otus.annotations.Log;

public class TestLogging implements TestLoggingInterface {
    @Log
    @Override
    public void calculation(int param){
    }

    @Log
    @Override
    public String concantenate(String str1, String str2) {
        return (str1==null || str1.isEmpty()) ? str2 : str1 + ' ' +str2 + '!';
    }

    @Log
    @Override
    public int calc3(int num1, int num2, int num3) {
        return num1 + num2 + num3;
    }

    @Log
    @Override
    public void hello_world() {

    }

    @Override
    public void hello_world_no_annotaded() {

    }
}
