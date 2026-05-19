package modelos;

public class Vacuna {
    private int id;
    private String nombre;
    private String descripcion;
    private int dosisRequeridas;
    private int edadMinimaMeses;
    private boolean activa;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getDosisRequeridas() { return dosisRequeridas; }
    public void setDosisRequeridas(int dosisRequeridas) { this.dosisRequeridas = dosisRequeridas; }
    public int getEdadMinimaMeses() { return edadMinimaMeses; }
    public void setEdadMinimaMeses(int edadMinimaMeses) { this.edadMinimaMeses = edadMinimaMeses; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
}
