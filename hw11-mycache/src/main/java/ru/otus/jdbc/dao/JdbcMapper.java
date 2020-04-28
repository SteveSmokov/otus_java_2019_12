package ru.otus.jdbc.dao;

import ru.otus.annotations.Column;
import ru.otus.annotations.Id;
import ru.otus.enteties.DBType;
import ru.otus.reflection.ReflectionClass;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JdbcMapper<T> {
    private final String CREATE_TABLE_SCRIPT_MASK = "CREATE TABLE IF NOT EXISTS public.%s (%s);";
    private final String DROP_TABLE_SCRIPT_MASK = "DROP TABLE IF EXISTS %s;";
    private final String INSERT_RECORD_SCRIPT_MASK = "INSERT INTO %s (%s) VALUES (%s);";
    private final String UPDATE_RECORD_SCRIPT_MASK = "UPDATE %s SET %s WHERE %s;";
    private final String DELETE_RECORD_SCRIPT_MASK = "DELETE FROM %s WHERE %s;";
    private final String SELECT_RECORD_SCRIPT_MASK = "SELECT %s FROM %s WHERE %s;";
    private Class clazz;
    private String tableName;
    private List<Field> fields;

    public JdbcMapper(Class<? extends T> clazz) {
        this.clazz = clazz;
        this.tableName = clazz.getSimpleName().toUpperCase();
        this.fields = ReflectionClass.getFields(clazz);
    }

    public String getCreateTableScript(){
        return String.format(CREATE_TABLE_SCRIPT_MASK, this.tableName, getFieldsScript());
    }

    public String getDropTableScript(){
        return String.format(DROP_TABLE_SCRIPT_MASK,tableName);
    }

    public String getInsertScript(){
        return String.format(INSERT_RECORD_SCRIPT_MASK,
                this.tableName,
                getFieldsName(),
                getValuesScript());
    }

    public String getUpdateScript(){
        return String.format(UPDATE_RECORD_SCRIPT_MASK,
                this.tableName,
                String.join(" = ?, ",
                        getFieldsForUpdate().stream().map(Field::getName).collect(Collectors.toList())) + " = ?",
                getKeyScript());
    }

    public String getKeyScript(){
        return getFieldAutoincrement().getName() + " = ?" ;
    }

    public String getDeleteScript(){
        return String.format(DELETE_RECORD_SCRIPT_MASK, this.tableName, getKeyScript());
    }

    public String getSelectScript(){
        return String.format(SELECT_RECORD_SCRIPT_MASK, getAllFieldsName(), this.tableName, getKeyScript());
    }

    private String getFieldTypeAndSize(Field field){
        DBType dbType = field.getAnnotation(Column.class).FieldType();
        int columnSize = field.getAnnotation(Column.class).MaxLength();
        boolean notNull = (field.isAnnotationPresent(Id.class) || (!field.getAnnotation(Column.class).Nullable()));
        return dbType.getType() + ((columnSize==0) ? "" : "("+ columnSize +")") +
                ((notNull) ? " NOT NULL" : "");
    }

    private String getAutoIncremenntValue(Field field){
        if (field.isAnnotationPresent(Id.class) && field.getAnnotation(Id.class).AutoIncrement()){
            return " AUTO_INCREMENT";
        } else return "";
    }

    private String getFieldScript(Field field){
        return field.getName().toUpperCase() + " " + getFieldTypeAndSize(field) +
                getAutoIncremenntValue(field);
    }

    private String getFieldsScript(){
        return String.join(", ", fields.stream().map(this::getFieldScript).collect(Collectors.toList()));
    }

    private String getFieldsName(){
        return String.join(", ", getFieldsForInsert().stream().map(Field::getName).collect(Collectors.toList()));
    }

    private String getAllFieldsName(){
        return String.join(", ", fields.stream().map(Field::getName).collect(Collectors.toList()));
    }

    private boolean isColumnAutoIncrement(Field field){
        if (field.isAnnotationPresent(Id.class) &&
            field.getAnnotation(Id.class).AutoIncrement()){
            return true;
        } else return false;
    }

    public Field getFieldAutoincrement(){
        Optional<Field> optField = fields.stream().filter(field -> isColumnAutoIncrement(field)).findFirst();
        if (!optField.isPresent()) {
            throw new EntetyException("This class didn't have autoincrement field");
        }
        return optField.get();
    }

    public List<Field> getFieldsForUpdate(){
        return fields.stream().filter(field -> !isColumnAutoIncrement(field)).collect(Collectors.toList());
    }

    private List<Field> getFieldsForInsert(){
        return fields.stream().filter(field -> !isColumnAutoIncrement(field)).collect(Collectors.toList());
    }

    public List<Object> getValuesForInsert(T t){
        return fields.stream().filter(field -> !isColumnAutoIncrement(field))
                .map(field -> ReflectionClass.getFieldValue(t, field.getName())).collect(Collectors.toList());
    }

    private String getValuesScript(){
        return Stream.iterate("?", n -> n).limit(getFieldsForInsert().size()).collect(Collectors.joining(", "));
    }
}
