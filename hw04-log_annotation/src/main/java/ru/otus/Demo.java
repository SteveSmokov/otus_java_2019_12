package ru.otus;

import ru.otus.classes.*;
import ru.otus.proxy.IoC;

public class Demo {
    public static void main(String[] args) {
        TestLoggingInterface testLog = (TestLoggingInterface) IoC.createClass(new TestLogging());
        testLog.calculation(6);
        int value = testLog.calc3(1,2,3);
        String str = testLog.concantenate("Hello", "world");
        testLog.hello_world();
        testLog.hello_world_no_annotaded();
    }
}
