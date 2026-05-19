package dao;

import java.sql.*;
import java.util.*;
import modelos.Paciente;

public class PacienteDAO extends AbstractDAO<Paciente> {

    @Override
    protected Paciente mapResultSetToEntity(ResultSet rs) throws SQLException {
        Paciente p = new Paciente();
        p.setId(rs.getInt("id"));
        p.setNombre(rs.getString("nombre"));
        p.setApellido(rs.getString("apellido"));
        p.setDni(rs.getString("dni"));
        java.sql.Date d = rs.getDate("fecha_nacimiento");
        if (d != null) p.setFechaNacimiento(d.toLocalDate());
        p.setTelefono(rs.getString("telefono"));
        p.setEmail(rs.getString("email"));
        p.setDireccion(rs.getString("direccion"));
        return p;
    }

    public List<Paciente> listarTodos() throws SQLException {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM pacientes ORDER BY apellido, nombre";
        try (Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapResultSetToEntity(rs));
        }
        return lista;
    }

    public Paciente buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM pacientes WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapResultSetToEntity(rs);
            }
        }
        return null;
    }
}
