package modelos;

import java.time.LocalDateTime;

public class Turno {
    private int id;
    private int pacienteId;
    private String pacienteNombre;
    private int vacunaId;
    private String vacunaNombre;
    private LocalDateTime fechaTurno;
    private String estado;
    private String observaciones;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPacienteId() { return pacienteId; }
    public void setPacienteId(int pacienteId) { this.pacienteId = pacienteId; }
    public String getPacienteNombre() { return pacienteNombre; }
    public void setPacienteNombre(String pacienteNombre) { this.pacienteNombre = pacienteNombre; }
    public int getVacunaId() { return vacunaId; }
    public void setVacunaId(int vacunaId) { this.vacunaId = vacunaId; }
    public String getVacunaNombre() { return vacunaNombre; }
    public void setVacunaNombre(String vacunaNombre) { this.vacunaNombre = vacunaNombre; }
    public LocalDateTime getFechaTurno() { return fechaTurno; }
    public void setFechaTurno(LocalDateTime fechaTurno) { this.fechaTurno = fechaTurno; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
