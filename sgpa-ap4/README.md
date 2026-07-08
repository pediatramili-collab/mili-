# SGPA — Actividad Práctica N.° 4 (Entrega Final)
## Sistema de Gestión de Producción Animada — BD Cine S.A.

### Tecnologías
- Java SE 17+
- MySQL 8.x
- JDBC con `mysql-connector-j 8.x`
- Sin frameworks externos

### Patrones de diseño aplicados
- **DAO** (Data Access Object): separa persistencia de lógica de negocio.
- **Singleton**: `DBConnection` mantiene una única instancia.
- **MVC**: arquitectura en tres capas.

### Instalación
```bash
# 1. Crear la base de datos
mysql -u root -p < sql/schema.sql

# 2. Configurar credenciales en src/com/bdcine/sgpa/util/DBConnection.java
#    (USER y PASSWORD)

# 3. Descargar mysql-connector-j-8.x.jar y colocarlo en lib/

# 4. Compilar
mkdir -p out
find src -name "*.java" | xargs javac -cp "lib/*" -d out

# 5. Ejecutar
java -cp "out:lib/*" com.bdcine.sgpa.Main
```

### Estructura
```
sgpa-ap4/
├── README.md
├── sql/schema.sql                       ← Esquema MySQL completo
├── lib/mysql-connector-j-8.x.jar        ← Driver JDBC (descargar)
└── src/com/bdcine/sgpa/
    ├── Main.java
    ├── model/
    │   ├── EntidadBase.java             ← abstract
    │   ├── ElementoProduccion.java      ← abstract
    │   ├── Proyecto.java
    │   ├── Etapa.java
    │   └── Tarea.java
    ├── dao/
    │   ├── IRepository.java             ← INTERFAZ genérica
    │   ├── ProyectoDAO.java
    │   ├── EtapaDAO.java
    │   └── TareaDAO.java
    ├── service/
    │   └── GestorProyectos.java
    ├── view/
    │   └── ConsoleUI.java
    ├── exception/
    │   ├── SgpaException.java
    │   ├── EntidadNoEncontradaException.java
    │   └── ConexionBDException.java
    └── util/
        ├── DBConnection.java            ← Singleton
        └── Constantes.java              ← Arrays
```

### Demostración de conceptos
| Concepto | Dónde |
|---|---|
| Abstracción | `EntidadBase`, `ElementoProduccion` (abstract) |
| Encapsulamiento | `Tarea.setPorcentajeAvance()` con validación |
| Herencia | `Proyecto/Etapa/Tarea extends ElementoProduccion` |
| Polimorfismo | `calcularAvance()` con 3 implementaciones |
| **Interfaces** | `IRepository<T>` implementada por todos los DAOs |
| **Patrón DAO** | `ProyectoDAO`, `EtapaDAO`, `TareaDAO` |
| **Patrón Singleton** | `DBConnection` con instancia única |
| **JDBC real** | `PreparedStatement`, `ResultSet` en cada DAO |
| **Excepciones** | Jerarquía propia: `SgpaException`, `ConexionBDException`, `EntidadNoEncontradaException` |
| **Arrays** | `Constantes.TIPOS_PROYECTO[]`, `ordenarPorNombreUsandoArray()` |
| **ArrayList** | `List<Proyecto>` en `GestorProyectos` y modelos |
