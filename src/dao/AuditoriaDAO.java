package dao;

import conexion.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuditoriaDAO {
    public void registrar(String usuario, String accion, String tabla, String detalles) {
        String sql = "INSERT INTO auditoria (usuario, accion, tabla_afectada, detalles) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, accion);
            ps.setString(3, tabla);
            ps.setString(4, detalles);
            ps.executeUpdate();
        } catch (SQLException e) {
            // swallow for now
        }
    }
}
