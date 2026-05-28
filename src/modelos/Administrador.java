package modelos;

import java.util.Map;
import java.time.LocalDateTime;

public class Administrador extends Usuario {

    public Administrador() {
        super();
    }

    public Administrador(int id, String nombre, String email, String contrasena, String rol, String cargo, String telefono, String cedula) {
        super(id, nombre, email, contrasena, rol, cargo, telefono, cedula);
    }

    public void gestionarUsuarios(Usuario usuario, String accion) {
        System.out.println("[AUDITORÍA] Admin " + this.nombre + " realizó acción: '" + accion + "' sobre el usuario " + usuario.getNombre());
    }

    public void gestionarVacunas(Vacuna vacuna) {
        System.out.println("[AUDITORÍA] Admin " + this.nombre + " gestionó la vacuna: " + vacuna.getNombre());
    }

    public void gestionarLotes(Lote lote) {
        System.out.println("[AUDITORÍA] Admin " + this.nombre + " gestionó el lote: " + lote.getNumeroLote());
    }

    public void gestionarCentros(CentroVacunacion centro) {
        System.out.println("[AUDITORÍA] Admin " + this.nombre + " gestionó el centro de vacunación: " + centro.getNombre());
    }

    public void gestionarInventario(CentroVacunacion centro) {
        System.out.println("[AUDITORÍA] Admin " + this.nombre + " actualizó el inventario del centro: " + centro.getNombre());
    }

    public Reporte generarReportes(String tipo, Map<String, String> filtros) {
        Reporte reporte = new Reporte();
        reporte.setId((int) (System.currentTimeMillis() % 1000));
        reporte.setNombre("Reporte de Vacunacion - " + tipo);
        reporte.setFechaGeneracion(LocalDateTime.now());
        reporte.setFormato("Texto/PDF/CSV Simulado");
        reporte.setUsuarioGenero(this.nombre);
        reporte.setContenido("Contenido del reporte tipo " + tipo + " generado con filtros " + filtros);
        System.out.println("[AUDITORÍA] Admin " + this.nombre + " generó un reporte de tipo: " + tipo);
        return reporte;
    }

    public String consultarAuditoria(Map<String, String> filtros) {
        return "[AUDITORÍA - Filtros: " + filtros + "] Log de Auditoría consultado por Admin " + this.nombre;
    }

    public void anularRegistroVacunacion(RegistroVacunacion registro, String justificacion) {
        if (registro != null) {
            registro.setEstado(EstadoVacunacion.ANULADA);
            registro.setObservacionesClinicas(registro.getObservacionesClinicas() + " [ANULADO por admin. Justificación: " + justificacion + "]");
            System.out.println("[AUDITORÍA] Admin " + this.nombre + " ANULÓ el registro de vacunación ID " + registro.getId());
        }
    }
}
