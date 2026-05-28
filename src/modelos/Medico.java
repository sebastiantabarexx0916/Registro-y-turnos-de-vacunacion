package modelos;

public class Medico extends Usuario {
    private String matricula;
    private String especialidad;

    public Medico() {
        super();
    }

    public Medico(int id, String nombre, String email, String contrasena, String rol, String cargo, String telefono, String cedula,
                  String matricula, String especialidad) {
        super(id, nombre, email, contrasena, rol, cargo, telefono, cedula);
        this.matricula = matricula;
        this.especialidad = especialidad;
    }

    public void registrarContraindicacion(Paciente paciente, Vacuna vacuna, String motivo) {
        String contraindicacion = "Contraindicación para vacuna: " + vacuna.getNombre() + ". Motivo: " + motivo;
        if (paciente.getCondicionesMedicas() == null || paciente.getCondicionesMedicas().isEmpty()) {
            paciente.setCondicionesMedicas(contraindicacion);
        } else {
            paciente.setCondicionesMedicas(paciente.getCondicionesMedicas() + " | " + contraindicacion);
        }
        System.out.println("Médico " + this.nombre + " registró contraindicación para paciente " + paciente.getNombre());
    }

    public void bloquearTurno(Turno turno, String justificacion) {
        if (turno != null) {
            turno.setEstado("BLOQUEADO");
            System.out.println("Médico " + this.nombre + " bloqueó el turno ID " + turno.getId() + ". Justificación: " + justificacion);
        }
    }

    // Getters and Setters
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
}
