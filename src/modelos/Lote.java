package modelos;

import java.time.LocalDate;

public class Lote {
    private int id;
    private String numeroLote;
    private LocalDate fechaFabricacion;
    private LocalDate fechaVencimiento;
    private int stockDisponible;
    private int stockInicial;
    private Vacuna vacuna;

    public Lote() {}

    public Lote(int id, String numeroLote, LocalDate fechaFabricacion, LocalDate fechaVencimiento, int stockInicial, Vacuna vacuna) {
        this.id = id;
        this.numeroLote = numeroLote;
        this.fechaFabricacion = fechaFabricacion;
        this.fechaVencimiento = fechaVencimiento;
        this.stockInicial = stockInicial;
        this.stockDisponible = stockInicial;
        this.vacuna = vacuna;
    }

    public boolean descontarStock(int cantidad) {
        if (this.stockDisponible >= cantidad) {
            this.stockDisponible -= cantidad;
            return true;
        }
        return false;
    }

    public void reponerStock(int cantidad) {
        this.stockDisponible += cantidad;
        this.stockInicial += cantidad;
        System.out.println("Lote " + this.numeroLote + " reabastecido con " + cantidad + " unidades.");
    }

    public boolean estaVencido() {
        return LocalDate.now().isAfter(this.fechaVencimiento);
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNumeroLote() { return numeroLote; }
    public void setNumeroLote(String numeroLote) { this.numeroLote = numeroLote; }

    public LocalDate getFechaFabricacion() { return fechaFabricacion; }
    public void setFechaFabricacion(LocalDate fechaFabricacion) { this.fechaFabricacion = fechaFabricacion; }

    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public int getStockDisponible() { return stockDisponible; }
    public void setStockDisponible(int stockDisponible) { this.stockDisponible = stockDisponible; }

    public int getStockInicial() { return stockInicial; }
    public void setStockInicial(int stockInicial) { this.stockInicial = stockInicial; }

    public Vacuna getVacuna() { return vacuna; }
    public void setVacuna(Vacuna vacuna) { this.vacuna = vacuna; }
}
