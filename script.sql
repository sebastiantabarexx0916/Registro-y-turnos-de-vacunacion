-- Script SQL para crear la base de datos y tablas del sistema VacUNO
-- Generado: 2026-05-19

CREATE DATABASE IF NOT EXISTS db_vacunacion;
USE db_vacunacion;

-- Usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Pacientes
CREATE TABLE IF NOT EXISTS pacientes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    dni VARCHAR(20) UNIQUE NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100),
    direccion TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Vacunas
CREATE TABLE IF NOT EXISTS vacunas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    dosis_requeridas INT DEFAULT 1,
    edad_minima_meses INT DEFAULT 0,
    activa BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Lotes
CREATE TABLE IF NOT EXISTS lotes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    vacuna_id INT NOT NULL,
    codigo_lote VARCHAR(50) UNIQUE NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    cantidad INT NOT NULL,
    cantidad_disponible INT NOT NULL,
    FOREIGN KEY (vacuna_id) REFERENCES vacunas(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Vacunaciones
CREATE TABLE IF NOT EXISTS vacunaciones (
    id INT PRIMARY KEY AUTO_INCREMENT,
    paciente_id INT NOT NULL,
    vacuna_id INT NOT NULL,
    lote_id INT NOT NULL,
    fecha_aplicacion DATETIME NOT NULL,
    numero_dosis INT NOT NULL,
    observaciones TEXT,
    usuario_id INT NOT NULL,
    estado VARCHAR(20) DEFAULT 'COMPLETADA',
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
    FOREIGN KEY (vacuna_id) REFERENCES vacunas(id),
    FOREIGN KEY (lote_id) REFERENCES lotes(id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Turnos
CREATE TABLE IF NOT EXISTS turnos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    paciente_id INT NOT NULL,
    vacuna_id INT NOT NULL,
    fecha_turno DATETIME NOT NULL,
    estado VARCHAR(20) DEFAULT 'PENDIENTE',
    observaciones TEXT,
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
    FOREIGN KEY (vacuna_id) REFERENCES vacunas(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Auditoría
CREATE TABLE IF NOT EXISTS auditoria (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario VARCHAR(50),
    accion VARCHAR(100),
    tabla_afectada VARCHAR(50),
    detalles TEXT,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Índices
CREATE INDEX IF NOT EXISTS idx_vacunaciones_paciente ON vacunaciones(paciente_id);
CREATE INDEX IF NOT EXISTS idx_vacunaciones_fecha ON vacunaciones(fecha_aplicacion);
CREATE INDEX IF NOT EXISTS idx_lotes_vencimiento ON lotes(fecha_vencimiento);
CREATE INDEX IF NOT EXISTS idx_lotes_vacuna ON lotes(vacuna_id);
CREATE INDEX IF NOT EXISTS idx_turnos_fecha ON turnos(fecha_turno);

-- Usuario admin (password: admin123 con BCrypt)
INSERT INTO usuarios (username, password, rol)
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMy.MrJXq6YqJq6YqJq6YqJq6YqJq6YqJq6', 'ADMIN')
ON DUPLICATE KEY UPDATE username = username;
