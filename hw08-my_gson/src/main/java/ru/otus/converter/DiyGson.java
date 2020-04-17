package ru.otus.converter;


import javax.json.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class DiyGson {

    public String toJSON(Object obj) {
        return  obj == null ? JsonObject.NULL.toString() : this.toJsonValue(obj).toString();
    }

    private Object getFieldValue(Object object, Field field){
        Boolean isAccess = true;
        try {
            isAccess = field.canAccess(object);
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } finally {
            if(field != null && !isAccess){
                field.setAccessible(false);
            }

        }

    }

    private JsonValue toJsonValue(Object object){
        if (object instanceof Number){
            return numberIntoJsonValue((Number) object);
        } else if (object instanceof Boolean){
            return object.equals(true) ? JsonObject.TRUE : JsonObject.FALSE;
        } else if (object instanceof Character || object instanceof String){
            return Json.createValue(String.valueOf(object));
        } else if (object instanceof Date){
            return dateIntoJsonValue(object);
        } else if (object.getClass().isArray()){
            return arrayIntoJsonValue(object);
        } else if (object instanceof Collection){
            return collectionIntoJsonValue(object);
        } else if (object instanceof Map){
            return mapIntoJsonValue(object);
        } return  objectIntoJsonValue(object, object.getClass());
    }

    private JsonValue arrayIntoJsonValue(Object object){
        JsonArrayBuilder result = Json.createArrayBuilder();
        for(int i=0; i<Array.getLength(object); i++){
            Object arrayElement = Array.get(object, i);
            result.add(toJsonValue(arrayElement));
        }
        return result.build();
    }

    private JsonValue collectionIntoJsonValue(Object object){
        Object[] objectArray = ((Collection) object).toArray();
        return arrayIntoJsonValue(objectArray);
    }

    private JsonValue mapIntoJsonValue(Object object){
        JsonObjectBuilder result = Json.createObjectBuilder();
        ((Map) object).forEach((k, v) -> result.add(k.toString(), toJsonValue(v)));
        return result.build();
    }

    private JsonValue dateIntoJsonValue(Object object){
        SimpleDateFormat dt = new SimpleDateFormat("MMM d, yyyy, h:mm:ss a", Locale.ENGLISH);
        return Json.createValue(dt.format((Date) object));
    }

    private JsonValue objectIntoJsonValue(Object object, Class<?> oClass){
        JsonObjectBuilder result = Json.createObjectBuilder();

        Class<?> objectClass = oClass;
        while (objectClass != Object.class) {
            Field[] fields = objectClass.getDeclaredFields();
            for(Field field : fields){
                if(Modifier.isTransient(field.getModifiers()) || Modifier.isStatic(field.getModifiers())){
                    continue;
                }

                final Object fieldValue = getFieldValue(object, field);
                if( fieldValue != null ){
                    result.add(field.getName(), toJsonValue(fieldValue));
                }
            }
            objectClass = objectClass.getSuperclass();
        }
        return result.build();
    }

    private JsonValue numberIntoJsonValue(Number number){
        if(number instanceof Integer){
            return Json.createValue(number.intValue());
        } else if (number instanceof Float || number instanceof Double){
            return Json.createValue(number.doubleValue());
        } else if (number instanceof Long){
            return Json.createValue(number.longValue());
        } else if (number instanceof Short){
            return Json.createValue(number.shortValue());
        } else if (number instanceof Byte){
            return Json.createValue(number.byteValue());
        } else return JsonObject.NULL;
    }
}
