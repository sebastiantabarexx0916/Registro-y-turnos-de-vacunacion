package modelos;

import java.time.LocalDate;

public class Lote {
    private int id;
    private int vacunaId;
    private String vacunaNombre;
    private String codigoLote;
    private LocalDate fechaVencimiento;
    private int cantidad;
    private int cantidadDisponible;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getVacunaId() { return vacunaId; }
    public void setVacunaId(int vacunaId) { this.vacunaId = vacunaId; }
    public String getVacunaNombre() { return vacunaNombre; }
    public void setVacunaNombre(String vacunaNombre) { this.vacunaNombre = vacunaNombre; }
    public String getCodigoLote() { return codigoLote; }
    public void setCodigoLote(String codigoLote) { this.codigoLote = codigoLote; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public int getCantidadDisponible() { return cantidadDisponible; }
    public void setCantidadDisponible(int cantidadDisponible) { this.cantidadDisponible = cantidadDisponible; }
}
