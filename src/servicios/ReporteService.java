package servicios;

import dao.LoteDAO;
import dao.VacunacionDAO;
import dao.PacienteDAO;
import modelos.Vacunacion;
import modelos.Lote;
import modelos.Paciente;
import utilidades.ResultadoOperacion;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.*;
import java.nio.file.*;

public class ReporteService {
    private VacunacionDAO vacunacionDAO;
    private PacienteDAO pacienteDAO;

    public ReporteService() {
        this.vacunacionDAO = new VacunacionDAO();
        this.pacienteDAO = new PacienteDAO();
    }

    public ResultadoOperacion reporteVacunacionesPorFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        try {
            List<Vacunacion> vacunaciones = vacunacionDAO.listarPorRangoFechas(fechaInicio, fechaFin);
            Map<String, Object> reporte = new HashMap<>();
            reporte.put("titulo", "Reporte de Vacunaciones");
            reporte.put("fechaInicio", fechaInicio);
            reporte.put("fechaFin", fechaFin);
            reporte.put("total", vacunaciones.size());
            reporte.put("datos", vacunaciones);
            reporte.put("generado", LocalDate.now());

            return ResultadoOperacion.success("Reporte generado", reporte);
        } catch (Exception e) {
            return ResultadoOperacion.error("Error al generar reporte: " + e.getMessage());
        }
    }

    public ResultadoOperacion reporteVacunacionesPorPaciente(int pacienteId) {
        try {
            Paciente paciente = pacienteDAO.buscarPorId(pacienteId);
            if (paciente == null) {
                return ResultadoOperacion.error("Paciente no encontrado");
            }

            List<Vacunacion> vacunaciones = vacunacionDAO.listarPorPaciente(pacienteId);
            Map<String, Object> reporte = new HashMap<>();
            reporte.put("titulo", "Historial de Vacunación");
            reporte.put("paciente", paciente);
            reporte.put("total", vacunaciones.size());
            reporte.put("datos", vacunaciones);
            reporte.put("generado", LocalDate.now());

            return ResultadoOperacion.success("Reporte generado", reporte);
        } catch (Exception e) {
            return ResultadoOperacion.error("Error al generar reporte: " + e.getMessage());
        }
    }

    public ResultadoOperacion reporteVacunasMasAplicadas() {
        try {
            List<Map<String, Object>> estadisticas = vacunacionDAO.contarPorVacuna();
            Map<String, Object> reporte = new HashMap<>();
            reporte.put("titulo", "Vacunas Más Aplicadas");
            reporte.put("datos", estadisticas);
            reporte.put("generado", LocalDate.now());

            return ResultadoOperacion.success("Reporte generado", reporte);
        } catch (Exception e) {
            return ResultadoOperacion.error("Error al generar reporte: " + e.getMessage());
        }
    }

    public ResultadoOperacion reporteCoberturaPorEdad() {
        try {
            List<Map<String, Object>> cobertura = vacunacionDAO.contarPorRangoEdad();
            Map<String, Object> reporte = new HashMap<>();
            reporte.put("titulo", "Cobertura por Rango de Edad");
            reporte.put("datos", cobertura);
            reporte.put("generado", LocalDate.now());

            return ResultadoOperacion.success("Reporte generado", reporte);
        } catch (Exception e) {
            return ResultadoOperacion.error("Error al generar reporte: " + e.getMessage());
        }
    }

    public ResultadoOperacion reporteLotesProximosAVencer(int dias) {
        try {
            LoteDAO loteDAO = new LoteDAO();
            List<Lote> lotes = loteDAO.listarProximosAVencer(dias);
            Map<String, Object> reporte = new HashMap<>();
            reporte.put("titulo", "Lotes Próximos a Vencer");
            reporte.put("dias", dias);
            reporte.put("total", lotes.size());
            reporte.put("datos", lotes);
            reporte.put("generado", LocalDate.now());

            return ResultadoOperacion.success("Reporte generado", reporte);
        } catch (Exception e) {
            return ResultadoOperacion.error("Error al generar reporte: " + e.getMessage());
        }
    }

    public ResultadoOperacion exportarACSV(List<Vacunacion> vacunaciones, String nombreArchivo) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            StringBuilder sb = new StringBuilder();

            sb.append("ID,Paciente,Vacuna,Lote,Fecha,Dosis,Usuario,Estado\n");

            for (Vacunacion v : vacunaciones) {
                sb.append(v.getId()).append(",");
                sb.append(escapeCSV(v.getPacienteNombre())).append(",");
                sb.append(escapeCSV(v.getVacunaNombre())).append(",");
                sb.append(escapeCSV(v.getLoteCodigo())).append(",");
                if (v.getFechaAplicacion() != null) {
                    sb.append(v.getFechaAplicacion().format(formatter)).append(",");
                } else {
                    sb.append(",");
                }
                sb.append(v.getNumeroDosis()).append(",");
                sb.append(escapeCSV(v.getUsuarioNombre())).append(",");
                sb.append(v.getEstado()).append("\n");
            }

            String directorio = System.getProperty("user.home") + "/Desktop/Reportes/";
            Files.createDirectories(Paths.get(directorio));
            String rutaCompleta = directorio + nombreArchivo + ".csv";
            Files.write(Paths.get(rutaCompleta), sb.toString().getBytes());

            return ResultadoOperacion.success("Exportado a: " + rutaCompleta);
        } catch (Exception e) {
            return ResultadoOperacion.error("Error al exportar: " + e.getMessage());
        }
    }

    private String escapeCSV(String valor) {
        if (valor == null) return "";
        if (valor.contains(",") || valor.contains("\"")) {
            return "\"" + valor.replace("\"", "\"\"") + "\"";
        }
        return valor;
    }
}
