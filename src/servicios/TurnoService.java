package servicios;

import dao.TurnoDAO;
import modelos.Turno;
import utilidades.ResultadoOperacion;

public class TurnoService {
    private TurnoDAO turnoDAO = new TurnoDAO();
    private VacunacionService vacunacionService = new VacunacionService();

    public ResultadoOperacion confirmarTurno(int turnoId, int usuarioId) {
        try {
            Turno t = turnoDAO.buscarPorId(turnoId);
            if (t == null) return ResultadoOperacion.error("Turno no encontrado");

            ResultadoOperacion r = vacunacionService.aplicarVacuna(t.getPacienteId(), t.getVacunaId(), usuarioId, "Desde turno " + turnoId);
            if (r.isSuccess()) {
                turnoDAO.actualizarEstado(turnoId, "COMPLETADO");
                return ResultadoOperacion.success("Turno confirmado y vacunación aplicada");
            }
            return r;
        } catch (Exception e) {
            return ResultadoOperacion.error("Error: " + e.getMessage());
        }
    }
}
