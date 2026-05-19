package dao;

import java.sql.*;
import java.util.*;
import modelos.Vacunacion;

public class VacunacionDAO {

    protected Connection getConnection() throws SQLException {
        return conexion.DatabaseConnection.getConnection();
    }

    protected Vacunacion mapResultSetToEntity(ResultSet rs) throws SQLException {
        Vacunacion v = new Vacunacion();
        v.setId(rs.getInt("id"));
        try {
            v.setPacienteId(rs.getInt("paciente_id"));
        } catch (SQLException ignore) {}
        try { v.setPacienteNombre(rs.getString("paciente_nombre")); } catch (SQLException ignore) {}
        try { v.setVacunaId(rs.getInt("vacuna_id")); } catch (SQLException ignore) {}
        try { v.setVacunaNombre(rs.getString("vacuna_nombre")); } catch (SQLException ignore) {}
        try { v.setLoteId(rs.getInt("lote_id")); } catch (SQLException ignore) {}
        try { v.setLoteCodigo(rs.getString("lote_codigo")); } catch (SQLException ignore) {}
        try {
            Timestamp ts = rs.getTimestamp("fecha_aplicacion");
            if (ts != null) v.setFechaAplicacion(ts.toLocalDateTime());
        } catch (SQLException ignore) {}
        try { v.setNumeroDosis(rs.getInt("numero_dosis")); } catch (SQLException ignore) {}
        try { v.setObservaciones(rs.getString("observaciones")); } catch (SQLException ignore) {}
        try { v.setUsuarioId(rs.getInt("usuario_id")); } catch (SQLException ignore) {}
        try { v.setUsuarioNombre(rs.getString("usuario_nombre")); } catch (SQLException ignore) {}
        try { v.setEstado(rs.getString("estado")); } catch (SQLException ignore) {}
        return v;
    }

    public List<Vacunacion> listarPorRangoFechas(java.time.LocalDate inicio, java.time.LocalDate fin) throws SQLException {
        List<Vacunacion> lista = new ArrayList<>();
        String sql = "SELECT v.*, CONCAT(p.nombre, ' ', p.apellido) as paciente_nombre, " +
                     "vac.nombre as vacuna_nombre, l.codigo_lote as lote_codigo, " +
                     "u.username as usuario_nombre FROM vacunaciones v " +
                     "JOIN pacientes p ON v.paciente_id = p.id " +
                     "JOIN vacunas vac ON v.vacuna_id = vac.id " +
                     "JOIN lotes l ON v.lote_id = l.id " +
                     "JOIN usuarios u ON v.usuario_id = u.id " +
                     "WHERE DATE(v.fecha_aplicacion) BETWEEN ? AND ? " +
                     "ORDER BY v.fecha_aplicacion DESC";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(inicio));
            ps.setDate(2, java.sql.Date.valueOf(fin));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSetToEntity(rs));
                }
            }
        }
        return lista;
    }

    public List<Vacunacion> listarPorPaciente(int pacienteId) throws SQLException {
        List<Vacunacion> lista = new ArrayList<>();
        String sql = "SELECT v.*, CONCAT(p.nombre, ' ', p.apellido) as paciente_nombre, " +
                     "vac.nombre as vacuna_nombre, l.codigo_lote as lote_codigo, " +
                     "u.username as usuario_nombre FROM vacunaciones v " +
                     "JOIN pacientes p ON v.paciente_id = p.id " +
                     "JOIN vacunas vac ON v.vacuna_id = vac.id " +
                     "JOIN lotes l ON v.lote_id = l.id " +
                     "JOIN usuarios u ON v.usuario_id = u.id " +
                     "WHERE v.paciente_id = ? ORDER BY v.fecha_aplicacion DESC";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pacienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapResultSetToEntity(rs));
            }
        }
        return lista;
    }

    public List<Map<String, Object>> contarPorVacuna() throws SQLException {
        List<Map<String, Object>> resultados = new ArrayList<>();
        String sql = "SELECT vac.nombre as vacuna, COUNT(*) as total " +
                     "FROM vacunaciones v JOIN vacunas vac ON v.vacuna_id = vac.id " +
                     "GROUP BY vac.id, vac.nombre ORDER BY total DESC";

        try (Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("vacuna", rs.getString("vacuna"));
                row.put("total", rs.getInt("total"));
                resultados.add(row);
            }
        }
        return resultados;
    }

    public List<Map<String, Object>> contarPorRangoEdad() throws SQLException {
        List<Map<String, Object>> resultados = new ArrayList<>();
        String sql = "SELECT " +
                     "CASE " +
                     "  WHEN TIMESTAMPDIFF(YEAR, p.fecha_nacimiento, CURDATE()) < 1 THEN '0-1 años' " +
                     "  WHEN TIMESTAMPDIFF(YEAR, p.fecha_nacimiento, CURDATE()) < 5 THEN '1-5 años' " +
                     "  WHEN TIMESTAMPDIFF(YEAR, p.fecha_nacimiento, CURDATE()) < 12 THEN '5-12 años' " +
                     "  WHEN TIMESTAMPDIFF(YEAR, p.fecha_nacimiento, CURDATE()) < 18 THEN '12-18 años' " +
                     "  WHEN TIMESTAMPDIFF(YEAR, p.fecha_nacimiento, CURDATE()) < 60 THEN '18-60 años' " +
                     "  ELSE '60+ años' " +
                     "END as rango, " +
                     "COUNT(*) as total " +
                     "FROM vacunaciones v " +
                     "JOIN pacientes p ON v.paciente_id = p.id " +
                     "GROUP BY rango ORDER BY MIN(TIMESTAMPDIFF(YEAR, p.fecha_nacimiento, CURDATE()))";

        try (Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("rango", rs.getString("rango"));
                row.put("total", rs.getInt("total"));
                resultados.add(row);
            }
        }
        return resultados;
    }

    /**
     * Insertar una vacunación usando la Connection proporcionada (transaccional).
     */
    public boolean insertar(Connection conn, modelos.Vacunacion v) throws SQLException {
        String sql = "INSERT INTO vacunaciones (paciente_id, vacuna_id, lote_id, fecha_aplicacion, numero_dosis, observaciones, usuario_id, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, v.getPacienteId());
            ps.setInt(2, v.getVacunaId());
            ps.setInt(3, v.getLoteId());
            if (v.getFechaAplicacion() != null) ps.setTimestamp(4, Timestamp.valueOf(v.getFechaAplicacion())); else ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            ps.setInt(5, v.getNumeroDosis());
            ps.setString(6, v.getObservaciones());
            ps.setInt(7, v.getUsuarioId());
            ps.setString(8, v.getEstado() == null ? "COMPLETADA" : v.getEstado());
            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) v.setId(rs.getInt(1));
                }
                return true;
            }
            return false;
        }
    }
}
