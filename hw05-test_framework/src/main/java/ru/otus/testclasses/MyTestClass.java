package ru.otus.testclasses;

import ru.otus.mytunit.annotations.After;
import ru.otus.mytunit.annotations.Before;
import ru.otus.mytunit.annotations.Test;

public class MyTestClass {
    @After
    public static void afterTest(){
        System.out.println("Procedure After test!");
    }

//    @After
//    public void afterTestException() throws Exception {
//        System.out.println("Procedure After test with exception!");
//        throw new Exception("Before test with exception");
//    }

    @Before
    public static void beforeTest() {
        System.out.println("Procedure Before test!");
    }

//    @Before
//    public void beforeTestException() throws Exception {
//        System.out.println("Procedure Before test with exception!");
//        throw new Exception("Before test with exception");
//    }


    @Test
    public void test(){
        System.out.println("Procedure Test!");
    }

    @Test
    public void testException() throws Exception {
        System.out.println("Procedure Test with exception!");
        throw new Exception("Test Exception");
    }
}
