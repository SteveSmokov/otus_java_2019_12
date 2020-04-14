package ru.otus.proxy;

import ru.otus.annotations.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class IoC {

    public static Object createClass(Object logObject) {
        InvocationHandler handler = new DemoInvocationHandler(logObject);
        return  Proxy.newProxyInstance(IoC.class.getClassLoader(),
                logObject.getClass().getInterfaces(), handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final Object logObject;
        private final Set<String> logMethodsName = new HashSet<>();
        DemoInvocationHandler(Object logObject) {
            this.logObject = logObject;
            Set<Method> logMethods = Arrays.stream(logObject.getClass().getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(Log.class)).collect(Collectors.toSet());
            logMethods.stream().forEach(method -> this.logMethodsName.add(getMetodDescription(method)));
        }

        private String getMetodDescription(Method method) {
            return method.getName() + " : " +
                   Arrays.stream(method.getParameterTypes()).map(Class::toString)
                           .collect(Collectors.joining("-"));
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
            if (logMethodsName.contains(getMetodDescription(method))) {
                String argsString = ((args==null) || (args.length==0)) ? "no params" : "params : " +
                        Arrays.stream(args).map(Object::toString).collect(Collectors.joining("; "));
                System.out.printf("Executed method: %s, %s.", method.getName(), argsString);
                System.out.println();
            }
            return method.invoke(logObject, args);
        }
    }
}
