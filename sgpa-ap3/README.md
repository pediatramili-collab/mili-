# SGPA — Actividad Práctica N.° 3
## Sistema de Gestión de Producción Animada — BD Cine S.A.

### Compilar y ejecutar
```bash
# Desde la raíz del proyecto:
mkdir -p out
find src -name "*.java" | xargs javac -d out
java -cp out com.bdcine.sgpa.Main
```

### Requisitos
- Java SE 17 o superior
- Sin dependencias externas (solo la JDK estándar)

### Estructura
```
src/com/bdcine/sgpa/
├── Main.java
├── model/
│   ├── EntidadBase.java        ← Abstracción + Encapsulamiento
│   ├── ElementoProduccion.java ← Herencia + Abstracción
│   ├── Proyecto.java           ← Herencia + Polimorfismo + Comparable
│   ├── Etapa.java              ← Herencia + Polimorfismo
│   └── Tarea.java              ← Herencia + Polimorfismo + Overloading
├── service/
│   └── GestorProyectos.java    ← Bubble Sort + Búsqueda Binaria + ArrayList
├── menu/
│   └── ConsoleUI.java          ← Menú + Excepciones + Estructuras de control
└── exception/
    ├── SgpaException.java
    └── EntidadNoEncontradaException.java
```
