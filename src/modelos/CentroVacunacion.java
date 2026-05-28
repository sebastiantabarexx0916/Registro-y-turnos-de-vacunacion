package modelos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CentroVacunacion {
    private int id;
    private String nombre;
    private String direccion;
    private String telefono;
    private int capacidadDiaria;
    private LocalTime horarioApertura;
    private LocalTime horarioCierre;
    private Map<String, Integer> stockVacunas; // Nombre de Vacuna -> Cantidad

    public CentroVacunacion() {
        this.stockVacunas = new HashMap<>();
    }

    public CentroVacunacion(int id, String nombre, String direccion, String telefono, int capacidadDiaria, LocalTime horarioApertura, LocalTime horarioCierre) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.capacidadDiaria = capacidadDiaria;
        this.horarioApertura = horarioApertura;
        this.horarioCierre = horarioCierre;
        this.stockVacunas = new HashMap<>();
    }

    public List<Turno> obtenerDisponibilidad(LocalDate fecha) {
        System.out.println("Verificando disponibilidad para el centro: " + this.nombre + " en fecha " + fecha);
        // Retornará una lista vacía por defecto; Main manejará la asignación real.
        return new ArrayList<>();
    }

    public int validarStock(Vacuna vacuna) {
        if (vacuna == null) return 0;
        return stockVacunas.getOrDefault(vacuna.getNombre(), 0);
    }

    public void agregarStock(Vacuna vacuna, int cantidad) {
        if (vacuna != null) {
            stockVacunas.put(vacuna.getNombre(), stockVacunas.getOrDefault(vacuna.getNombre(), 0) + cantidad);
        }
    }

    public boolean descontarStock(Vacuna vacuna, int cantidad) {
        if (vacuna == null) return false;
        int stockActual = validarStock(vacuna);
        if (stockActual >= cantidad) {
            stockVacunas.put(vacuna.getNombre(), stockActual - cantidad);
            return true;
        }
        return false;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public int getCapacidadDiaria() { return capacidadDiaria; }
    public void setCapacidadDiaria(int capacidadDiaria) { this.capacidadDiaria = capacidadDiaria; }

    public LocalTime getHorarioApertura() { return horarioApertura; }
    public void setHorarioApertura(LocalTime horarioApertura) { this.horarioApertura = horarioApertura; }

    public LocalTime getHorarioCierre() { return horarioCierre; }
    public void setHorarioCierre(LocalTime horarioCierre) { this.horarioCierre = horarioCierre; }

    public Map<String, Integer> getStockVacunas() { return stockVacunas; }
    public void setStockVacunas(Map<String, Integer> stockVacunas) { this.stockVacunas = stockVacunas; }
}
