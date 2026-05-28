package modelos;

import java.time.LocalDateTime;

public class Notificacion {
    private int id;
    private String tipo; // EMAIL, SMS, PUSH
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private String estado; // ENVIADO, ERROR, REINTENTANDO
    private String destinatario;

    public Notificacion() {}

    public Notificacion(int id, String tipo, String mensaje, String destinatario) {
        this.id = id;
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.fechaEnvio = LocalDateTime.now();
        this.estado = "ENVIADO";
        this.destinatario = destinatario;
    }

    public boolean enviar() {
        this.estado = "ENVIADO";
        System.out.println("----------------------------------------------------------------");
        System.out.println("[NOTIFICACIÓN - " + this.tipo + "] Destinatario: " + this.destinatario);
        System.out.println("Mensaje: " + this.mensaje);
        System.out.println("----------------------------------------------------------------");
        return true;
    }

    public void reintentar() {
        this.estado = "REINTENTANDO";
        System.out.println("Reintentando envío de notificación ID " + this.id + " para " + this.destinatario);
        enviar();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getDestinatario() { return destinatario; }
    public void setDestinatario(String destinatario) { this.destinatario = destinatario; }
}
