ISO/IEC 25010 — Mapeo de características de calidad al proyecto "Registro y Turnos de Vacunación"

Resumen
-------
Este documento relaciona las características de calidad definidas en la norma ISO/IEC 25010 con las decisiones de diseño y elementos concretos del proyecto (principalmente el archivo `Main.java` y las clases del paquete `modelos`). Está pensado para verificar requisitos de calidad y orientar mejoras.
pro
1) Adecuación funcional
- Qué significa: el sistema ofrece las funciones que los usuarios esperan y que constan en el UML.
- Evidencia en el proyecto:
  - Implementación de flujos de negocio principales en [Registro-y-turnos-de-vacunacion/src/main/Main.java](Registro-y-turnos-de-vacunacion/src/main/Main.java): autenticación (`iniciarSesion()`), gestión de turnos (`solicitarTurno()`, `asignarTurnoManual()`, `verTurnos()`, `registrarAplicacion()`), gestión de usuarios (`registrarPacienteManual()`).
  - Modelos relevantes: [src/modelos/Turno.java](Registro-y-turnos-de-vacunacion/src/modelos/Turno.java), [src/modelos/RegistroVacunacion.java](Registro-y-turnos-de-vacunacion/src/modelos/RegistroVacunacion.java), [src/modelos/Vacuna.java](Registro-y-turnos-de-vacunacion/src/modelos/Vacuna.java).
- Recomendación: documentar requisitos funcionales y añadir pruebas unitarias y de integración para cada caso de uso principal.

2) Usabilidad
- Qué significa: el sistema es fácil de usar y entender por sus usuarios objetivo.
- Evidencia en el proyecto:
  - Interfaz de consola con menús por rol y mensajes orientativos (`menuMedico()`, `menuPaciente()`, `menuVacunador()`, `menuRecepcionista()` en [Main.java](Registro-y-turnos-de-vacunacion/src/main/Main.java)).
  - Validaciones básicas de entrada (`leerTexto()`, `leerEntero()`, validación de fechas).
- Recomendación: mejorar mensajes de ayuda, confirmar acciones destructivas, y crear una guía de uso / ejemplos; considerar pruebas de usabilidad con usuarios reales o escenarios de usuario.

3) Eficiencia
- Qué significa: desempeño y uso de recursos acorde con requisitos.
- Evidencia en el proyecto:
  - Almacenamiento en memoria con listas (`List<>`) y recorridos lineales en operaciones de búsqueda (p. ej. `buscarTurnoPorId()`, `buscarPacientePorId()` en [Main.java](Registro-y-turnos-de-vacunacion/src/main/Main.java)). Adecuado para prototipo y datos pequeños.
- Recomendación: si se espera crecimiento, usar estructuras indexadas (Map por id) o persistencia; medir tiempos en escenarios con N grande.

4) Fiabilidad
- Qué significa: el sistema mantiene operaciones correctas bajo condiciones esperadas y recupera fallos razonablemente.
- Evidencia en el proyecto:
  - Manejo de entradas inválidas y parseos con bucles de reintento (`leerLocalDate`, `leerLocalDateTime`).
  - Gestión de estado de turnos/registros con transiciones simples (`Turno.setEstado(...)`, `RegistroVacunacion` creado por `Vacunador.registrarAplicacion`).
- Riesgos: uso de estado global mutable sin control de concurrencia; falta de manejo de excepciones más allá de parseos.
- Recomendación: añadir casos de prueba que simulen errores, validaciones más estrictas y, si procede, control de concurrencia y persistencia transaccional.

5) Seguridad
- Qué significa: protección contra accesos no autorizados, integridad de datos y confidencialidad.
- Evidencia en el proyecto:
  - Autenticación básica en memoria (`iniciarSesion()`); credenciales de prueba visibles en `Main.java`.
- Riesgos: contraseñas en texto plano, ausencia de hashing, sin control de roles más fino ni logging seguro.
- Recomendación: para producción introducir hashing de contraseñas, control de sesiones, validación de entradas (sanitización), y políticas de auditoría y acceso.

6) Mantenibilidad
- Qué significa: facilidad para modificar el sistema, arreglar errores y extender funcionalidad.
- Evidencia en el proyecto:
  - Código consolidado en un único archivo `Main.java` (fácil de explorar, pero poco modular).
  - Clases modelo agrupadas en `src/modelos/`.
- Recomendación: refactorizar en capas (modelo, servicio, persistencia, UI), extraer clases a archivos individuales, añadir Javadoc, y crear suite de pruebas automatizadas.

7) Portabilidad
- Qué significa: facilidad para ejecutar el software en diferentes entornos (sistemas operativos, JVMs).
- Evidencia en el proyecto:
  - Implementado en Java y usando solo la biblioteca estándar → alta portabilidad entre JVMs y OS.
  - Compilación y ejecución con javac/java (comandos de build simples en el repo).
- Recomendación: documentar la versión mínima de Java requerida y proporcionar scripts de build (`gradle`/`maven`) para facilitar despliegue multiplataforma.

Anexos — Acciones concretas sugeridas
-----------------------------------
- Tests: añadir `tests/` con pruebas unitarias (JUnit) para `Turno`, `Paciente`, `Vacunador` y flujos end-to-end del `Main` (pueden ser pruebas de integración que simulen I/O).
- Refactor: dividir `Main.java` en paquetes `ui`, `service`, `model` con clases pequeñas y responsabilidades claras.
- Seguridad: reemplazar credenciales en texto por un mecanismo de carga segura (archivo cifrado o variables de entorno) y aplicar hashing de contraseñas (BCrypt).

Archivo fuente principal
- [Registro-y-turnos-de-vacunacion/src/main/Main.java](Registro-y-turnos-de-vacunacion/src/main/Main.java)

Fin del informe.
