package dao;

import java.sql.*;
import java.util.*;
import modelos.Lote;

public class LoteDAO {

    protected Connection getConnection() throws SQLException {
        return conexion.DatabaseConnection.getConnection();
    }

    protected Lote mapResultSetToEntity(ResultSet rs) throws SQLException {
        Lote l = new Lote();
        l.setId(rs.getInt("id"));
        try { l.setVacunaId(rs.getInt("vacuna_id")); } catch (SQLException ignore) {}
        try { l.setVacunaNombre(rs.getString("vacuna_nombre")); } catch (SQLException ignore) {}
        try { l.setCodigoLote(rs.getString("codigo_lote")); } catch (SQLException ignore) {}
        try { java.sql.Date d = rs.getDate("fecha_vencimiento"); if (d != null) l.setFechaVencimiento(d.toLocalDate()); } catch (SQLException ignore) {}
        try { l.setCantidad(rs.getInt("cantidad")); } catch (SQLException ignore) {}
        try { l.setCantidadDisponible(rs.getInt("cantidad_disponible")); } catch (SQLException ignore) {}
        return l;
    }

    public List<Lote> listarProximosAVencer(int dias) throws SQLException {
        List<Lote> lista = new ArrayList<>();
        String sql = "SELECT l.*, v.nombre as vacuna_nombre FROM lotes l " +
                     "JOIN vacunas v ON l.vacuna_id = v.id " +
                     "WHERE l.fecha_vencimiento BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL ? DAY) " +
                     "AND l.cantidad_disponible > 0 " +
                     "ORDER BY l.fecha_vencimiento ASC";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dias);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapResultSetToEntity(rs));
            }
        }
        return lista;
    }

    /**
     * Descontar stock de forma atómica usando la Connection existente.
     */
    public boolean descontarStock(Connection conn, int loteId, int cantidad) throws SQLException {
        String sql = "UPDATE lotes SET cantidad_disponible = cantidad_disponible - ? WHERE id = ? AND cantidad_disponible >= ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, loteId);
            ps.setInt(3, cantidad);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }
}
