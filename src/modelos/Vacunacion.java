package modelos;

import java.time.LocalDateTime;

public class Vacunacion {
    private int id;
    private int pacienteId;
    private String pacienteNombre;
    private int vacunaId;
    private String vacunaNombre;
    private int loteId;
    private String loteCodigo;
    private LocalDateTime fechaAplicacion;
    private int numeroDosis;
    private String observaciones;
    private int usuarioId;
    private String usuarioNombre;
    private String estado;

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
    public int getLoteId() { return loteId; }
    public void setLoteId(int loteId) { this.loteId = loteId; }
    public String getLoteCodigo() { return loteCodigo; }
    public void setLoteCodigo(String loteCodigo) { this.loteCodigo = loteCodigo; }
    public LocalDateTime getFechaAplicacion() { return fechaAplicacion; }
    public void setFechaAplicacion(LocalDateTime fechaAplicacion) { this.fechaAplicacion = fechaAplicacion; }
    public int getNumeroDosis() { return numeroDosis; }
    public void setNumeroDosis(int numeroDosis) { this.numeroDosis = numeroDosis; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
