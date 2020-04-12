package ru.otus.jdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;

public interface IDBExecutor<T> {
    void formatDB(String query) throws SQLException;
    long insert(String query, List<Object> params) throws SQLException;
    boolean update(String query, List<Object> params, long id) throws SQLException;
    boolean delete(String query, long id) throws SQLException;
    <T> T select(String query, Function<ResultSet, T> function, long id) throws SQLException;

}
