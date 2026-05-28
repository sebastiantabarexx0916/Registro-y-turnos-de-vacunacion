package modelos;

import java.time.LocalDateTime;

public class Recepcionista extends Usuario {

    public Recepcionista() {
        super();
    }

    public Recepcionista(int id, String nombre, String email, String contrasena, String rol, String cargo, String telefono, String cedula) {
        super(id, nombre, email, contrasena, rol, cargo, telefono, cedula);
    }

    public Paciente registrarPacienteManual(Paciente datos) {
        System.out.println("Recepcionista " + this.nombre + " registró manualmente al paciente: " + datos.getNombre());
        return datos;
    }

    public Turno asignarTurnoManual(Paciente paciente, Vacuna vacuna, CentroVacunacion centro) {
        Turno nuevoTurno = new Turno();
        nuevoTurno.setPaciente(paciente);
        nuevoTurno.setVacuna(vacuna);
        nuevoTurno.setCentro(centro);
        nuevoTurno.setFechaHora(LocalDateTime.now().plusDays(1)); // Por defecto, mañana
        nuevoTurno.setEstado("PENDIENTE");
        nuevoTurno.setCodigoConfirmacion("CONF-MAN-" + System.currentTimeMillis() % 100000);
        System.out.println("Recepcionista " + this.nombre + " asignó turno manual ID " + nuevoTurno.getId() + " para el paciente " + paciente.getNombre());
        return nuevoTurno;
    }
}
