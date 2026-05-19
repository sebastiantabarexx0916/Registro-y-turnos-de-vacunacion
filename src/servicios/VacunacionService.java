package servicios;

import conexion.DatabaseConnection;
import dao.LoteDAO;
import dao.VacunacionDAO;
import java.sql.*;
import modelos.Vacunacion;
import utilidades.ResultadoOperacion;

public class VacunacionService {
    private VacunacionDAO vacunacionDAO = new VacunacionDAO();
    private LoteDAO loteDAO = new LoteDAO();

    /**
     * Aplicar vacuna: realiza FEFO, descuenta stock atómico e inserta la vacunación en una transacción.
     */
    public ResultadoOperacion aplicarVacuna(int pacienteId, int vacunaId, int usuarioId, String observaciones) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // 1) seleccionar lote FEFO para esa vacuna
                String sql = "SELECT id FROM lotes WHERE vacuna_id = ? AND cantidad_disponible > 0 AND fecha_vencimiento >= CURDATE() ORDER BY fecha_vencimiento ASC LIMIT 1";
                Integer loteId = null;
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, vacunaId);
                    try (ResultSet rs = ps.executeQuery()) { if (rs.next()) loteId = rs.getInt("id"); }
                }

                if (loteId == null) {
                    conn.rollback();
                    return ResultadoOperacion.error("No hay lotes disponibles para la vacuna seleccionada");
                }

                // 2) descontar stock atómico
                boolean descontado = loteDAO.descontarStock(conn, loteId, 1);
                if (!descontado) {
                    conn.rollback();
                    return ResultadoOperacion.error("No fue posible descontar stock del lote");
                }

                // 3) insertar vacunación
                Vacunacion v = new Vacunacion();
                v.setPacienteId(pacienteId);
                v.setVacunaId(vacunaId);
                v.setLoteId(loteId);
                v.setNumeroDosis(1);
                v.setObservaciones(observaciones);
                v.setUsuarioId(usuarioId);
                v.setEstado("COMPLETADA");

                boolean inserted = vacunacionDAO.insertar(conn, v);
                if (!inserted) {
                    conn.rollback();
                    return ResultadoOperacion.error("Error al registrar la vacunación");
                }

                conn.commit();
                return ResultadoOperacion.success("Vacunación registrada", v);
            } catch (Exception e) {
                conn.rollback();
                return ResultadoOperacion.error("Error en transacción: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            return ResultadoOperacion.error("Error al conectar: " + e.getMessage());
        }
    }
}
