package modelos;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public class Reporte {
    private int id;
    private String nombre;
    private LocalDateTime fechaGeneracion;
    private String formato;
    private String usuarioGenero;
    private String contenido;

    public Reporte() {}

    public Reporte(int id, String nombre, String formato, String usuarioGenero, String contenido) {
        this.id = id;
        this.nombre = nombre;
        this.fechaGeneracion = LocalDateTime.now();
        this.formato = formato;
        this.usuarioGenero = usuarioGenero;
        this.contenido = contenido;
    }

    public String exportarPDF() {
        this.formato = "PDF";
        String fileName = "Reporte_" + this.id + ".txt"; // Guardado como txt para simulación limpia en consola
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("==================================================\n");
            writer.write("           REPORTE DE VACUNACIÓN (PDF SIMULADO)   \n");
            writer.write("==================================================\n");
            writer.write("Reporte ID: " + this.id + "\n");
            writer.write("Nombre: " + this.nombre + "\n");
            writer.write("Generado por: " + this.usuarioGenero + "\n");
            writer.write("Fecha: " + this.fechaGeneracion + "\n");
            writer.write("Contenido:\n" + this.contenido + "\n");
            writer.write("==================================================\n");
            System.out.println("Reporte exportado como PDF simulado en: " + fileName);
        } catch (IOException e) {
            System.out.println("Error al exportar PDF: " + e.getMessage());
        }
        return fileName;
    }

    public String exportarCSV() {
        this.formato = "CSV";
        String fileName = "Reporte_" + this.id + ".csv";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("ID,Nombre,GeneradoPor,Fecha,Contenido\n");
            writer.write(this.id + "," + this.nombre + "," + this.usuarioGenero + "," + this.fechaGeneracion + "," + this.contenido.replace(",", " ") + "\n");
            System.out.println("Reporte exportado como CSV en: " + fileName);
        } catch (IOException e) {
            System.out.println("Error al exportar CSV: " + e.getMessage());
        }
        return fileName;
    }

    public void aplicarFiltros(Map<String, String> filtros) {
        System.out.println("Filtros aplicados al reporte ID " + this.id + ": " + filtros);
        this.contenido += " | Filtros aplicados: " + filtros.toString();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDateTime getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(LocalDateTime fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }

    public String getFormato() { return formato; }
    public void setFormato(String formato) { this.formato = formato; }

    public String getUsuarioGenero() { return usuarioGenero; }
    public void setUsuarioGenero(String usuarioGenero) { this.usuarioGenero = usuarioGenero; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
}
