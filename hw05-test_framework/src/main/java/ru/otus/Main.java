package ru.otus;

import ru.otus.testclasses.MyTestClass;
import ru.otus.mytunit.annotations.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            run(MyTestClass.class);
        } catch (Exception E) {
            E.printStackTrace();
        }

    }

    private static void run(Class<?> classTest) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Constructor<?> constructor = classTest.getConstructor();

        List<Method> testMethods = new ArrayList<>();
        Method beforeMethod = null;
        Method afterMethod = null;

        for (Method method : classTest.getDeclaredMethods()){
            if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            } else if (method.isAnnotationPresent(Before.class)) {
                beforeMethod = method;
            } else if (method.isAnnotationPresent(After.class)) {
                afterMethod = method;
            }
        }

        if (beforeMethod != null && Modifier.isStatic(beforeMethod.getModifiers())) {
            beforeMethod.invoke(null);
            System.out.println("Вызов метода Before!");
        }

        if (!testMethods.isEmpty()){
            for (Method method : testMethods){
                Object instance = constructor.newInstance();
                try {
                    method.invoke(instance);
                    System.out.println("Вызов метода " + method.getName() + '!');
                } catch (IllegalAccessException | InvocationTargetException e){
                    e.printStackTrace();
                }
            }
        }

        if (afterMethod != null && Modifier.isStatic(afterMethod.getModifiers())) {
            afterMethod.invoke(null);
            System.out.println("Вызов метода After!");
        }
    }
}