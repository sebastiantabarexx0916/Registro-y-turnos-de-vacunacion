package main;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private static final String LOGIN_USUARIO = "admin";
    private static final String LOGIN_CONTRASENA = "admin123";

    private static final List<Usuario> usuarios = new ArrayList<>();
    private static final List<Paciente> pacientes = new ArrayList<>();
    private static final List<Vacuna> vacunas = new ArrayList<>();
    private static final List<Lote> lotes = new ArrayList<>();
    private static final List<CentroVacunacion> centros = new ArrayList<>();
    private static final List<Turno> turnos = new ArrayList<>();
    private static final List<RegistroVacunacion> registros = new ArrayList<>();
    private static final List<String> auditoria = new ArrayList<>();

    private static Administrador administradorBase;
    private static Medico medicoBase;
    private static Vacunador vacunadorBase;
    private static Recepcionista recepcionistaBase;
    private static Usuario usuarioActual;

    public static void main(String[] args) {
        cargarDatosIniciales();
        ejecutarAplicacion();
    }

    private static void cargarDatosIniciales() {
        administradorBase = new Administrador(1, "Administrador", LOGIN_USUARIO, LOGIN_CONTRASENA, "Administrador", "Jefe de sistema", "555-0001", "AD-001");
        medicoBase = new Medico(2, "Medico", "medico", "medico123", "Medico", "Medico General", "555-0002", "ME-001", "MAT-100", "General");
        vacunadorBase = new Vacunador(3, "Vacunador", "vacunador", "vacunador123", "Vacunador", "Auxiliar", "555-0003", "VA-001");
        recepcionistaBase = new Recepcionista(4, "Recepcionista", "recepcionista", "recep123", "Recepcionista", "Recepcion", "555-0004", "RE-001");

        usuarios.add(administradorBase);
        usuarios.add(medicoBase);
        usuarios.add(vacunadorBase);
        usuarios.add(recepcionistaBase);

        Vacuna vacunaCovid = new Vacuna(1, "Covid-19 Pfizer", "Pfizer", "Covid-19", 2, 21);
        Vacuna vacunaInfluenza = new Vacuna(2, "Influenza Adulto", "Sanofi", "Gripe estacional", 1, 0);
        vacunas.add(vacunaCovid);
        vacunas.add(vacunaInfluenza);

        Lote loteCovid = new Lote(1, "LOT-COV-01", LocalDate.now().minusDays(10), LocalDate.now().plusYears(1), 100, vacunaCovid);
        Lote loteInfluenza = new Lote(2, "LOT-INF-01", LocalDate.now().minusDays(5), LocalDate.now().plusMonths(6), 50, vacunaInfluenza);
        lotes.add(loteCovid);
        lotes.add(loteInfluenza);

        CentroVacunacion centroNorte = new CentroVacunacion(1, "Centro Norte", "Av. Principal 123", "555-1000", 50, LocalTime.of(8, 0), LocalTime.of(18, 0));
        CentroVacunacion centroSur = new CentroVacunacion(2, "Hospital Sur", "Calle Sur 456", "555-2000", 100, LocalTime.of(7, 0), LocalTime.of(20, 0));
        centroNorte.agregarStock(vacunaCovid, 80);
        centroNorte.agregarStock(vacunaInfluenza, 40);
        centroSur.agregarStock(vacunaCovid, 150);
        centroSur.agregarStock(vacunaInfluenza, 60);
        centros.add(centroNorte);
        centros.add(centroSur);

        Paciente pacienteJuan = new Paciente(5, "Juan Perez", "juan", "juan123", "Paciente", "Paciente", "555-3000", "PA-001", LocalDate.of(1990, 5, 15), "Ninguna", "Ninguna");
        Paciente pacienteMaria = new Paciente(6, "Maria Gomez", "maria", "maria123", "Paciente", "Paciente", "555-3001", "PA-002", LocalDate.of(1985, 11, 20), "Penicilina", "Hipertension controlada");
        pacientes.add(pacienteJuan);
        pacientes.add(pacienteMaria);
        usuarios.add(pacienteJuan);
        usuarios.add(pacienteMaria);

        Turno turno1 = new Turno(1, LocalDateTime.now().plusDays(1).withHour(9).withMinute(0), "PENDIENTE", "CONF-001", pacienteJuan, vacunaCovid, centroNorte);
        Turno turno2 = new Turno(2, LocalDateTime.now().plusDays(2).withHour(10).withMinute(30), "COMPLETADO", "CONF-002", pacienteMaria, vacunaInfluenza, centroSur);
        turnos.add(turno1);
        turnos.add(turno2);

        loteInfluenza.descontarStock(1);
        RegistroVacunacion registro = new RegistroVacunacion(1, LocalDateTime.now().minusHours(6), "Aplicacion sin incidentes", vacunadorBase.getNombre(), loteInfluenza.getNumeroLote(), 1, turno2, pacienteMaria, EstadoVacunacion.COMPLETADA);
        registros.add(registro);

        auditoria.add("Sistema inicializado con datos en memoria.");
    }

    private static void ejecutarAplicacion() {
        boolean ejecutando = true;
        while (ejecutando) {
            if (usuarioActual == null) {
                mostrarPantallaInicio();
                switch (leerTexto("Opcion")) {
                    case "1" -> iniciarSesion();
                    case "2" -> mostrarCredencialesPrueba();
                    case "0" -> ejecutando = false;
                    default -> System.out.println("Opcion no valida.");
                }
            } else {
                if (usuarioActual instanceof Administrador) {
                    mostrarMenuPrincipal();
                    switch (leerTexto("Opcion")) {
                        case "1" -> menuUsuarios();
                        case "2" -> menuCatalogos();
                        case "3" -> menuTurnos();
                        case "4" -> menuVacunacion();
                        case "5" -> menuRolActual();
                        case "6" -> cerrarSesion();
                        case "0" -> ejecutando = false;
                        default -> System.out.println("Opcion no valida.");
                    }
                } else {
                    menuRolActual();
                    if (usuarioActual != null) {
                        cerrarSesion();
                    }
                }
            }
        }
        System.out.println("Saliendo del sistema.");
    }

    private static void mostrarPantallaInicio() {
        System.out.println();
        System.out.println("==================================================");
        System.out.println("   SISTEMA DE REGISTRO Y TURNOS DE VACUNACION");
        System.out.println("==================================================");
        System.out.println("1. Iniciar sesion");
        System.out.println("2. Ver credenciales de prueba");
        System.out.println("0. Salir");
    }

    private static void mostrarMenuPrincipal() {
        System.out.println();
        System.out.println("==================================================");
        System.out.println("MENU ADMINISTRADOR | USUARIO ACTUAL: " + usuarioActual.getNombre() + " | Rol: " + usuarioActual.getRol());
        System.out.println("==================================================");
        System.out.println("1. Usuarios y pacientes");
        System.out.println("2. Vacunas, lotes e inventario");
        System.out.println("3. Turnos");
        System.out.println("4. Vacunacion y registros");
        System.out.println("5. Menu de rol (Medico/Paciente/Vacunador/Recepcionista)");
        System.out.println("6. Cerrar sesion");
        System.out.println("0. Salir");
    }

    private static void iniciarSesion() {
        System.out.println();
        System.out.println("--- INICIO DE SESION ---");
        String usuario = leerTexto("Usuario");
        String contrasena = leerTexto("Contrasena");
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(usuario) && u.getContrasena().equals(contrasena)) {
                usuarioActual = u;
                registrarAuditoria("Inicio de sesion correcto para " + usuarioActual.getNombre());
                System.out.println("Sesion iniciada correctamente. Bienvenido: " + usuarioActual.getNombre());
                return;
            }
        }
        System.out.println("Credenciales incorrectas.");
    }

    private static void mostrarCredencialesPrueba() {
        System.out.println();
        System.out.println("Credenciales de prueba:");
        System.out.println("Administrador: " + LOGIN_USUARIO + " / " + LOGIN_CONTRASENA);
        System.out.println("Medico: medico / medico123");
        System.out.println("Vacunador: vacunador / vacunador123");
        System.out.println("Recepcionista: recepcionista / recep123");
        System.out.println("Paciente (Juan Perez): juan / juan123");
        System.out.println("Paciente (Maria Gomez): maria / maria123");
    }

    private static void cerrarSesion() {
        registrarAuditoria("Cierre de sesion de " + usuarioActual.getNombre());
        usuarioActual.cerrarSesion();
        usuarioActual = null;
    }

    private static void menuUsuarios() {
        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("--- USUARIOS Y PACIENTES ---");
            System.out.println("1. Listar usuarios");
            System.out.println("2. Registrar paciente manualmente");
            System.out.println("3. Listar pacientes");
            System.out.println("4. Ver auditoria");
            System.out.println("0. Volver");
            switch (leerTexto("Opcion")) {
                case "1" -> listarUsuarios();
                case "2" -> registrarPacienteManual();
                case "3" -> listarPacientes();
                case "4" -> mostrarAuditoria();
                case "0" -> volver = true;
                default -> System.out.println("Opcion no valida.");
            }
        }
    }

    private static void menuCatalogos() {
        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("--- VACUNAS, LOTES E INVENTARIO ---");
            System.out.println("1. Listar vacunas");
            System.out.println("2. Crear vacuna");
            System.out.println("3. Listar lotes");
            System.out.println("4. Crear lote");
            System.out.println("5. Listar centros");
            System.out.println("6. Crear centro");
            System.out.println("7. Gestionar inventario de centro");
            System.out.println("0. Volver");
            switch (leerTexto("Opcion")) {
                case "1" -> listarVacunas();
                case "2" -> crearVacuna();
                case "3" -> listarLotes();
                case "4" -> crearLote();
                case "5" -> listarCentros();
                case "6" -> crearCentro();
                case "7" -> gestionarInventarioCentro();
                case "0" -> volver = true;
                default -> System.out.println("Opcion no valida.");
            }
        }
    }

    private static void menuTurnos() {
        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("--- TURNOS ---");
            System.out.println("1. Solicitar turno");
            System.out.println("2. Asignar turno manualmente");
            System.out.println("3. Consultar estado del turno");
            System.out.println("4. Cancelar turno");
            System.out.println("5. Reprogramar turno");
            System.out.println("6. Ver turnos del dia");
            System.out.println("0. Volver");
            switch (leerTexto("Opcion")) {
                case "1" -> solicitarTurno();
                case "2" -> asignarTurnoManual();
                case "3" -> consultarEstadoTurno();
                case "4" -> cancelarTurno();
                case "5" -> reprogramarTurno();
                case "6" -> verTurnosDelDia();
                case "0" -> volver = true;
                default -> System.out.println("Opcion no valida.");
            }
        }
    }

    private static void menuVacunacion() {
        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("--- VACUNACION Y REGISTROS ---");
            System.out.println("1. Registrar aplicacion de vacuna");
            System.out.println("2. Marcar paciente como ausente");
            System.out.println("3. Bloquear turno");
            System.out.println("4. Listar registros");
            System.out.println("5. Anular registro");
            System.out.println("0. Volver");
            switch (leerTexto("Opcion")) {
                case "1" -> registrarAplicacion();
                case "2" -> marcarAusente();
                case "3" -> bloquearTurno();
                case "4" -> listarRegistros();
                case "5" -> anularRegistro();
                case "0" -> volver = true;
                default -> System.out.println("Opcion no valida.");
            }
        }
    }

    private static void menuRolActual() {
        if (usuarioActual == null) {
            System.out.println("No hay usuario logueado.");
            return;
        }
        if (usuarioActual instanceof Administrador) {
            boolean volver = false;
            while (!volver) {
                System.out.println();
                System.out.println("--- MENU DE ROL (simular) ---");
                System.out.println("1. Medico");
                System.out.println("2. Paciente");
                System.out.println("3. Vacunador");
                System.out.println("4. Recepcionista");
                System.out.println("0. Volver");
                switch (leerTexto("Opcion")) {
                    case "1" -> menuMedico();
                    case "2" -> menuPaciente();
                    case "3" -> menuVacunador();
                    case "4" -> menuRecepcionista();
                    case "0" -> volver = true;
                    default -> System.out.println("Opcion no valida.");
                }
            }
        } else if (usuarioActual instanceof Medico) {
            menuMedico();
        } else if (usuarioActual instanceof Paciente) {
            menuPaciente();
        } else if (usuarioActual instanceof Vacunador) {
            menuVacunador();
        } else if (usuarioActual instanceof Recepcionista) {
            menuRecepcionista();
        } else {
            System.out.println("Rol no soportado para menu de rol.");
        }
    }

    private static void menuMedico() {
        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("--- MENU MEDICO ---");
            System.out.println("1. Ver turnos");
            System.out.println("2. Bloquear turno");
            System.out.println("3. Registrar contraindicacion");
            System.out.println("0. Volver");
            switch (leerTexto("Opcion")) {
                case "1" -> verTurnos();
                case "2" -> {
                    Turno turno = buscarTurnoSeleccionado("bloquear");
                    if (turno != null) {
                        String just = leerTexto("Justificacion");
                        medicoBase.bloquearTurno(turno, just);
                        System.out.println("Turno bloqueado.");
                    }
                }
                case "3" -> {
                    listarPacientes();
                    Paciente paciente = buscarPacientePorId(leerEntero("ID del paciente"));
                    if (paciente != null) {
                        listarVacunas();
                        Vacuna vacuna = buscarVacunaPorId(leerEntero("ID de la vacuna"));
                        if (vacuna != null) {
                            String motivo = leerTexto("Motivo de la contraindicacion");
                            medicoBase.registrarContraindicacion(paciente, vacuna, motivo);
                            System.out.println("Contraindicacion registrada.");
                        }
                    }
                }
                case "0" -> volver = true;
                default -> System.out.println("Opcion no valida.");
            }
        }
    }

    private static void menuPaciente() {
        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("--- MENU PACIENTE ---");
            System.out.println("1. Solicitar turno");
            System.out.println("2. Consultar estado del turno");
            System.out.println("3. Ver carnet digital");
            System.out.println("4. Cancelar turno");
            System.out.println("5. Reprogramar turno");
            System.out.println("0. Volver");
            switch (leerTexto("Opcion")) {
                case "1" -> solicitarTurnoComoPacienteActual();
                case "2" -> consultarEstadoDeMisTurnos();
                case "3" -> mostrarCarnetPacienteActual();
                case "4" -> cancelarMiTurno();
                case "5" -> reprogramarMiTurno();
                case "0" -> volver = true;
                default -> System.out.println("Opcion no valida.");
            }
        }
    }

    private static Paciente obtenerPacienteActual() {
        return usuarioActual instanceof Paciente ? (Paciente) usuarioActual : null;
    }

    private static List<Turno> obtenerTurnosDelPaciente(Paciente paciente) {
        List<Turno> turnosPaciente = new ArrayList<>();
        for (Turno turno : turnos) {
            if (turno.getPaciente() != null && turno.getPaciente().getId() == paciente.getId()) {
                turnosPaciente.add(turno);
            }
        }
        return turnosPaciente;
    }

    private static Turno seleccionarTurnoDelPaciente(String accion) {
        Paciente paciente = obtenerPacienteActual();
        if (paciente == null) {
            System.out.println("Solo un paciente autenticado puede hacer esta accion.");
            return null;
        }
        List<Turno> turnosPaciente = obtenerTurnosDelPaciente(paciente);
        if (turnosPaciente.isEmpty()) {
            System.out.println("No tienes turnos registrados.");
            return null;
        }
        System.out.println();
        System.out.println("--- TUS TURNOS ---");
        for (Turno turno : turnosPaciente) {
            System.out.println("ID: " + turno.getId() + " | Vacuna: " + (turno.getVacuna() != null ? turno.getVacuna().getNombre() : "-") + " | Fecha: " + (turno.getFechaHora() != null ? turno.getFechaHora().format(DATE_TIME_FORMATTER) : "-") + " | Estado: " + turno.getEstado());
        }
        Turno turno = buscarTurnoPorId(leerEntero("ID del turno a " + accion));
        if (turno == null || turno.getPaciente() == null || turno.getPaciente().getId() != paciente.getId()) {
            System.out.println("Turno no encontrado en tus registros.");
            return null;
        }
        return turno;
    }

    private static void verTurnos() {
        System.out.println();
        System.out.println("--- LISTA DE TURNOS ---");
        for (Turno t : turnos) {
            System.out.println("ID: " + t.getId() + " | Paciente: " + (t.getPaciente() != null ? t.getPaciente().getNombre() : "-") + " | Vacuna: " + (t.getVacuna() != null ? t.getVacuna().getNombre() : "-") + " | Fecha: " + (t.getFechaHora() != null ? t.getFechaHora().format(DATE_TIME_FORMATTER) : "-") + " | Estado: " + t.getEstado());
        }
    }

    private static void solicitarTurnoComoPacienteActual() {
        Paciente paciente = obtenerPacienteActual();
        if (paciente == null) {
            System.out.println("Solo un paciente autenticado puede solicitar turno desde este menu.");
            return;
        }
        System.out.println();
        System.out.println("--- SOLICITAR TURNO ---");
        listarVacunas();
        Vacuna vacuna = buscarVacunaPorId(leerEntero("ID de la vacuna"));
        if (vacuna == null) {
            System.out.println("Vacuna no encontrada.");
            return;
        }
        listarCentros();
        CentroVacunacion centro = buscarCentroPorId(leerEntero("ID del centro"));
        if (centro == null) {
            System.out.println("Centro no encontrado.");
            return;
        }
        if (!vacuna.validarEdadAplicacion(calcularEdad(paciente.getFechaNacimiento()))) {
            System.out.println("La vacuna no es valida para tu edad.");
            return;
        }
        LocalDateTime fechaHora = leerLocalDateTime("Fecha y hora del turno (yyyy-MM-dd HH:mm)");
        Turno turno = paciente.solicitarTurno(vacuna, centro);
        turno.setId(siguienteId(turnos));
        turno.setFechaHora(fechaHora);
        turnos.add(turno);
        System.out.println("Turno creado con ID " + turno.getId() + ".");
    }

    private static void consultarEstadoDeMisTurnos() {
        Turno turno = seleccionarTurnoDelPaciente("consultar");
        if (turno == null) {
            return;
        }
        System.out.println("Estado del turno: " + turno.getEstado());
        System.out.println("Paciente: " + turno.getPaciente().getNombre());
        System.out.println("Vacuna: " + turno.getVacuna().getNombre());
        System.out.println("Fecha: " + turno.getFechaHora().format(DATE_TIME_FORMATTER));
        System.out.println(turno.obtenerCodigoQR());
    }

    private static void mostrarCarnetPacienteActual() {
        Paciente paciente = obtenerPacienteActual();
        if (paciente == null) {
            System.out.println("Solo un paciente autenticado puede ver su carnet.");
            return;
        }
        System.out.println(paciente.verCarnetDigital());
        List<Turno> turnosPaciente = obtenerTurnosDelPaciente(paciente);
        if (turnosPaciente.isEmpty()) {
            System.out.println("No tienes turnos registrados.");
            return;
        }
        System.out.println("Turnos asociados:");
        for (Turno turno : turnosPaciente) {
            System.out.println("- ID: " + turno.getId() + " | Vacuna: " + (turno.getVacuna() != null ? turno.getVacuna().getNombre() : "-") + " | Estado: " + turno.getEstado());
        }
    }

    private static void cancelarMiTurno() {
        Turno turno = seleccionarTurnoDelPaciente("cancelar");
        if (turno == null) {
            return;
        }
        if (turno.cancelar(24)) {
            System.out.println("Turno cancelado.");
        }
    }

    private static void reprogramarMiTurno() {
        Turno turno = seleccionarTurnoDelPaciente("reprogramar");
        if (turno == null) {
            return;
        }
        LocalDateTime nueva = leerLocalDateTime("Nueva fecha y hora (yyyy-MM-dd HH:mm)");
        if (turno.reprogramar(nueva)) {
            System.out.println("Turno reprogramado.");
        }
    }

    private static void menuVacunador() {
        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("--- MENU VACUNADOR ---");
            System.out.println("1. Ver turnos pendientes");
            System.out.println("2. Registrar aplicacion de vacuna");
            System.out.println("3. Marcar paciente ausente");
            System.out.println("4. Listar registros");
            System.out.println("0. Volver");
            switch (leerTexto("Opcion")) {
                case "1" -> {
                    for (Turno t : turnos) {
                        if ("PENDIENTE".equals(t.getEstado())) {
                            System.out.println("ID: " + t.getId() + " | Paciente: " + t.getPaciente().getNombre() + " | Fecha: " + t.getFechaHora());
                        }
                    }
                }
                case "2" -> registrarAplicacion();
                case "3" -> {
                    Turno turno = buscarTurnoSeleccionado("marcar ausente");
                    if (turno != null) {
                        vacunadorBase.marcarAusente(turno);
                        System.out.println("Turno marcado como ausente.");
                    }
                }
                case "4" -> listarRegistros();
                case "0" -> volver = true;
                default -> System.out.println("Opcion no valida.");
            }
        }
    }

    private static void menuRecepcionista() {
        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("--- MENU RECEPCIONISTA ---");
            System.out.println("1. Registrar paciente manual");
            System.out.println("2. Asignar turno manualmente");
            System.out.println("0. Volver");
            switch (leerTexto("Opcion")) {
                case "1" -> registrarPacienteManual();
                case "2" -> asignarTurnoManual();
                case "0" -> volver = true;
                default -> System.out.println("Opcion no valida.");
            }
        }
    }

    private static void listarUsuarios() {
        System.out.println();
        System.out.println("--- USUARIOS ---");
        for (Usuario usuario : usuarios) {
            System.out.println(formatoUsuario(usuario));
        }
    }

    private static void listarPacientes() {
        System.out.println();
        System.out.println("--- PACIENTES ---");
        for (Paciente paciente : pacientes) {
            System.out.println("ID: " + paciente.getId() + " | " + paciente.getNombre() + " | Cedula: " + paciente.getCedula() + " | Nacimiento: " + paciente.getFechaNacimiento());
        }
    }

    private static void registrarPacienteManual() {
        System.out.println();
        System.out.println("--- REGISTRAR PACIENTE ---");
        String nombre = leerTexto("Nombre");
        String email = leerTexto("Usuario o email");
        String contrasena = leerTexto("Contrasena");
        String telefono = leerTexto("Telefono");
        String cedula = leerTexto("Cedula");
        LocalDate fechaNacimiento = leerLocalDate("Fecha de nacimiento (yyyy-MM-dd)");
        String alergias = leerTexto("Alergias");
        String condiciones = leerTexto("Condiciones medicas");
        Paciente nuevo = new Paciente(siguienteId(usuarios), nombre, email, contrasena, "Paciente", "Paciente", telefono, cedula, fechaNacimiento, alergias, condiciones);
        pacientes.add(nuevo);
        usuarios.add(nuevo);
        registrarAuditoria("Paciente registrado manualmente: " + nombre);
        System.out.println("Paciente registrado correctamente.");
    }

    private static void listarVacunas() {
        System.out.println();
        System.out.println("--- VACUNAS ---");
        for (Vacuna vacuna : vacunas) {
            System.out.println("ID: " + vacuna.getId() + " | " + vacuna.obtenerEsquemaCompleto());
        }
    }

    private static void crearVacuna() {
        System.out.println();
        System.out.println("--- CREAR VACUNA ---");
        String nombre = leerTexto("Nombre");
        String laboratorio = leerTexto("Laboratorio");
        String enfermedad = leerTexto("Enfermedad que previene");
        int dosis = leerEnteroMinimo("Dosis requeridas", 1);
        int intervalo = leerEnteroMinimo("Intervalo en dias", 0);
        Vacuna vacuna = new Vacuna(siguienteId(vacunas), nombre, laboratorio, enfermedad, dosis, intervalo);
        vacunas.add(vacuna);
        System.out.println("Vacuna creada correctamente.");
    }

    private static void listarLotes() {
        System.out.println();
        System.out.println("--- LOTES ---");
        for (Lote lote : lotes) {
            System.out.println("ID: " + lote.getId() + " | Lote: " + lote.getNumeroLote() + " | Vacuna: " + lote.getVacuna().getNombre() + " | Stock: " + lote.getStockDisponible() + " | Vence: " + lote.getFechaVencimiento());
        }
    }

    private static void crearLote() {
        System.out.println();
        System.out.println("--- CREAR LOTE ---");
        listarVacunas();
        Vacuna vacuna = buscarVacunaPorId(leerEntero("ID de la vacuna"));
        if (vacuna == null) {
            System.out.println("Vacuna no encontrada.");
            return;
        }
        String numeroLote = leerTexto("Numero de lote");
        LocalDate fabricacion = leerLocalDate("Fecha de fabricacion (yyyy-MM-dd)");
        LocalDate vencimiento = leerLocalDate("Fecha de vencimiento (yyyy-MM-dd)");
        int stockInicial = leerEnteroMinimo("Stock inicial", 1);
        Lote lote = new Lote(siguienteId(lotes), numeroLote, fabricacion, vencimiento, stockInicial, vacuna);
        lotes.add(lote);
        System.out.println("Lote creado correctamente.");
    }

    private static void listarCentros() {
        System.out.println();
        System.out.println("--- CENTROS ---");
        for (CentroVacunacion centro : centros) {
            System.out.println("ID: " + centro.getId() + " | " + centro.getNombre() + " | Direccion: " + centro.getDireccion() + " | Telefono: " + centro.getTelefono() + " | Capacidad: " + centro.getCapacidadDiaria());
            System.out.println("Inventario: " + centro.inventarioComoTexto());
        }
    }

    private static void crearCentro() {
        System.out.println();
        System.out.println("--- CREAR CENTRO ---");
        String nombre = leerTexto("Nombre");
        String direccion = leerTexto("Direccion");
        String telefono = leerTexto("Telefono");
        int capacidad = leerEnteroMinimo("Capacidad diaria", 1);
        LocalTime apertura = leerLocalTime("Horario de apertura (HH:mm)");
        LocalTime cierre = leerLocalTime("Horario de cierre (HH:mm)");
        CentroVacunacion centro = new CentroVacunacion(siguienteId(centros), nombre, direccion, telefono, capacidad, apertura, cierre);
        centros.add(centro);
        System.out.println("Centro creado correctamente.");
    }

    private static void gestionarInventarioCentro() {
        System.out.println();
        System.out.println("--- GESTION DE INVENTARIO ---");
        listarCentros();
        CentroVacunacion centro = buscarCentroPorId(leerEntero("ID del centro"));
        if (centro == null) {
            System.out.println("Centro no encontrado.");
            return;
        }
        System.out.println("Inventario actual de " + centro.getNombre() + ": " + centro.inventarioComoTexto());
        listarVacunas();
        Vacuna vacuna = buscarVacunaPorId(leerEntero("ID de la vacuna"));
        if (vacuna == null) {
            System.out.println("Vacuna no encontrada.");
            return;
        }
        int cantidad = leerEnteroMinimo("Cantidad a agregar", 1);
        centro.agregarStock(vacuna, cantidad);
        System.out.println("Inventario actualizado: " + centro.inventarioComoTexto());
    }

    private static void solicitarTurno() {
        System.out.println();
        System.out.println("--- SOLICITAR TURNO ---");
        listarPacientes();
        Paciente paciente = buscarPacientePorId(leerEntero("ID del paciente"));
        if (paciente == null) {
            System.out.println("Paciente no encontrado.");
            return;
        }
        listarVacunas();
        Vacuna vacuna = buscarVacunaPorId(leerEntero("ID de la vacuna"));
        if (vacuna == null) {
            System.out.println("Vacuna no encontrada.");
            return;
        }
        listarCentros();
        CentroVacunacion centro = buscarCentroPorId(leerEntero("ID del centro"));
        if (centro == null) {
            System.out.println("Centro no encontrado.");
            return;
        }
        if (!vacuna.validarEdadAplicacion(calcularEdad(paciente.getFechaNacimiento()))) {
            System.out.println("La vacuna no es valida para la edad del paciente.");
            return;
        }
        LocalDateTime fechaHora = leerLocalDateTime("Fecha y hora del turno (yyyy-MM-dd HH:mm)");
        Turno turno = paciente.solicitarTurno(vacuna, centro);
        turno.setId(siguienteId(turnos));
        turno.setFechaHora(fechaHora);
        turnos.add(turno);
        System.out.println("Turno creado con ID " + turno.getId() + ".");
    }

    private static void asignarTurnoManual() {
        System.out.println();
        System.out.println("--- ASIGNAR TURNO MANUAL ---");
        listarPacientes();
        Paciente paciente = buscarPacientePorId(leerEntero("ID del paciente"));
        if (paciente == null) {
            System.out.println("Paciente no encontrado.");
            return;
        }
        listarVacunas();
        Vacuna vacuna = buscarVacunaPorId(leerEntero("ID de la vacuna"));
        if (vacuna == null) {
            System.out.println("Vacuna no encontrada.");
            return;
        }
        listarCentros();
        CentroVacunacion centro = buscarCentroPorId(leerEntero("ID del centro"));
        if (centro == null) {
            System.out.println("Centro no encontrado.");
            return;
        }
        LocalDateTime fechaHora = leerLocalDateTime("Fecha y hora del turno (yyyy-MM-dd HH:mm)");
        Turno turno = recepcionistaBase.asignarTurnoManual(paciente, vacuna, centro);
        turno.setId(siguienteId(turnos));
        turno.setFechaHora(fechaHora);
        turnos.add(turno);
        System.out.println("Turno asignado con ID " + turno.getId() + ".");
    }

    private static void consultarEstadoTurno() {
        Turno turno = buscarTurnoSeleccionado("consultar");
        if (turno == null) {
            return;
        }
        System.out.println("Estado del turno: " + turno.getEstado());
        System.out.println("Paciente: " + turno.getPaciente().getNombre());
        System.out.println("Vacuna: " + turno.getVacuna().getNombre());
        System.out.println("Fecha: " + turno.getFechaHora().format(DATE_TIME_FORMATTER));
        System.out.println(turno.obtenerCodigoQR());
    }

    private static void cancelarTurno() {
        Turno turno = buscarTurnoSeleccionado("cancelar", "PENDIENTE", "CONFIRMADO");
        if (turno == null) {
            return;
        }
        if (turno.cancelar(24)) {
            System.out.println("Turno cancelado.");
        }
    }

    private static void reprogramarTurno() {
        Turno turno = buscarTurnoSeleccionado("reprogramar", "PENDIENTE", "CONFIRMADO");
        if (turno == null) {
            return;
        }
        LocalDateTime nuevaFecha = leerLocalDateTime("Nueva fecha y hora (yyyy-MM-dd HH:mm)");
        if (turno.reprogramar(nuevaFecha)) {
            System.out.println("Turno reprogramado.");
        }
    }

    private static void verTurnosDelDia() {
        LocalDate fecha = leerLocalDate("Fecha a consultar (yyyy-MM-dd)");
        System.out.println();
        System.out.println("--- TURNOS DEL DIA " + fecha + " ---");
        for (Turno turno : turnos) {
            if (turno.getFechaHora() != null && turno.getFechaHora().toLocalDate().equals(fecha)) {
                System.out.println("ID: " + turno.getId() + " | Paciente: " + turno.getPaciente().getNombre() + " | Vacuna: " + turno.getVacuna().getNombre() + " | Estado: " + turno.getEstado());
            }
        }
    }

    private static void registrarAplicacion() {
        System.out.println();
        System.out.println("--- REGISTRAR APLICACION ---");
        Turno turno = buscarTurnoSeleccionado("registrar la aplicacion", "PENDIENTE");
        if (turno == null) {
            return;
        }
        listarLotes();
        Lote lote = buscarLotePorId(leerEntero("ID del lote"));
        if (lote == null) {
            System.out.println("Lote no encontrado.");
            return;
        }
        String observaciones = leerTexto("Observaciones clinicas");
        RegistroVacunacion reg = vacunadorBase.registrarAplicacion(turno, lote, observaciones);
        if (reg == null) {
            return;
        }
        reg.setId(siguienteId(registros));
        registros.add(reg);
        System.out.println("Aplicacion registrada correctamente.");
    }

    private static void marcarAusente() {
        Turno turno = buscarTurnoSeleccionado("marcar como ausente", "PENDIENTE");
        if (turno == null) {
            return;
        }
        vacunadorBase.marcarAusente(turno);
    }

    private static void bloquearTurno() {
        Turno turno = buscarTurnoSeleccionado("bloquear", "PENDIENTE");
        if (turno == null) {
            return;
        }
        String justificacion = leerTexto("Justificacion");
        medicoBase.bloquearTurno(turno, justificacion);
    }

    private static void listarRegistros() {
        System.out.println();
        System.out.println("--- REGISTROS DE VACUNACION ---");
        for (RegistroVacunacion reg : registros) {
            System.out.println("ID: " + reg.getId() + " | Paciente: " + reg.getPaciente().getNombre() + " | Turno: " + reg.getTurno().getId() + " | Lote: " + reg.getNumeroLoteUsado() + " | Estado: " + reg.getEstado());
        }
    }

    private static void anularRegistro() {
        listarRegistros();
        if (registros.isEmpty()) {
            System.out.println("No hay registros para anular.");
            return;
        }
        int id = leerEntero("ID del registro");
        RegistroVacunacion reg = buscarRegistroPorId(id);
        if (reg == null) {
            System.out.println("Registro no encontrado.");
            return;
        }
        String justificacion = leerTexto("Justificacion");
        administradorBase.anularRegistroVacunacion(reg, justificacion);
        System.out.println("Registro anulado.");
    }

    private static void mostrarAuditoria() {
        System.out.println();
        System.out.println("--- AUDITORIA ---");
        for (String linea : auditoria) {
            System.out.println(linea);
        }
    }

    private static Turno buscarTurnoSeleccionado(String accion, String... estadosPermitidos) {
        if (turnos.isEmpty()) {
            System.out.println("No hay turnos para " + accion + ".");
            return null;
        }
        boolean filtrarPorEstado = estadosPermitidos != null && estadosPermitidos.length > 0;
        boolean hayCoincidencias = false;
        for (Turno turno : turnos) {
            if (!filtrarPorEstado || estadoPermitido(turno, estadosPermitidos)) {
                hayCoincidencias = true;
                System.out.println("ID: " + turno.getId() + " | Paciente: " + turno.getPaciente().getNombre() + " | Vacuna: " + turno.getVacuna().getNombre() + " | Estado: " + turno.getEstado());
            }
        }
        if (!hayCoincidencias) {
            System.out.println("No hay turnos disponibles para " + accion + ".");
            return null;
        }
        Turno turno = buscarTurnoPorId(leerEntero("ID del turno a " + accion));
        if (turno == null || (filtrarPorEstado && !estadoPermitido(turno, estadosPermitidos))) {
            System.out.println("Turno no encontrado.");
            return null;
        }
        return turno;
    }

    private static boolean estadoPermitido(Turno turno, String... estadosPermitidos) {
        if (turno == null || estadosPermitidos == null || estadosPermitidos.length == 0) {
            return true;
        }
        for (String estado : estadosPermitidos) {
            if (estado != null && estado.equalsIgnoreCase(turno.getEstado())) {
                return true;
            }
        }
        return false;
    }

    private static Paciente buscarPacientePorId(int id) {
        for (Paciente paciente : pacientes) {
            if (paciente.getId() == id) return paciente;
        }
        return null;
    }

    private static Vacuna buscarVacunaPorId(int id) {
        for (Vacuna vacuna : vacunas) {
            if (vacuna.getId() == id) return vacuna;
        }
        return null;
    }

    private static Lote buscarLotePorId(int id) {
        for (Lote lote : lotes) {
            if (lote.getId() == id) return lote;
        }
        return null;
    }

    private static CentroVacunacion buscarCentroPorId(int id) {
        for (CentroVacunacion centro : centros) {
            if (centro.getId() == id) return centro;
        }
        return null;
    }

    private static Turno buscarTurnoPorId(int id) {
        for (Turno turno : turnos) {
            if (turno.getId() == id) return turno;
        }
        return null;
    }

    private static RegistroVacunacion buscarRegistroPorId(int id) {
        for (RegistroVacunacion reg : registros) {
            if (reg.getId() == id) return reg;
        }
        return null;
    }

    private static void registrarAuditoria(String mensaje) {
        auditoria.add("[" + LocalDateTime.now().format(DATE_TIME_FORMATTER) + "] " + mensaje);
    }

    private static String formatoUsuario(Usuario usuario) {
        StringBuilder texto = new StringBuilder();
        texto.append("ID: ").append(usuario.getId());
        texto.append(" | Nombre: ").append(usuario.getNombre());
        texto.append(" | Rol: ").append(usuario.getRol());
        texto.append(" | Usuario: ").append(usuario.getEmail());
        if (usuario instanceof Paciente p) {
            texto.append(" | Nacimiento: ").append(p.getFechaNacimiento());
        } else if (usuario instanceof Medico m) {
            texto.append(" | Matricula: ").append(m.getMatricula());
        }
        return texto.toString();
    }

    private static int siguienteId(List<?> lista) {
        return lista.size() + 1;
    }

    private static int calcularEdad(LocalDate nacimiento) {
        return nacimiento == null ? 0 : Period.between(nacimiento, LocalDate.now()).getYears();
    }

    private static String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje + ": ");
            String valor = SCANNER.nextLine().trim();
            if (!valor.isEmpty()) return valor;
            System.out.println("El valor no puede estar vacio.");
        }
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje + ": ");
            try {
                return Integer.parseInt(SCANNER.nextLine().trim());
            } catch (NumberFormatException ex) {
                System.out.println("Ingrese un numero valido.");
            }
        }
    }

    private static int leerEnteroMinimo(String mensaje, int minimo) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor >= minimo) return valor;
            System.out.println("El valor debe ser mayor o igual a " + minimo + ".");
        }
    }

    private static LocalDate leerLocalDate(String mensaje) {
        while (true) {
            System.out.print(mensaje + ": ");
            try {
                return LocalDate.parse(SCANNER.nextLine().trim(), DATE_FORMATTER);
            } catch (DateTimeParseException ex) {
                System.out.println("Fecha invalida.");
            }
        }
    }

    private static LocalDateTime leerLocalDateTime(String mensaje) {
        while (true) {
            System.out.print(mensaje + ": ");
            try {
                return LocalDateTime.parse(SCANNER.nextLine().trim(), DATE_TIME_FORMATTER);
            } catch (DateTimeParseException ex) {
                System.out.println("Fecha y hora invalidas.");
            }
        }
    }

    private static LocalTime leerLocalTime(String mensaje) {
        while (true) {
            System.out.print(mensaje + ": ");
            try {
                return LocalTime.parse(SCANNER.nextLine().trim(), TIME_FORMATTER);
            } catch (DateTimeParseException ex) {
                System.out.println("Hora invalida.");
            }
        }
    }

    // ===== CLASES INTERNAS =====

    private static abstract class Usuario {
        protected final int id;
        protected final String nombre;
        protected final String email;
        protected final String contrasena;
        protected final String rol;
        protected final String cedula;

        Usuario(int id, String nombre, String email, String contrasena, String rol, String cargo, String telefono, String cedula) {
            this.id = id;
            this.nombre = nombre;
            this.email = email;
            this.contrasena = contrasena;
            this.rol = rol;
            this.cedula = cedula;
        }

        public int getId() { return id; }
        public String getNombre() { return nombre; }
        public String getEmail() { return email; }
        public String getContrasena() { return contrasena; }
        public String getRol() { return rol; }
        public String getCedula() { return cedula; }

        public void cerrarSesion() {
            System.out.println("Sesion cerrada para " + nombre + ".");
        }
    }

    private static class Administrador extends Usuario {
        Administrador(int id, String nombre, String email, String contrasena, String rol, String cargo, String telefono, String cedula) {
            super(id, nombre, email, contrasena, rol, cargo, telefono, cedula);
        }

        public void anularRegistroVacunacion(RegistroVacunacion registro, String justificacion) {
            if (registro != null) {
                registro.setEstado(EstadoVacunacion.ANULADA);
                registro.setObservacionesClinicas(registro.getObservacionesClinicas() + " [ANULADO: " + justificacion + "]");
            }
        }
    }

    private static class Medico extends Usuario {
        private final String matricula;

        Medico(int id, String nombre, String email, String contrasena, String rol, String cargo, String telefono, String cedula, String matricula, String especialidad) {
            super(id, nombre, email, contrasena, rol, cargo, telefono, cedula);
            this.matricula = matricula;
        }

        public String getMatricula() { return matricula; }

        public void registrarContraindicacion(Paciente paciente, Vacuna vacuna, String motivo) {
            if (paciente.getCondicionesMedicas() == null || paciente.getCondicionesMedicas().isEmpty()) {
                paciente.setCondicionesMedicas(motivo);
            } else {
                paciente.setCondicionesMedicas(paciente.getCondicionesMedicas() + " | " + motivo);
            }
        }

        public void bloquearTurno(Turno turno, String justificacion) {
            if (turno != null) {
                turno.setEstado("BLOQUEADO");
            }
        }
    }

    private static class Vacunador extends Usuario {
        Vacunador(int id, String nombre, String email, String contrasena, String rol, String cargo, String telefono, String cedula) {
            super(id, nombre, email, contrasena, rol, cargo, telefono, cedula);
        }

        public RegistroVacunacion registrarAplicacion(Turno turno, Lote lote, String observaciones) {
            if (turno == null || lote == null || lote.estaVencido() || !lote.descontarStock(1)) {
                return null;
            }
            turno.setEstado("COMPLETADO");
            RegistroVacunacion reg = new RegistroVacunacion();
            reg.setFechaAplicacion(LocalDateTime.now());
            reg.setObservacionesClinicas(observaciones);
            reg.setVacunadorNombre(this.nombre);
            reg.setNumeroLoteUsado(lote.getNumeroLote());
            reg.setDosisAplicada(1);
            reg.setTurno(turno);
            reg.setPaciente(turno.getPaciente());
            reg.setEstado(EstadoVacunacion.COMPLETADA);
            return reg;
        }

        public void marcarAusente(Turno turno) {
            if (turno != null) turno.setEstado("AUSENTE");
        }
    }

    private static class Recepcionista extends Usuario {
        Recepcionista(int id, String nombre, String email, String contrasena, String rol, String cargo, String telefono, String cedula) {
            super(id, nombre, email, contrasena, rol, cargo, telefono, cedula);
        }

        public Turno asignarTurnoManual(Paciente paciente, Vacuna vacuna, CentroVacunacion centro) {
            Turno nuevoTurno = new Turno();
            nuevoTurno.setPaciente(paciente);
            nuevoTurno.setVacuna(vacuna);
            nuevoTurno.setCentro(centro);
            nuevoTurno.setFechaHora(LocalDateTime.now().plusDays(1));
            nuevoTurno.setEstado("PENDIENTE");
            nuevoTurno.setCodigoConfirmacion("CONF-MAN");
            return nuevoTurno;
        }
    }

    private static class Paciente extends Usuario {
        private LocalDate fechaNacimiento;
        private String condicionesMedicas;

        Paciente(int id, String nombre, String email, String contrasena, String rol, String cargo, String telefono, String cedula, LocalDate fechaNacimiento, String alergias, String condicionesMedicas) {
            super(id, nombre, email, contrasena, rol, cargo, telefono, cedula);
            this.fechaNacimiento = fechaNacimiento;
            this.condicionesMedicas = condicionesMedicas;
        }

        public LocalDate getFechaNacimiento() { return fechaNacimiento; }
        public String getCondicionesMedicas() { return condicionesMedicas; }
        public void setCondicionesMedicas(String condicionesMedicas) { this.condicionesMedicas = condicionesMedicas; }

        public Turno solicitarTurno(Vacuna vacuna, CentroVacunacion centro) {
            Turno turno = new Turno();
            turno.setPaciente(this);
            turno.setVacuna(vacuna);
            turno.setCentro(centro);
            turno.setFechaHora(LocalDateTime.now().plusDays(3));
            turno.setEstado("PENDIENTE");
            turno.setCodigoConfirmacion("CONF-" + System.currentTimeMillis() % 100000);
            return turno;
        }

        public String verCarnetDigital() { return "Carnet digital de " + nombre; }
    }

    private static class Vacuna {
        private final int id;
        private final String nombre;
        private final String laboratorio;
        private final String enfermedadPreviene;
        private final int dosisRequeridas;
        private final int intervaloDias;

        Vacuna(int id, String nombre, String laboratorio, String enfermedadPreviene, int dosisRequeridas, int intervaloDias) {
            this.id = id;
            this.nombre = nombre;
            this.laboratorio = laboratorio;
            this.enfermedadPreviene = enfermedadPreviene;
            this.dosisRequeridas = dosisRequeridas;
            this.intervaloDias = intervaloDias;
        }

        public int getId() { return id; }
        public String getNombre() { return nombre; }
        public String obtenerEsquemaCompleto() {
            return nombre + " (" + laboratorio + ") | Previene: " + enfermedadPreviene + " | Dosis: " + dosisRequeridas + " | Intervalo: " + intervaloDias + " dias";
        }
        public boolean validarEdadAplicacion(int edadAnios) {
            return !nombre.toLowerCase().contains("covid") || edadAnios >= 3;
        }
    }

    private static class Lote {
        private final int id;
        private final String numeroLote;
        private final LocalDate fechaVencimiento;
        private int stockDisponible;
        private final Vacuna vacuna;

        Lote(int id, String numeroLote, LocalDate fechaFabricacion, LocalDate fechaVencimiento, int stockInicial, Vacuna vacuna) {
            this.id = id;
            this.numeroLote = numeroLote;
            this.fechaVencimiento = fechaVencimiento;
            this.stockDisponible = stockInicial;
            this.vacuna = vacuna;
        }

        public int getId() { return id; }
        public String getNumeroLote() { return numeroLote; }
        public LocalDate getFechaVencimiento() { return fechaVencimiento; }
        public int getStockDisponible() { return stockDisponible; }
        public Vacuna getVacuna() { return vacuna; }
        public boolean descontarStock(int cantidad) {
            if (stockDisponible >= cantidad) { stockDisponible -= cantidad; return true; }
            return false;
        }
        public boolean estaVencido() { return LocalDate.now().isAfter(fechaVencimiento); }
    }

    private static class CentroVacunacion {
        private final int id;
        private final String nombre;
        private final String direccion;
        private final String telefono;
        private final int capacidadDiaria;
        private final Map<String, Integer> stockVacunas = new LinkedHashMap<>();

        CentroVacunacion(int id, String nombre, String direccion, String telefono, int capacidadDiaria, LocalTime horarioApertura, LocalTime horarioCierre) {
            this.id = id;
            this.nombre = nombre;
            this.direccion = direccion;
            this.telefono = telefono;
            this.capacidadDiaria = capacidadDiaria;
        }

        public int getId() { return id; }
        public String getNombre() { return nombre; }
        public String getDireccion() { return direccion; }
        public String getTelefono() { return telefono; }
        public int getCapacidadDiaria() { return capacidadDiaria; }
        public void agregarStock(Vacuna vacuna, int cantidad) {
            stockVacunas.put(vacuna.getNombre(), stockVacunas.getOrDefault(vacuna.getNombre(), 0) + cantidad);
        }
        public String inventarioComoTexto() {
            if (stockVacunas.isEmpty()) return "Sin stock registrado";
            StringBuilder texto = new StringBuilder();
            boolean primero = true;
            for (Map.Entry<String, Integer> entry : stockVacunas.entrySet()) {
                if (!primero) texto.append(" | ");
                texto.append(entry.getKey()).append(": ").append(entry.getValue());
                primero = false;
            }
            return texto.toString();
        }
    }

    private static class Turno {
        private int id;
        private LocalDateTime fechaHora;
        private String estado;
        @SuppressWarnings("unused")
        private String codigoConfirmacion;
        private Paciente paciente;
        private Vacuna vacuna;
        @SuppressWarnings("unused")
        private CentroVacunacion centro;

        Turno() {}

        Turno(int id, LocalDateTime fechaHora, String estado, String codigoConfirmacion, Paciente paciente, Vacuna vacuna, CentroVacunacion centro) {
            this.id = id;
            this.fechaHora = fechaHora;
            this.estado = estado;
            this.codigoConfirmacion = codigoConfirmacion;
            this.paciente = paciente;
            this.vacuna = vacuna;
            this.centro = centro;
        }

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public LocalDateTime getFechaHora() { return fechaHora; }
        public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
        public void setCodigoConfirmacion(String codigoConfirmacion) { this.codigoConfirmacion = codigoConfirmacion; }
        public Paciente getPaciente() { return paciente; }
        public void setPaciente(Paciente paciente) { this.paciente = paciente; }
        public Vacuna getVacuna() { return vacuna; }
        public void setVacuna(Vacuna vacuna) { this.vacuna = vacuna; }
        public void setCentro(CentroVacunacion centro) { this.centro = centro; }
        public boolean cancelar(int anticipacionHoras) { estado = "CANCELADO"; return true; }
        public boolean reprogramar(LocalDateTime nuevaFecha) { fechaHora = nuevaFecha; estado = "PENDIENTE"; return true; }
        public String obtenerCodigoQR() { return "[QR Turno " + id + "]"; }
    }

    private enum EstadoVacunacion {
        COMPLETADA,
        ANULADA
    }

    private static class RegistroVacunacion {
        private int id;
        @SuppressWarnings("unused")
        private LocalDateTime fechaAplicacion;
        private String observacionesClinicas;
        @SuppressWarnings("unused")
        private String vacunadorNombre;
        private String numeroLoteUsado;
        @SuppressWarnings("unused")
        private int dosisAplicada;
        private Turno turno;
        private Paciente paciente;
        private EstadoVacunacion estado;

        RegistroVacunacion() {}

        RegistroVacunacion(int id, LocalDateTime fechaAplicacion, String observacionesClinicas, String vacunadorNombre, String numeroLoteUsado, int dosisAplicada, Turno turno, Paciente paciente, EstadoVacunacion estado) {
            this.id = id;
            this.fechaAplicacion = fechaAplicacion;
            this.observacionesClinicas = observacionesClinicas;
            this.vacunadorNombre = vacunadorNombre;
            this.numeroLoteUsado = numeroLoteUsado;
            this.dosisAplicada = dosisAplicada;
            this.turno = turno;
            this.paciente = paciente;
            this.estado = estado;
        }

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public void setFechaAplicacion(LocalDateTime fechaAplicacion) { this.fechaAplicacion = fechaAplicacion; }
        public String getObservacionesClinicas() { return observacionesClinicas; }
        public void setObservacionesClinicas(String observacionesClinicas) { this.observacionesClinicas = observacionesClinicas; }
        public void setVacunadorNombre(String vacunadorNombre) { this.vacunadorNombre = vacunadorNombre; }
        public String getNumeroLoteUsado() { return numeroLoteUsado; }
        public void setNumeroLoteUsado(String numeroLoteUsado) { this.numeroLoteUsado = numeroLoteUsado; }
        public void setDosisAplicada(int dosisAplicada) { this.dosisAplicada = dosisAplicada; }
        public Turno getTurno() { return turno; }
        public void setTurno(Turno turno) { this.turno = turno; }
        public Paciente getPaciente() { return paciente; }
        public void setPaciente(Paciente paciente) { this.paciente = paciente; }
        public EstadoVacunacion getEstado() { return estado; }
        public void setEstado(EstadoVacunacion estado) { this.estado = estado; }
    }
}