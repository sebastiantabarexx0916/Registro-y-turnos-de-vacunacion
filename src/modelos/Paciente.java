package modelos;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Paciente extends Usuario {
    private LocalDate fechaNacimiento;
    private String alergias;
    private String condicionesMedicas;

    public Paciente() {
        super();
    }

    public Paciente(int id, String nombre, String email, String contrasena, String rol, String cargo, String telefono, String cedula,
                    LocalDate fechaNacimiento, String alergias, String condicionesMedicas) {
        super(id, nombre, email, contrasena, rol, cargo, telefono, cedula);
        this.fechaNacimiento = fechaNacimiento;
        this.alergias = alergias;
        this.condicionesMedicas = condicionesMedicas;
    }

    public Turno solicitarTurno(Vacuna vacuna, CentroVacunacion centro) {
        // En un sistema en memoria, delegamos la creación a un método helper de Main que asigna ID único.
        // O lo creamos y lo añadimos a la lista en Main.
        Turno nuevoTurno = new Turno();
        nuevoTurno.setPaciente(this);
        nuevoTurno.setVacuna(vacuna);
        nuevoTurno.setCentro(centro);
        nuevoTurno.setFechaHora(LocalDateTime.now().plusDays(3)); // Por defecto a 3 días en el futuro
        nuevoTurno.setEstado("PENDIENTE");
        nuevoTurno.setCodigoConfirmacion("CONF-" + System.currentTimeMillis() % 100000);
        return nuevoTurno;
    }

    public boolean cancelarTurno(Turno turno) {
        if (turno != null && turno.getPaciente().getId() == this.id) {
            return turno.cancelar(24); // Cancela con 24 horas de anticipación simulada
        }
        return false;
    }

    public boolean reprogramarTurno(Turno turno, LocalDateTime nuevaFecha) {
        if (turno != null && turno.getPaciente().getId() == this.id) {
            return turno.reprogramar(nuevaFecha);
        }
        return false;
    }

    public String consultarEstadoTurno() {
        // Retorna un resumen de los turnos de este paciente
        return "Consulta de estado de turnos realizada para paciente: " + this.nombre;
    }

    public String verCarnetDigital() {
        // Devuelve el carnet digital de vacunación en formato texto
        return "Carnet Digital de Vacunación - Paciente: " + this.nombre + " [Cédula: " + this.cedula + "]";
    }

    // Getters and Setters
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getAlergias() { return alergias; }
    public void setAlergias(String alergias) { this.alergias = alergias; }

    public String getCondicionesMedicas() { return condicionesMedicas; }
    public void setCondicionesMedicas(String condicionesMedicas) { this.condicionesMedicas = condicionesMedicas; }
}
