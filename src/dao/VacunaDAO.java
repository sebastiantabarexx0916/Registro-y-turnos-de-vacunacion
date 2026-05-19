package dao;

import java.sql.*;
import java.util.*;
import modelos.Vacuna;

public class VacunaDAO extends AbstractDAO<Vacuna> {
    @Override
    protected Vacuna mapResultSetToEntity(ResultSet rs) throws SQLException {
        Vacuna v = new Vacuna();
        v.setId(rs.getInt("id"));
        v.setNombre(rs.getString("nombre"));
        v.setDescripcion(rs.getString("descripcion"));
        v.setDosisRequeridas(rs.getInt("dosis_requeridas"));
        v.setEdadMinimaMeses(rs.getInt("edad_minima_meses"));
        v.setActiva(rs.getBoolean("activa"));
        return v;
    }

    public List<Vacuna> listarTodos() throws SQLException {
        List<Vacuna> lista = new ArrayList<>();
        String sql = "SELECT * FROM vacunas ORDER BY nombre";
        try (Connection conn = getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapResultSetToEntity(rs));
        }
        return lista;
    }
}
