Sistema de Registro y Turnos de Vacunación

Proyecto de ejemplo para el manejo de usuarios, turnos y registros de vacunación.

Resumen
Aplicación Java (NetBeans / Ant) que permite gestionar centros, vacunas, pacientes, turnos y registrar vacunaciones. Incluye generación de reportes y notificaciones básicas.

Requisitos
- Java JDK 8 o superior
- Apache Ant 1.8+ (opcional si usa NetBeans)

Compilar
Desde la carpeta del proyecto:

```bash
cd Registro-y-turnos-de-vacunacion
ant clean
ant
```

Esto ejecuta las tareas por defecto (compilación, tests y construcción del JAR).

Ejecutar
Después de compilar, puede ejecutar la clase principal:

```bash
cd Registro-y-turnos-de-vacunacion
java -cp build/classes sistema_vacunacion.Sistema_Vacunacion
```

O abra el proyecto en NetBeans y use Run.

Estructura del proyecto
- src/ : código fuente
- build.xml, nbproject/ : scripts y configuración de Ant/NetBeans
- lib/ : dependencias externas (si las hubiera)
- dist/ : artefactos generados (JAR)

Funcionalidades principales (demo mínima)
1. Crear/gestionar usuarios (administrador, recepcionista, vacunador)
2. Registrar pacientes
3. Agendar turnos
4. Registrar vacunación (lote, vacuna, vacunador)
5. Generar reportes

Para un guion de demostración paso a paso, ver `docs/DEMO_SCRIPT.md`.

Calidad y mapeo a ISO/25010
El repositorio contiene una matriz de mapeo a ISO/25010 en `ISO25010_MAPPING.md`.

Presentación
Un bosquejo de la presentación está en `docs/PRESENTACION.md`.

Contacto
Para dudas o ajustes rápidos, abre un issue o contáctame directamente.
