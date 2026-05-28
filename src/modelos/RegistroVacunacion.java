package modelos;

import java.time.LocalDateTime;

public class RegistroVacunacion {
    private int id;
    private LocalDateTime fechaAplicacion;
    private String observacionesClinicas;
    private String vacunadorNombre;
    private String numeroLoteUsado;
    private int dosisAplicada;
    private Turno turno;
    private Paciente paciente;
    private EstadoVacunacion estado; // Enum

    public RegistroVacunacion() {}

    public RegistroVacunacion(int id, LocalDateTime fechaAplicacion, String observacionesClinicas, String vacunadorNombre, String numeroLoteUsado, int dosisAplicada, Turno turno, Paciente paciente, EstadoVacunacion estado) {
        this.id = id;
        this.fechaAplicacion = fechaAplicacion;
        this.observacionesClinicas = observacionesClinicas;
        this.vacunadorNombre = vacunadorNombre;
        this.numeroLoteUsado = numeroLoteUsado;
        this.dosisAplicada = dosisAplicada;
        this.turno = turno;
        this.paciente = paciente;
        this.estado = estado;
    }

    public void crearRegistro() {
        System.out.println("Registro de vacunación clínico creado exitosamente para el paciente: " + this.paciente.getNombre());
    }

    public boolean esInmutable() {
        // En base de datos o lógica de negocio, los registros son inmutables a menos que los anule el admin.
        // Si el estado es ANULADA, se considera inactivo.
        return this.estado != EstadoVacunacion.ANULADA;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDateTime getFechaAplicacion() { return fechaAplicacion; }
    public void setFechaAplicacion(LocalDateTime fechaAplicacion) { this.fechaAplicacion = fechaAplicacion; }

    public String getObservacionesClinicas() { return observacionesClinicas; }
    public void setObservacionesClinicas(String observacionesClinicas) { this.observacionesClinicas = observacionesClinicas; }

    public String getVacunadorNombre() { return vacunadorNombre; }
    public void setVacunadorNombre(String vacunadorNombre) { this.vacunadorNombre = vacunadorNombre; }

    public String getNumeroLoteUsado() { return numeroLoteUsado; }
    public void setNumeroLoteUsado(String numeroLoteUsado) { this.numeroLoteUsado = numeroLoteUsado; }

    public int getDosisAplicada() { return dosisAplicada; }
    public void setDosisAplicada(int dosisAplicada) { this.dosisAplicada = dosisAplicada; }

    public Turno getTurno() { return turno; }
    public void setTurno(Turno turno) { this.turno = turno; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public EstadoVacunacion getEstado() { return estado; }
    public void setEstado(EstadoVacunacion estado) { this.estado = estado; }
}
