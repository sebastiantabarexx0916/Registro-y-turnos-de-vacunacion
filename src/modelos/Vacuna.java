package modelos;

public class Vacuna {
    private int id;
    private String nombre;
    private String laboratorio;
    private String enfermedadPreviene;
    private int dosisRequeridas;
    private int intervaloDias;

    public Vacuna() {}

    public Vacuna(int id, String nombre, String laboratorio, String enfermedadPreviene, int dosisRequeridas, int intervaloDias) {
        this.id = id;
        this.nombre = nombre;
        this.laboratorio = laboratorio;
        this.enfermedadPreviene = enfermedadPreviene;
        this.dosisRequeridas = dosisRequeridas;
        this.intervaloDias = intervaloDias;
    }

    public String obtenerEsquemaCompleto() {
        return "Vacuna: " + this.nombre + " (" + this.laboratorio + ") | Previene: " + this.enfermedadPreviene +
               " | Dosis Requeridas: " + this.dosisRequeridas + " | Intervalo: " + this.intervaloDias + " días.";
    }

    public boolean validarEdadAplicacion(int edadAnios) {
        // Validación simplificada: por ejemplo, la vacuna de COVID es para mayores de 3 años, etc.
        if (this.nombre.toLowerCase().contains("covid")) {
            return edadAnios >= 3;
        }
        return edadAnios >= 0; // Por defecto apta para cualquier edad
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getLaboratorio() { return laboratorio; }
    public void setLaboratorio(String laboratorio) { this.laboratorio = laboratorio; }

    public String getEnfermedadPreviene() { return enfermedadPreviene; }
    public void setEnfermedadPreviene(String enfermedadPreviene) { this.enfermedadPreviene = enfermedadPreviene; }

    public int getDosisRequeridas() { return dosisRequeridas; }
    public void setDosisRequeridas(int dosisRequeridas) { this.dosisRequeridas = dosisRequeridas; }

    public int getIntervaloDias() { return intervaloDias; }
    public void setIntervaloDias(int intervaloDias) { this.intervaloDias = intervaloDias; }
}
