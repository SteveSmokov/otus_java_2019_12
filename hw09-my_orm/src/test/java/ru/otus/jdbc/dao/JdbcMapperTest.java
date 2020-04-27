package ru.otus.jdbc.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.enteties.User;

class JdbcMapperTest {
    private JdbcMapper<User> userDBMapper;
    @BeforeEach
    void setUp() {
        userDBMapper = new JdbcMapper(User.class);
    }

    @Test
    void getCreateTableScript() {
        final String EXPECTED_CREATE_USER_SCRIPT = "CREATE TABLE IF NOT EXISTS public.USER (ID bigint(20) NOT NULL " +
                "AUTO_INCREMENT, NAME varchar(255), AGE int(3));";
        String createUserSqript = userDBMapper.getCreateTableScript();
        Assertions.assertEquals(EXPECTED_CREATE_USER_SCRIPT, createUserSqript);
    }

    @Test
    void getDropTableScript() {
        final String EXPECTED_DROP_USER_SCRIPT = "DROP TABLE IF EXISTS USER;";
        String dropUserSqript = userDBMapper.getDropTableScript();
        Assertions.assertEquals(EXPECTED_DROP_USER_SCRIPT,dropUserSqript);
    }

    @Test
    void getInsertScript() {
        final String EXPECTED_INSERT_USER_SCRIPT = "INSERT INTO USER (name, age) VALUES (?, ?);";
        String insertScript = userDBMapper.getInsertScript();
        Assertions.assertEquals(EXPECTED_INSERT_USER_SCRIPT,insertScript);
    }

    @Test
    void getUpdateScript() {
        final String EXPECTED_USER_SCRIPT = "UPDATE USER SET name = ?, age = ? WHERE id = ?;";
        String updateScript = userDBMapper.getUpdateScript();
        Assertions.assertEquals(EXPECTED_USER_SCRIPT,updateScript);
    }

    @Test
    void getDeleteScript() {
        final String EXPECTED_USER_SCRIPT = "DELETE FROM USER WHERE id = ?;";
        String deleteScript = userDBMapper.getDeleteScript();
        Assertions.assertEquals(EXPECTED_USER_SCRIPT,deleteScript);
    }

    @Test
    void getSelectScript() {
        final String EXPECTED_USER_SCRIPT = "SELECT id, name, age FROM USER WHERE id = ?;";
        String selectScript = userDBMapper.getSelectScript();
        Assertions.assertEquals(EXPECTED_USER_SCRIPT,selectScript);
    }
}