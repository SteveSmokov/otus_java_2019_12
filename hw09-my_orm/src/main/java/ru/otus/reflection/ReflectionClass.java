package ru.otus.reflection;

import ru.otus.annotations.Column;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class ReflectionClass {

    public static void setFieldValue(Object object, Field field, Object value) {
        boolean fAccessible = true;
        try {
            fAccessible = field.canAccess(object);
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (field != null && !fAccessible){
                field.setAccessible(fAccessible);
            }
        }
    }

    public static <T> T instantiate(Class<T> type) {
        try {
            final Constructor<T> constructor = type.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Object getFieldValue(Object object, String fieldName){
        Field field = null;
        boolean fAccessable = true;
        try {
            field = object.getClass().getDeclaredField(fieldName);
            fAccessable = field.canAccess(object);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (field != null && !fAccessable){
                field.setAccessible(false);
            }
        }
    }

    public static Object getFieldValue(Object object, Field field){
        boolean fAccessable = true;
        try {
            fAccessable = field.canAccess(object);
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (field != null && !fAccessable){
                field.setAccessible(false);
            }
        }
    }

    public static List<Field> getFields(Class clazz){
        List<Field> fields = new ArrayList<>();
        Class localClass = clazz;
        if (localClass!=null && !Object.class.equals(localClass)) {
            fields.addAll(Arrays.asList(localClass.getDeclaredFields()));
            localClass = localClass.getSuperclass();
        }
        return  fields.stream().filter(field -> field.isAnnotationPresent(Column.class)).collect(Collectors.toList());
    }
}
