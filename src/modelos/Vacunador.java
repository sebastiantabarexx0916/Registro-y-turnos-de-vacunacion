package modelos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Vacunador extends Usuario {

    public Vacunador() {
        super();
    }

    public Vacunador(int id, String nombre, String email, String contrasena, String rol, String cargo, String telefono, String cedula) {
        super(id, nombre, email, contrasena, rol, cargo, telefono, cedula);
    }

    public RegistroVacunacion registrarAplicacion(Turno turno, Lote lote, String observaciones) {
        if (turno == null || lote == null) {
            System.out.println("Error: Turno o Lote nulo.");
            return null;
        }

        if (lote.estaVencido()) {
            System.out.println("Error: El lote de vacuna está vencido.");
            return null;
        }

        boolean stockDescontado = lote.descontarStock(1);
        if (!stockDescontado) {
            System.out.println("Error: No hay stock disponible en el lote.");
            return null;
        }

        turno.setEstado("COMPLETADO");

        RegistroVacunacion registro = new RegistroVacunacion();
        registro.setId((int) (System.currentTimeMillis() % 1000));
        registro.setFechaAplicacion(LocalDateTime.now());
        registro.setObservacionesClinicas(observaciones);
        registro.setVacunadorNombre(this.nombre);
        registro.setNumeroLoteUsado(lote.getNumeroLote());
        registro.setDosisAplicada(1);
        registro.setTurno(turno);
        registro.setPaciente(turno.getPaciente());
        registro.setEstado(EstadoVacunacion.COMPLETADA);

        System.out.println("Vacunador " + this.nombre + " registró la aplicación de " + turno.getVacuna().getNombre() + " al paciente " + turno.getPaciente().getNombre());
        return registro;
    }

    public void marcarAusente(Turno turno) {
        if (turno != null) {
            turno.setEstado("AUSENTE");
            System.out.println("Vacunador " + this.nombre + " marcó el turno ID " + turno.getId() + " como AUSENTE.");
        }
    }

    public List<Turno> verTurnosDelDia(LocalDate fecha) {
        // En memoria, este método recibirá los turnos desde el Main y filtrará los de la fecha.
        // Aquí simulamos que se ejecuta y se retorna una lista.
        System.out.println("Vacunador " + this.nombre + " consultando turnos para el día: " + fecha);
        return new ArrayList<>();
    }
}
