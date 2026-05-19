package dao;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractDAO<T> {
    protected Connection getConnection() throws SQLException {
        return conexion.DatabaseConnection.getConnection();
    }

    protected abstract T mapResultSetToEntity(java.sql.ResultSet rs) throws java.sql.SQLException;
}
