package ru.otus.annotations;

import ru.otus.enteties.DBType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    DBType FieldType() default DBType.VARCHAR;
    int MaxLength() default 0;
    boolean Nullable() default true;
}
