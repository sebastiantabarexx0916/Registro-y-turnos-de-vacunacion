package modelos;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Turno {
    private int id;
    private LocalDateTime fechaHora;
    private String estado; // PENDIENTE, CONFIRMADO, CANCELADO, COMPLETADO, AUSENTE, BLOQUEADO
    private String codigoConfirmacion;
    private Paciente paciente;
    private Vacuna vacuna;
    private CentroVacunacion centro;

    public Turno() {}

    public Turno(int id, LocalDateTime fechaHora, String estado, String codigoConfirmacion, Paciente paciente, Vacuna vacuna, CentroVacunacion centro) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.estado = estado;
        this.codigoConfirmacion = codigoConfirmacion;
        this.paciente = paciente;
        this.vacuna = vacuna;
        this.centro = centro;
    }

    public boolean confirmar() {
        if ("PENDIENTE".equals(this.estado)) {
            this.estado = "CONFIRMADO";
            System.out.println("Turno ID " + this.id + " confirmado exitosamente.");
            return true;
        }
        return false;
    }

    public boolean cancelar(int anticipacionHoras) {
        // En una app de consola en memoria, simulamos la cancelación.
        // Si hay suficiente anticipación desde la hora actual a la fecha del turno:
        long horasDiferencia = ChronoUnit.HOURS.between(LocalDateTime.now(), this.fechaHora);
        if (horasDiferencia >= anticipacionHoras) {
            this.estado = "CANCELADO";
            System.out.println("Turno ID " + this.id + " cancelado. Reembolsando cupo...");
            return true;
        } else {
            System.out.println("No se puede cancelar el turno: falta menos de " + anticipacionHoras + " horas.");
            return false;
        }
    }

    public boolean reprogramar(LocalDateTime nuevaFecha) {
        if (verificarAnticipacionMinima()) {
            this.fechaHora = nuevaFecha;
            this.estado = "PENDIENTE"; // Vuelve a pendiente de confirmación
            System.out.println("Turno ID " + this.id + " reprogramado para la fecha: " + nuevaFecha);
            return true;
        }
        System.out.println("No se puede reprogramar: falta menos de 24 horas para el turno.");
        return false;
    }

    public boolean verificarAnticipacionMinima() {
        // Verifica si falta más de 24 horas para la cita
        long horasDiferencia = ChronoUnit.HOURS.between(LocalDateTime.now(), this.fechaHora);
        return horasDiferencia >= 24;
    }

    public String obtenerCodigoQR() {
        return "[QR-CODE: Turno ID: " + this.id + " | Paciente: " + (paciente != null ? paciente.getNombre() : "N/A") +
               " | Vacuna: " + (vacuna != null ? vacuna.getNombre() : "N/A") + " | Fecha: " + this.fechaHora + "]";
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getCodigoConfirmacion() { return codigoConfirmacion; }
    public void setCodigoConfirmacion(String codigoConfirmacion) { this.codigoConfirmacion = codigoConfirmacion; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public Vacuna getVacuna() { return vacuna; }
    public void setVacuna(Vacuna vacuna) { this.vacuna = vacuna; }

    public CentroVacunacion getCentro() { return centro; }
    public void setCentro(CentroVacunacion centro) { this.centro = centro; }
}
