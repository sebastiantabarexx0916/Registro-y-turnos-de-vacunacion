package dao;

import java.sql.*;
import java.util.*;
import modelos.Turno;

public class TurnoDAO extends AbstractDAO<Turno> {
    @Override
    protected Turno mapResultSetToEntity(ResultSet rs) throws SQLException {
        Turno t = new Turno();
        t.setId(rs.getInt("id"));
        t.setPacienteId(rs.getInt("paciente_id"));
        t.setVacunaId(rs.getInt("vacuna_id"));
        Timestamp ts = rs.getTimestamp("fecha_turno");
        if (ts != null) t.setFechaTurno(ts.toLocalDateTime());
        t.setEstado(rs.getString("estado"));
        t.setObservaciones(rs.getString("observaciones"));
        return t;
    }

    public List<Turno> listarPendientes() throws SQLException {
        List<Turno> lista = new ArrayList<>();
        String sql = "SELECT * FROM turnos WHERE estado = 'PENDIENTE' ORDER BY fecha_turno";
        try (Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapResultSetToEntity(rs));
        }
        return lista;
    }

    public Turno buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM turnos WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return mapResultSetToEntity(rs); }
        }
        return null;
    }

    public boolean actualizarEstado(int id, String estado) throws SQLException {
        String sql = "UPDATE turnos SET estado = ? WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }
}
