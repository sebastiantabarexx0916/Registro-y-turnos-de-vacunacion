package controladores;

import vistas.PacienteView;
import dao.PacienteDAO;
import utilidades.NotificationHelper;

public class PacienteController {
    private PacienteView view;
    private PacienteDAO dao = new PacienteDAO();

    public PacienteController(PacienteView view) {
        this.view = view;
        init();
    }

    private void init() {
        try {
            view.setPacientes(dao.listarTodos());
        } catch (Exception e) {
            NotificationHelper.showError("Error cargando pacientes: " + e.getMessage());
        }
    }
}
