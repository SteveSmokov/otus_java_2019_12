package common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.common.Serializers;
import ru.otus.entities.User;

import java.util.Arrays;
import java.util.List;

class SerializersTest {
    public static final Logger log = LoggerFactory.getLogger(SerializersTest.class);
    private final Serializers serializers = new Serializers();
    @Test
    @DisplayName(value = "Конвертирование в byte массив и обратно в объект")
    public void serializeDesearializeObject(){

        User testUserClass = new User("admin","admin","admin");
        log.info(testUserClass.toString());
        byte[] data = serializers.serialize(testUserClass);
        User testUserClass2 = serializers.deserialize(data, User.class);
        log.info(testUserClass2.toString());
        Assertions.assertTrue(testUserClass.equals(testUserClass2));
    }

    @Test
    @DisplayName(value = "Конвертирование списка в byte массив и обратно в список объектов")
    public void serializeDesearializeObjectsList(){
        List<User> users = Arrays.asList(new User("admin","lgn1","psw1"),
                new User("admin2","lgn2","psw2"),
                new User("asmin3","lgn3","psw3"));
        log.info(users.toString());
        byte[] data = serializers.serialize(users);
        List<User> users2 = (List<User>) serializers.deserializeObjectList(data);
        log.info(users2.toString());
        Assertions.assertTrue(users.equals(users2));


    }
}