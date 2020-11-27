package ru.otus.jdbc.templates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.jdbc.dao.DBExecutorImpl;
import ru.otus.jdbc.dao.EntetyException;
import ru.otus.jdbc.dao.DBExecutor;
import ru.otus.jdbc.dao.JdbcMapper;
import ru.otus.reflection.ReflectionClass;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;
import java.util.function.Function;

public class JdbcTemplateImpl<T> implements JdbcTemplate<T> {
    private final long ID_NULL = -1;
    private static Logger logger = LoggerFactory.getLogger(JdbcTemplateImpl.class);
    private final DataSource dataSource;
    private DBExecutor<T> executer;
    private JdbcMapper<T> mapper;

    public JdbcTemplateImpl(DataSource dataSource, Class<? extends T> clazz) {
        this.dataSource = dataSource;
        this.mapper = new JdbcMapper<>(clazz);
    }

    @Override
    public void createTable() throws SQLException {
        logger.info("Procedure for create table");
        try(Connection connection = dataSource.getConnection()){
            executer = new DBExecutorImpl<>(connection);
            String query = mapper.getCreateTableScript();
            logger.info("Received query script for create table");
            executer.formatDB(query);
            logger.info("Table is created");
        }
    }

    @Override
    public void deleteTable() throws SQLException {
        logger.info("Delete table procedure");
        try(Connection connection = dataSource.getConnection()){
            executer = new DBExecutorImpl<>(connection);
            String query = mapper.getDropTableScript();
            logger.info("Received query script for drop table");
            executer.formatDB(query);
            logger.info("Table is dropped");
        }
    }

    @Override
    public void create(T objectData) throws SQLException {
        logger.info("Procedure for insert record");
        try(Connection connection = dataSource.getConnection()){
            executer = new DBExecutorImpl<>(connection);
            String query = mapper.getInsertScript();
            logger.info("Received query script for insert record");
            List<Object> params = mapper.getValuesForInsert(objectData);
            logger.info("Received values("+params.size()+") for insert");
            Savepoint savepoint = connection.setSavepoint("InsertData");
            try {
                long id = executer.insert(query, params);
                logger.info("Inserted record, id=" + id);
                connection.commit();
                logger.info("Committed changes");
                ReflectionClass.setFieldValue(objectData, mapper.getFieldAutoincrement(), id);
                logger.info("Seted value to Id field");
            } catch (SQLException e) {
                logger.info("Rollback to savepoint. Error: "+e.getMessage());
                connection.rollback(savepoint);
            }

        }
    }

    @Override
    public void update(T objectData) throws SQLException {
        logger.info("Procedure for update record");
        try(Connection connection = dataSource.getConnection()){
            executer = new DBExecutorImpl<>(connection);
            String query = mapper.getUpdateScript();
            logger.info("Received query script for update record");
            List<Object> params = mapper.getValuesForInsert(objectData);
            logger.info("Received values("+params.size()+") for update");
            Savepoint savepoint = connection.setSavepoint("UpdateData");
            try {
                Object objectValue = ReflectionClass.getFieldValue(objectData, mapper.getFieldAutoincrement());
                long id = objectValue != null ? (long) objectValue : ID_NULL;
                if( id == ID_NULL ) {
                    throw new EntetyException("Id is null");
                } else {
                    executer.update(query, params, id);
                    logger.info("Update record, id=" + id);
                    connection.commit();
                    logger.info("Committed changes");
                }
            } catch (SQLException | EntetyException e) {
                logger.info("Rollback to savepoint. Error: "+e.getMessage());
                connection.rollback(savepoint);
            }

        }
    }

    @Override
    public void createOrUpdate(T objectData) throws SQLException {
        logger.info("Procedure for create or update record");
        try(Connection connection = dataSource.getConnection()){
            executer = new DBExecutorImpl<>(connection);
            logger.info("Received query script for update record");
            List<Object> params = mapper.getValuesForInsert(objectData);
            logger.info("Received values("+params.size()+") for update");
            Savepoint savepoint = connection.setSavepoint("CreateOrUpdateData");
            try {
                Object objectValue = ReflectionClass.getFieldValue(objectData, mapper.getFieldAutoincrement());
                long id = objectValue != null ? (long) objectValue : ID_NULL;
                if( id == ID_NULL ) {
                    id = executer.insert(mapper.getInsertScript(), params);
                    logger.info("Inserted record, id=" + id);
                    connection.commit();
                    logger.info("Committed changes");
                    ReflectionClass.setFieldValue(objectData, mapper.getFieldAutoincrement(), id);
                    logger.info("Seted value to Id field");
                } else {
                    executer.update(mapper.getUpdateScript(), params, id);
                    logger.info("Update record, id=" + id);
                    connection.commit();
                    logger.info("Committed changes");
                }
            } catch (SQLException | EntetyException e) {
                logger.info("Rollback to savepoint. Error: "+e.getMessage());
                connection.rollback(savepoint);
            }

        }
    }

    @Override
    public <T> T load(long id, Class<T> clazz) throws SQLException {
        logger.info("Function for select record");
        try(Connection connection = dataSource.getConnection()) {
            executer = new DBExecutorImpl<>(connection);
            String query = mapper.getSelectScript();
            logger.info("Received query script for select record");
            Function<ResultSet, T> function = resultSet -> {
                T resultObject;
                try {
                    resultObject = getObject(resultSet, clazz);
                    if (resultObject == null)
                        throw new EntetyException(String.format("Record in DB with ID=%d not found", id));
                    return resultObject;
                } catch (SQLException e) {
                    logger.info("Error: "+e.getMessage());
                }
                return null;
            };
            return executer.select(query, function, id);
        }
    }

    private <T> T getObject(ResultSet resultSet, Class<T> clazz) throws SQLException {
        T result = null;
        List<Field> listField =  ReflectionClass.getFields(clazz);
        while (resultSet.next()){
            result = ReflectionClass.instantiate(clazz);
            for (Field field : listField){
                Object fieldObject = null;
                if (field.getType().equals(boolean.class)) {
                    fieldObject = resultSet.getBoolean(field.getName());
                }
                else if (field.getType().equals(byte.class)) {
                    fieldObject = resultSet.getByte(field.getName());
                }
                else if (field.getType().equals(short.class)) {
                    fieldObject = resultSet.getShort(field.getName());
                }
                else if (field.getType().equals(int.class)) {
                    fieldObject = resultSet.getInt(field.getName());
                }
                else if (field.getType().equals(long.class)) {
                    fieldObject = resultSet.getLong(field.getName());
                }
                else if (field.getType().equals(float.class)) {
                    fieldObject = resultSet.getFloat(field.getName());
                }
                else if (field.getType().equals(double.class)) {
                    fieldObject = resultSet.getDouble(field.getName());
                }
                else if (field.getType().equals(String.class)) {
                    fieldObject = resultSet.getString(field.getName());
                } else {
                    fieldObject = resultSet.getObject(field.getName());
                }
                ReflectionClass.setFieldValue(result, field, fieldObject);
            }
        }
        return result;
    }
}
