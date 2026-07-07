-- =============================================================================
--  SGPA – Sistema de Gestión de Producción Animada
--  BD Cine S.A.
--  Base de datos: MySQL 8.x
--  Codificación : UTF-8
-- =============================================================================

CREATE DATABASE IF NOT EXISTS sgpa_bdcine
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE sgpa_bdcine;

-- -----------------------------------------------------------------------------
-- 1. PROFESIONAL
-- -----------------------------------------------------------------------------
CREATE TABLE profesional (
    id              INT             NOT NULL AUTO_INCREMENT,
    nombre          VARCHAR(100)    NOT NULL,
    apellido        VARCHAR(100)    NOT NULL,
    email           VARCHAR(150)    NOT NULL UNIQUE,
    rol             ENUM(
                        'DIRECTOR',
                        'PRODUCTOR_EJECUTIVO',
                        'JEFE_PRODUCCION',
                        'ANIMADOR',
                        'ARTISTA_3D',
                        'SUPERVISOR',
                        'COMPOSITOR',
                        'EDITOR',
                        'ADMINISTRADOR'
                    )               NOT NULL,
    disponible      TINYINT(1)      NOT NULL DEFAULT 1,
    fecha_alta      DATE            NOT NULL DEFAULT (CURRENT_DATE),
    CONSTRAINT pk_profesional PRIMARY KEY (id)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 2. USUARIO (extiende profesional con credenciales de acceso)
-- -----------------------------------------------------------------------------
CREATE TABLE usuario (
    id              INT             NOT NULL AUTO_INCREMENT,
    id_profesional  INT             NOT NULL,
    username        VARCHAR(60)     NOT NULL UNIQUE,
    password_hash   VARCHAR(255)    NOT NULL,          -- BCrypt hash
    perfil          ENUM(
                        'ADMIN',
                        'DIRECTOR',
                        'PRODUCTOR',
                        'OPERADOR'
                    )               NOT NULL DEFAULT 'OPERADOR',
    activo          TINYINT(1)      NOT NULL DEFAULT 1,
    ultimo_acceso   DATETIME                 DEFAULT NULL,
    CONSTRAINT pk_usuario          PRIMARY KEY (id),
    CONSTRAINT fk_usuario_prof     FOREIGN KEY (id_profesional)
                                   REFERENCES profesional(id)
                                   ON DELETE RESTRICT
                                   ON UPDATE CASCADE,
    CONSTRAINT uq_usuario_prof     UNIQUE (id_profesional)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 3. PROYECTO
-- -----------------------------------------------------------------------------
CREATE TABLE proyecto (
    id                  INT             NOT NULL AUTO_INCREMENT,
    nombre              VARCHAR(200)    NOT NULL,
    descripcion         TEXT,
    tipo                ENUM(
                            'LARGOMETRAJE',
                            'CORTOMETRAJE',
                            'SERIE',
                            'PUBLICIDAD',
                            'OTRO'
                        )               NOT NULL DEFAULT 'LARGOMETRAJE',
    fecha_inicio        DATE            NOT NULL,
    fecha_fin_plan      DATE            NOT NULL,
    fecha_fin_real      DATE                     DEFAULT NULL,
    presupuesto         DECIMAL(15,2)            DEFAULT NULL,
    estado              ENUM(
                            'PREPRODUCCION',
                            'PRODUCCION',
                            'POSTPRODUCCION',
                            'FINALIZADO',
                            'CANCELADO'
                        )               NOT NULL DEFAULT 'PREPRODUCCION',
    id_director         INT                      DEFAULT NULL,
    observaciones       TEXT,
    fecha_creacion      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP
                                        ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_proyecto          PRIMARY KEY (id),
    CONSTRAINT fk_proyecto_director FOREIGN KEY (id_director)
                                    REFERENCES profesional(id)
                                    ON DELETE SET NULL
                                    ON UPDATE CASCADE,
    CONSTRAINT chk_fechas_proyecto  CHECK (fecha_fin_plan >= fecha_inicio)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 4. ETAPA
-- -----------------------------------------------------------------------------
CREATE TABLE etapa (
    id                  INT             NOT NULL AUTO_INCREMENT,
    id_proyecto         INT             NOT NULL,
    nombre              VARCHAR(150)    NOT NULL,
    tipo                ENUM(
                            'PREPRODUCCION',
                            'PRODUCCION',
                            'POSTPRODUCCION'
                        )               NOT NULL,
    orden               TINYINT         NOT NULL DEFAULT 1,
    fecha_inicio        DATE            NOT NULL,
    fecha_fin_plan      DATE            NOT NULL,
    fecha_fin_real      DATE                     DEFAULT NULL,
    estado              ENUM(
                            'PENDIENTE',
                            'EN_CURSO',
                            'COMPLETADA',
                            'BLOQUEADA'
                        )               NOT NULL DEFAULT 'PENDIENTE',
    descripcion         TEXT,
    CONSTRAINT pk_etapa             PRIMARY KEY (id),
    CONSTRAINT fk_etapa_proyecto    FOREIGN KEY (id_proyecto)
                                    REFERENCES proyecto(id)
                                    ON DELETE CASCADE
                                    ON UPDATE CASCADE,
    CONSTRAINT chk_fechas_etapa     CHECK (fecha_fin_plan >= fecha_inicio),
    CONSTRAINT uq_etapa_orden       UNIQUE (id_proyecto, orden)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 5. TAREA
-- -----------------------------------------------------------------------------
CREATE TABLE tarea (
    id                  INT             NOT NULL AUTO_INCREMENT,
    id_etapa            INT             NOT NULL,
    nombre              VARCHAR(200)    NOT NULL,
    descripcion         TEXT,
    fecha_inicio        DATE            NOT NULL,
    fecha_fin_plan      DATE            NOT NULL,
    fecha_fin_real      DATE                     DEFAULT NULL,
    porcentaje_avance   TINYINT         NOT NULL DEFAULT 0,
    estado              ENUM(
                            'PENDIENTE',
                            'EN_CURSO',
                            'EN_REVISION',
                            'COMPLETADA',
                            'BLOQUEADA'
                        )               NOT NULL DEFAULT 'PENDIENTE',
    prioridad           ENUM(
                            'BAJA',
                            'MEDIA',
                            'ALTA',
                            'CRITICA'
                        )               NOT NULL DEFAULT 'MEDIA',
    CONSTRAINT pk_tarea             PRIMARY KEY (id),
    CONSTRAINT fk_tarea_etapa       FOREIGN KEY (id_etapa)
                                    REFERENCES etapa(id)
                                    ON DELETE CASCADE
                                    ON UPDATE CASCADE,
    CONSTRAINT chk_avance           CHECK (porcentaje_avance BETWEEN 0 AND 100),
    CONSTRAINT chk_fechas_tarea     CHECK (fecha_fin_plan >= fecha_inicio)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 6. ASIGNACION (relación Profesional ↔ Tarea)
-- -----------------------------------------------------------------------------
CREATE TABLE asignacion (
    id                  INT             NOT NULL AUTO_INCREMENT,
    id_tarea            INT             NOT NULL,
    id_profesional      INT             NOT NULL,
    fecha_asignacion    DATE            NOT NULL DEFAULT (CURRENT_DATE),
    horas_estimadas     DECIMAL(6,2)             DEFAULT NULL,
    horas_reales        DECIMAL(6,2)             DEFAULT NULL,
    observaciones       TEXT,
    CONSTRAINT pk_asignacion            PRIMARY KEY (id),
    CONSTRAINT fk_asign_tarea           FOREIGN KEY (id_tarea)
                                        REFERENCES tarea(id)
                                        ON DELETE CASCADE
                                        ON UPDATE CASCADE,
    CONSTRAINT fk_asign_profesional     FOREIGN KEY (id_profesional)
                                        REFERENCES profesional(id)
                                        ON DELETE RESTRICT
                                        ON UPDATE CASCADE,
    CONSTRAINT uq_asignacion            UNIQUE (id_tarea, id_profesional)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 7. HISTORIAL_AVANCE (auditoría de cambios en porcentaje de tareas)
-- -----------------------------------------------------------------------------
CREATE TABLE historial_avance (
    id                  INT             NOT NULL AUTO_INCREMENT,
    id_tarea            INT             NOT NULL,
    id_usuario          INT             NOT NULL,
    porcentaje_anterior TINYINT         NOT NULL,
    porcentaje_nuevo    TINYINT         NOT NULL,
    comentario          TEXT,
    fecha_registro      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_historial         PRIMARY KEY (id),
    CONSTRAINT fk_hist_tarea        FOREIGN KEY (id_tarea)
                                    REFERENCES tarea(id)
                                    ON DELETE CASCADE
                                    ON UPDATE CASCADE,
    CONSTRAINT fk_hist_usuario      FOREIGN KEY (id_usuario)
                                    REFERENCES usuario(id)
                                    ON DELETE RESTRICT
                                    ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =============================================================================
-- ÍNDICES adicionales para optimizar consultas frecuentes
-- =============================================================================
CREATE INDEX idx_proyecto_estado       ON proyecto(estado);
CREATE INDEX idx_etapa_proyecto        ON etapa(id_proyecto, tipo);
CREATE INDEX idx_tarea_etapa           ON tarea(id_etapa, estado);
CREATE INDEX idx_tarea_avance          ON tarea(porcentaje_avance);
CREATE INDEX idx_asignacion_prof       ON asignacion(id_profesional);
CREATE INDEX idx_historial_tarea       ON historial_avance(id_tarea);

-- =============================================================================
-- DATOS DE PRUEBA
-- =============================================================================

-- Profesionales
INSERT INTO profesional (nombre, apellido, email, rol) VALUES
    ('Ana',     'Gómez',     'agomez@bdcine.com',      'DIRECTOR'),
    ('Carlos',  'Herrera',   'cherrera@bdcine.com',    'PRODUCTOR_EJECUTIVO'),
    ('Lucía',   'Martínez',  'lmartinez@bdcine.com',   'JEFE_PRODUCCION'),
    ('Rodrigo', 'Paz',       'rpaz@bdcine.com',        'ANIMADOR'),
    ('Valentina','Torres',   'vtorres@bdcine.com',     'ARTISTA_3D'),
    ('Mateo',   'Silva',     'msilva@bdcine.com',      'COMPOSITOR'),
    ('Admin',   'Sistema',   'admin@bdcine.com',       'ADMINISTRADOR');

-- Usuarios (contraseña de prueba: "sgpa1234" — en producción usar BCrypt)
INSERT INTO usuario (id_profesional, username, password_hash, perfil) VALUES
    (1, 'agomez',    '$2a$12$dummyhashDirector000000000000000000000000000000000000', 'DIRECTOR'),
    (2, 'cherrera',  '$2a$12$dummyhashProductor00000000000000000000000000000000000', 'PRODUCTOR'),
    (3, 'lmartinez', '$2a$12$dummyhashJefe0000000000000000000000000000000000000000', 'DIRECTOR'),
    (4, 'rpaz',      '$2a$12$dummyhashAnimador000000000000000000000000000000000000', 'OPERADOR'),
    (5, 'vtorres',   '$2a$12$dummyhashArtista0000000000000000000000000000000000000', 'OPERADOR'),
    (7, 'admin',     '$2a$12$dummyhashAdmin000000000000000000000000000000000000000', 'ADMIN');

-- Proyecto de prueba
INSERT INTO proyecto (nombre, descripcion, tipo, fecha_inicio, fecha_fin_plan, presupuesto, estado, id_director) VALUES
    ('El Último Bosque', 'Largometraje animado CGI sobre la conservación ambiental en la Patagonia.', 
     'LARGOMETRAJE', '2025-03-01', '2026-12-31', 2500000.00, 'PREPRODUCCION', 1);

-- Etapas del proyecto
INSERT INTO etapa (id_proyecto, nombre, tipo, orden, fecha_inicio, fecha_fin_plan, estado) VALUES
    (1, 'Desarrollo de guión',        'PREPRODUCCION', 1, '2025-03-01', '2025-05-31', 'EN_CURSO'),
    (1, 'Diseño de personajes',       'PREPRODUCCION', 2, '2025-04-01', '2025-07-31', 'PENDIENTE'),
    (1, 'Storyboard y animática',     'PREPRODUCCION', 3, '2025-06-01', '2025-09-30', 'PENDIENTE'),
    (1, 'Modelado 3D',                'PRODUCCION',    4, '2025-08-01', '2026-02-28', 'PENDIENTE'),
    (1, 'Animación y rigging',        'PRODUCCION',    5, '2025-10-01', '2026-05-31', 'PENDIENTE'),
    (1, 'Iluminación y render',       'PRODUCCION',    6, '2026-02-01', '2026-08-31', 'PENDIENTE'),
    (1, 'Compositing y edición',      'POSTPRODUCCION',7, '2026-07-01', '2026-11-30', 'PENDIENTE'),
    (1, 'Mezcla de audio y doblaje',  'POSTPRODUCCION',8, '2026-09-01', '2026-12-15', 'PENDIENTE');

-- Tareas de la etapa 1
INSERT INTO tarea (id_etapa, nombre, descripcion, fecha_inicio, fecha_fin_plan, porcentaje_avance, estado, prioridad) VALUES
    (1, 'Redacción del guión literario',    'Primera versión completa del guión.',           '2025-03-01', '2025-04-15', 80, 'EN_CURSO',   'ALTA'),
    (1, 'Revisión y ajustes del guión',     'Correcciones según notas del director.',        '2025-04-16', '2025-05-15', 0,  'PENDIENTE',  'ALTA'),
    (1, 'Guión técnico (script breakdown)', 'Desglose técnico por escenas y planos.',        '2025-05-16', '2025-05-31', 0,  'PENDIENTE',  'MEDIA');

-- Asignaciones
INSERT INTO asignacion (id_tarea, id_profesional, fecha_asignacion, horas_estimadas) VALUES
    (1, 1, '2025-03-01', 120.00),
    (1, 3, '2025-03-01',  80.00),
    (2, 1, '2025-03-01',  60.00),
    (3, 3, '2025-03-01',  40.00);
