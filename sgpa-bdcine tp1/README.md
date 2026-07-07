# SGPA — Sistema de Gestión de Producción Animada
### BD Cine S.A. | Seminario de Práctica de Informática

---

## Descripción
Prototipo operacional de un sistema de gestión de producción para la productora de animación BD Cine S.A. Permite administrar proyectos de animación, sus etapas y tareas, siguiendo el **Proceso Unificado de Desarrollo (PUD)**.

## Tecnologías
| Capa | Tecnología |
|---|---|
| Lenguaje | Java SE 17+ |
| Base de datos | MySQL 8.x |
| GUI | Java Swing |
| Conectividad | JDBC (`mysql-connector-j`) |
| Arquitectura | MVC (Modelo–Vista–Controlador) |

## Estructura del proyecto
```
sgpa-bdcine/
├── sql/
│   └── schema.sql              ← Esquema y datos de prueba
└── src/main/java/com/bdcine/sgpa/
    ├── Main.java                ← Punto de entrada
    ├── model/
    │   ├── Proyecto.java
    │   ├── Etapa.java
    │   └── Tarea.java
    ├── dao/
    │   ├── ProyectoDAO.java
    │   ├── EtapaDAO.java
    │   └── TareaDAO.java
    ├── service/
    │   ├── ProyectoService.java
    │   └── ServiceException.java
    ├── view/
    │   ├── MainView.java
    │   └── ProyectoDialog.java
    └── util/
        └── DBConnection.java
```

## Instrucciones de instalación

### 1. Base de datos
```sql
-- Ejecutar en MySQL Workbench o desde consola:
mysql -u root -p < sql/schema.sql
```

### 2. Dependencias Java
Descargar [mysql-connector-j-8.x.jar](https://dev.mysql.com/downloads/connector/j/) y agregarlo al classpath del proyecto en tu IDE.

### 3. Configuración de conexión
Editar `src/main/java/com/bdcine/sgpa/util/DBConnection.java`:
```java
private static final String URL      = "jdbc:mysql://localhost:3306/sgpa_bdcine...";
private static final String USER     = "tu_usuario";
private static final String PASSWORD = "tu_contraseña";
```

### 4. Compilar y ejecutar
Desde IntelliJ IDEA o Eclipse:
- Importar como proyecto Java
- Agregar `mysql-connector-j` como librería externa
- Ejecutar `Main.java`

## Módulos del prototipo
- ✅ **Gestión de Proyectos** — alta, edición, eliminación, avance de estado
- ✅ **Gestión de Etapas** — CRUD completo por proyecto
- ✅ **Gestión de Tareas** — CRUD con control de avance
- ⏳ **Reportes** — pendiente para Fase 2
- ⏳ **Gestión de usuarios con autenticación** — pendiente para Fase 2

## Autores
Trabajo Práctico N.° 1 — Análisis y Diseño de Software  
Seminario de Práctica de Informática — 2025
