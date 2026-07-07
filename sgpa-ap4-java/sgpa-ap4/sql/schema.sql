-- =============================================================================
-- SGPA – Sistema de Gestión de Producción Animada
-- BD Cine S.A. — Esquema MySQL 8.x
-- =============================================================================

DROP DATABASE IF EXISTS sgpa_bdcine;
CREATE DATABASE sgpa_bdcine
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE sgpa_bdcine;

-- -----------------------------------------------------------------------------
-- PROFESIONAL
-- -----------------------------------------------------------------------------
CREATE TABLE profesional (
    id          INT NOT NULL AUTO_INCREMENT,
    nombre      VARCHAR(100) NOT NULL,
    apellido    VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    rol         ENUM('DIRECTOR','PRODUCTOR_EJECUTIVO','JEFE_PRODUCCION',
                     'ANIMADOR','ARTISTA_3D','SUPERVISOR','COMPOSITOR',
                     'EDITOR','ADMINISTRADOR') NOT NULL,
    disponible  TINYINT(1) NOT NULL DEFAULT 1,
    CONSTRAINT pk_profesional PRIMARY KEY (id)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- PROYECTO
-- -----------------------------------------------------------------------------
CREATE TABLE proyecto (
    id             INT NOT NULL AUTO_INCREMENT,
    nombre         VARCHAR(200) NOT NULL,
    descripcion    TEXT,
    tipo           ENUM('LARGOMETRAJE','CORTOMETRAJE','SERIE','PUBLICIDAD','OTRO')
                   NOT NULL,
    fecha_inicio   DATE NOT NULL,
    fecha_fin_plan DATE NOT NULL,
    fecha_fin_real DATE,
    presupuesto    DECIMAL(15,2),
    estado         ENUM('PREPRODUCCION','PRODUCCION','POSTPRODUCCION',
                        'FINALIZADO','CANCELADO')
                   NOT NULL DEFAULT 'PREPRODUCCION',
    id_director    INT,
    CONSTRAINT pk_proyecto PRIMARY KEY (id),
    CONSTRAINT fk_proj_dir FOREIGN KEY (id_director)
                REFERENCES profesional(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- ETAPA
-- -----------------------------------------------------------------------------
CREATE TABLE etapa (
    id             INT NOT NULL AUTO_INCREMENT,
    id_proyecto    INT NOT NULL,
    nombre         VARCHAR(150) NOT NULL,
    tipo           ENUM('PREPRODUCCION','PRODUCCION','POSTPRODUCCION') NOT NULL,
    orden          TINYINT NOT NULL DEFAULT 1,
    fecha_inicio   DATE NOT NULL,
    fecha_fin_plan DATE NOT NULL,
    fecha_fin_real DATE,
    estado         ENUM('PENDIENTE','EN_CURSO','COMPLETADA','BLOQUEADA')
                   NOT NULL DEFAULT 'PENDIENTE',
    CONSTRAINT pk_etapa     PRIMARY KEY (id),
    CONSTRAINT fk_etap_proy FOREIGN KEY (id_proyecto)
                REFERENCES proyecto(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- TAREA
-- -----------------------------------------------------------------------------
CREATE TABLE tarea (
    id                INT NOT NULL AUTO_INCREMENT,
    id_etapa          INT NOT NULL,
    nombre            VARCHAR(200) NOT NULL,
    fecha_inicio      DATE NOT NULL,
    fecha_fin_plan    DATE NOT NULL,
    fecha_fin_real    DATE,
    porcentaje_avance TINYINT NOT NULL DEFAULT 0,
    estado            ENUM('PENDIENTE','EN_CURSO','EN_REVISION',
                            'COMPLETADA','BLOQUEADA')
                      NOT NULL DEFAULT 'PENDIENTE',
    prioridad         ENUM('BAJA','MEDIA','ALTA','CRITICA')
                      NOT NULL DEFAULT 'MEDIA',
    CONSTRAINT pk_tarea     PRIMARY KEY (id),
    CONSTRAINT fk_tar_etap  FOREIGN KEY (id_etapa)
                REFERENCES etapa(id) ON DELETE CASCADE,
    CONSTRAINT chk_avance   CHECK (porcentaje_avance BETWEEN 0 AND 100)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- DATOS DE PRUEBA
-- -----------------------------------------------------------------------------
INSERT INTO profesional (nombre, apellido, email, rol) VALUES
    ('Ana','Gomez',      'agomez@bdcine.com',    'DIRECTOR'),
    ('Carlos','Herrera', 'cherrera@bdcine.com',  'PRODUCTOR_EJECUTIVO'),
    ('Lucia','Martinez', 'lmartinez@bdcine.com', 'JEFE_PRODUCCION'),
    ('Rodrigo','Paz',    'rpaz@bdcine.com',      'ANIMADOR');

INSERT INTO proyecto (nombre, descripcion, tipo, fecha_inicio, fecha_fin_plan,
                      presupuesto, estado, id_director) VALUES
    ('El Ultimo Bosque','Largometraje CGI sobre la Patagonia.',
     'LARGOMETRAJE','2026-03-01','2027-12-31', 2500000.00, 'PREPRODUCCION', 1),
    ('Serie Patagonik','Serie animada de 13 episodios.',
     'SERIE','2026-06-01','2027-06-30', 800000.00, 'PREPRODUCCION', 1);

INSERT INTO etapa (id_proyecto, nombre, tipo, orden, fecha_inicio, fecha_fin_plan) VALUES
    (1, 'Desarrollo de guion',  'PREPRODUCCION', 1, '2026-03-01', '2026-05-31'),
    (1, 'Modelado 3D',          'PRODUCCION',    2, '2026-06-01', '2026-12-31'),
    (2, 'Diseno de personajes', 'PREPRODUCCION', 1, '2026-06-01', '2026-08-31');

INSERT INTO tarea (id_etapa, nombre, fecha_inicio, fecha_fin_plan,
                   porcentaje_avance, prioridad) VALUES
    (1, 'Guion literario',  '2026-03-01', '2026-04-15', 80, 'ALTA'),
    (1, 'Guion tecnico',    '2026-04-16', '2026-05-31',  0, 'MEDIA'),
    (2, 'Modelado personajes', '2026-06-01', '2026-09-30', 0, 'ALTA');
