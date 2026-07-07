package com.bdcine.sgpa.menu;

import com.bdcine.sgpa.exception.EntidadNoEncontradaException;
import com.bdcine.sgpa.exception.SgpaException;
import com.bdcine.sgpa.model.*;
import com.bdcine.sgpa.service.GestorProyectos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Interfaz de usuario por consola con menu de seleccion.
 *
 * Demuestra:
 * - MENU DE SELECCION con estructuras do-while y switch.
 * - MANEJO DE EXCEPCIONES con try-catch-finally.
 * - ESTRUCTURAS CONDICIONALES y repetitivas.
 * - Uso de Scanner para entrada de datos.
 * - Polimorfismo al llamar getDescripcion() sobre distintas subclases.
 */
public class ConsoleUI {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String LINEA = "═".repeat(65);
    private static final String LINEA_S = "─".repeat(65);

    private final GestorProyectos gestor;
    private final Scanner         sc;

    // ── Constructor ───────────────────────────────────────────────────────────
    public ConsoleUI(GestorProyectos gestor) {
        this.gestor = gestor;
        this.sc     = new Scanner(System.in);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // MENU PRINCIPAL
    // ══════════════════════════════════════════════════════════════════════════
    public void iniciar() {
        cargarDatosDemostracion();
        int opcion;

        do {                           // estructura repetitiva do-while
            mostrarMenuPrincipal();
            opcion = leerEnteroSeguro("Seleccione una opcion: ", 0, 5);

            switch (opcion) {          // estructura switch (menu de seleccion)
                case 1 -> menuProyectos();
                case 2 -> menuEtapas();
                case 3 -> menuTareas();
                case 4 -> menuReportes();
                case 5 -> menuDemoOOP();
                case 0 -> System.out.println("\n  Cerrando SGPA. ¡Hasta pronto!");
                default -> System.out.println("  Opcion no valida.");
            }
        } while (opcion != 0);

        sc.close();
    }

    // ── Menu de Proyectos ─────────────────────────────────────────────────────
    private void menuProyectos() {
        int op;
        do {
            header("GESTION DE PROYECTOS");
            System.out.println("  1. Registrar nuevo proyecto");
            System.out.println("  2. Listar todos los proyectos");
            System.out.println("  3. Ver detalle de un proyecto");
            System.out.println("  4. Eliminar proyecto");
            System.out.println("  0. Volver al menu principal");
            linea();
            op = leerEnteroSeguro("Opcion: ", 0, 4);

            switch (op) {
                case 1 -> registrarProyecto();
                case 2 -> listarProyectos();
                case 3 -> verDetalleProyecto();
                case 4 -> eliminarProyecto();
            }
        } while (op != 0);
    }

    // ── Menu de Etapas ────────────────────────────────────────────────────────
    private void menuEtapas() {
        int op;
        do {
            header("GESTION DE ETAPAS");
            System.out.println("  1. Agregar etapa a proyecto");
            System.out.println("  2. Listar etapas de un proyecto");
            System.out.println("  0. Volver");
            linea();
            op = leerEnteroSeguro("Opcion: ", 0, 2);
            switch (op) {
                case 1 -> agregarEtapa();
                case 2 -> listarEtapas();
            }
        } while (op != 0);
    }

    // ── Menu de Tareas ────────────────────────────────────────────────────────
    private void menuTareas() {
        int op;
        do {
            header("GESTION DE TAREAS");
            System.out.println("  1. Agregar tarea a etapa");
            System.out.println("  2. Registrar avance de tarea");
            System.out.println("  0. Volver");
            linea();
            op = leerEnteroSeguro("Opcion: ", 0, 2);
            switch (op) {
                case 1 -> agregarTarea();
                case 2 -> registrarAvance();
            }
        } while (op != 0);
    }

    // ── Menu de Reportes ──────────────────────────────────────────────────────
    private void menuReportes() {
        int op;
        do {
            header("REPORTES");
            System.out.println("  1. Proyectos ordenados por nombre (Bubble Sort)");
            System.out.println("  2. Proyectos ordenados por avance (desc)");
            System.out.println("  3. Buscar proyecto por nombre (Busqueda Binaria)");
            System.out.println("  4. Estadisticas generales");
            System.out.println("  0. Volver");
            linea();
            op = leerEnteroSeguro("Opcion: ", 0, 4);
            switch (op) {
                case 1 -> reporteOrdenadoPorNombre();
                case 2 -> reporteOrdenadoPorAvance();
                case 3 -> buscarPorNombre();
                case 4 -> estadisticas();
            }
        } while (op != 0);
    }

    // ── Menu Demo OOP ─────────────────────────────────────────────────────────
    private void menuDemoOOP() {
        header("DEMOSTRACION DE CONCEPTOS OOP");
        System.out.println("  Demostrando POLIMORFISMO:");
        System.out.println("  Llamando getDescripcion() sobre distintas subclases...\n");

        // POLIMORFISMO: la misma llamada getDescripcion() produce resultados
        // distintos segun el tipo real del objeto (Proyecto, Etapa o Tarea).
        List<Proyecto> lista = gestor.listarProyectos();
        for (Proyecto p : lista) {
            System.out.println("  >> " + p.getDescripcion());   // polimorfismo
            for (Etapa e : p.getEtapas()) {
                System.out.println("     -> " + e.getDescripcion());  // polimorfismo
                for (Tarea t : e.getTareas()) {
                    System.out.println("        -- " + t.getDescripcion()); // polimorfismo
                }
            }
        }

        System.out.println("\n  Demostrando MANEJO DE EXCEPCIONES:");
        try {
            System.out.println("  Intentando buscar proyecto con ID 999...");
            gestor.buscarProyectoPorId(999);  // lanzara excepcion
        } catch (EntidadNoEncontradaException ex) {
            System.out.println("  CAPTURADO -> " + ex);
        } finally {
            System.out.println("  [finally] Bloque finally siempre se ejecuta.\n");
        }

        pausa();
    }

    // ══════════════════════════════════════════════════════════════════════════
    // ACCIONES
    // ══════════════════════════════════════════════════════════════════════════
    private void registrarProyecto() {
        header("NUEVO PROYECTO");
        try {
            String nombre = leerTexto("Nombre del proyecto: ");
            System.out.println("  Tipos: 1-Largometraje  2-Cortometraje  3-Serie  4-Publicidad  5-Otro");
            int tipoIdx  = leerEnteroSeguro("Tipo: ", 1, 5);
            Proyecto.TipoProyecto tipo = Proyecto.TipoProyecto.values()[tipoIdx - 1];
            LocalDate inicio  = leerFecha("Fecha inicio (dd/mm/aaaa): ");
            LocalDate fin     = leerFecha("Fecha fin plan (dd/mm/aaaa): ");
            String director   = leerTexto("Director: ");
            String presupStr  = leerTexto("Presupuesto (0 si no definido): ");

            BigDecimal presup = new BigDecimal(presupStr.replace(",", "."));
            Proyecto p = new Proyecto(0, nombre, tipo, inicio, fin, presup, director);
            gestor.registrarProyecto(p);
            System.out.println("\n  ✔ Proyecto registrado con ID " + p.getId());

        } catch (SgpaException ex) {
            System.out.println("  ERROR: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            System.out.println("  ERROR: El presupuesto debe ser un numero valido.");
        }
        pausa();
    }

    private void listarProyectos() {
        header("LISTADO DE PROYECTOS");
        List<Proyecto> lista = gestor.listarProyectos();
        if (lista.isEmpty()) {
            System.out.println("  No hay proyectos registrados.");
        } else {
            for (Proyecto p : lista) {     // estructura repetitiva
                System.out.println("  " + p);
            }
        }
        pausa();
    }

    private void verDetalleProyecto() {
        int id = leerEnteroSeguro("ID del proyecto: ", 1, Integer.MAX_VALUE);
        try {
            Proyecto p = gestor.buscarProyectoPorId(id);
            header("DETALLE: " + p.getNombre());
            System.out.println("  " + p.getDescripcion());
            System.out.println("  Etapas (" + p.getCantidadEtapas() + "):");
            for (Etapa e : p.getEtapas()) {
                System.out.println("    " + e);
                for (Tarea t : e.getTareas()) {
                    System.out.println("      " + t);
                }
            }
        } catch (EntidadNoEncontradaException ex) {
            System.out.println("  ERROR: " + ex.getMessage());
        }
        pausa();
    }

    private void eliminarProyecto() {
        int id = leerEnteroSeguro("ID del proyecto a eliminar: ", 1, Integer.MAX_VALUE);
        try {
            gestor.eliminarProyecto(id);
            System.out.println("  ✔ Proyecto eliminado correctamente.");
        } catch (EntidadNoEncontradaException ex) {
            System.out.println("  ERROR: " + ex.getMessage());
        }
        pausa();
    }

    private void agregarEtapa() {
        try {
            listarProyectos();
            int idProy = leerEnteroSeguro("ID del proyecto: ", 1, Integer.MAX_VALUE);
            String nombre = leerTexto("Nombre de la etapa: ");
            System.out.println("  Tipos: 1-Preproduccion  2-Produccion  3-Postproduccion");
            int tipoIdx  = leerEnteroSeguro("Tipo: ", 1, 3);
            Etapa.TipoEtapa tipo = Etapa.TipoEtapa.values()[tipoIdx - 1];
            int orden    = leerEnteroSeguro("Orden: ", 1, 99);
            LocalDate ini = leerFecha("Inicio (dd/mm/aaaa): ");
            LocalDate fin = leerFecha("Fin plan (dd/mm/aaaa): ");

            Etapa e = new Etapa(0, nombre, tipo, orden, ini, fin);
            gestor.agregarEtapa(idProy, e);
            System.out.println("  ✔ Etapa agregada con ID " + e.getId());

        } catch (SgpaException ex) {
            System.out.println("  ERROR: " + ex.getMessage());
        }
        pausa();
    }

    private void listarEtapas() {
        int idProy = leerEnteroSeguro("ID del proyecto: ", 1, Integer.MAX_VALUE);
        try {
            Proyecto p = gestor.buscarProyectoPorId(idProy);
            header("ETAPAS DE: " + p.getNombre());
            if (p.getEtapas().isEmpty()) {
                System.out.println("  Sin etapas registradas.");
            } else {
                for (Etapa e : p.getEtapas()) System.out.println("  " + e.getDescripcion());
            }
        } catch (EntidadNoEncontradaException ex) {
            System.out.println("  ERROR: " + ex.getMessage());
        }
        pausa();
    }

    private void agregarTarea() {
        try {
            int idProy = leerEnteroSeguro("ID del proyecto: ", 1, Integer.MAX_VALUE);
            Proyecto p = gestor.buscarProyectoPorId(idProy);
            for (Etapa e : p.getEtapas()) System.out.println("  " + e);

            int idEtapa = leerEnteroSeguro("ID de la etapa: ", 1, Integer.MAX_VALUE);
            String nombre = leerTexto("Nombre de la tarea: ");
            String resp   = leerTexto("Responsable: ");
            System.out.println("  Prioridades: 1-Baja  2-Media  3-Alta  4-Critica");
            int priIdx = leerEnteroSeguro("Prioridad: ", 1, 4);
            Tarea.Prioridad pri = Tarea.Prioridad.values()[priIdx - 1];
            LocalDate ini = leerFecha("Inicio (dd/mm/aaaa): ");
            LocalDate fin = leerFecha("Fin plan (dd/mm/aaaa): ");

            Tarea t = new Tarea(0, nombre, ini, fin, pri, resp);
            gestor.agregarTarea(idProy, idEtapa, t);
            System.out.println("  ✔ Tarea agregada con ID " + t.getId());

        } catch (SgpaException ex) {
            System.out.println("  ERROR: " + ex.getMessage());
        }
        pausa();
    }

    private void registrarAvance() {
        try {
            int idProy  = leerEnteroSeguro("ID del proyecto: ", 1, Integer.MAX_VALUE);
            int idEtapa = leerEnteroSeguro("ID de la etapa: ", 1, Integer.MAX_VALUE);
            int idTarea = leerEnteroSeguro("ID de la tarea: ", 1, Integer.MAX_VALUE);
            int avance  = leerEnteroSeguro("Nuevo avance (0-100): ", 0, 100);
            gestor.actualizarAvanceTarea(idProy, idEtapa, idTarea, avance);
            System.out.println("  ✔ Avance actualizado.");
        } catch (SgpaException ex) {
            System.out.println("  ERROR: " + ex.getMessage());
        }
        pausa();
    }

    private void reporteOrdenadoPorNombre() {
        header("PROYECTOS ORDENADOS POR NOMBRE (Bubble Sort)");
        List<Proyecto> ordenados = gestor.ordenarPorNombreBubbleSort();
        for (int i = 0; i < ordenados.size(); i++) {   // for clasico
            System.out.printf("  %d. %s%n", i+1, ordenados.get(i));
        }
        pausa();
    }

    private void reporteOrdenadoPorAvance() {
        header("PROYECTOS ORDENADOS POR AVANCE (desc)");
        List<Proyecto> ordenados = gestor.ordenarPorAvanceDesc();
        for (Proyecto p : ordenados) {
            System.out.printf("  [%.1f%%] %s%n", p.calcularAvance(), p.getNombre());
        }
        pausa();
    }

    private void buscarPorNombre() {
        String nombre = leerTexto("Nombre a buscar: ");
        Proyecto result = gestor.busquedaBinariaPorNombre(nombre);
        if (result != null) {
            header("RESULTADO BUSQUEDA BINARIA");
            System.out.println("  " + result.getDescripcion());
        } else {
            System.out.println("  No se encontro ningun proyecto con ese nombre.");
        }
        pausa();
    }

    private void estadisticas() {
        header("ESTADISTICAS GENERALES");
        System.out.printf("  Total de proyectos : %d%n", gestor.getCantidadProyectos());
        System.out.printf("  Avance promedio    : %.1f%%%n", gestor.getAvancePromedio());
        pausa();
    }

    // ══════════════════════════════════════════════════════════════════════════
    // DATOS DE DEMOSTRACION
    // ══════════════════════════════════════════════════════════════════════════
    private void cargarDatosDemostracion() {
        try {
            // Proyecto 1
            Proyecto p1 = new Proyecto(0,"El Ultimo Bosque",
                Proyecto.TipoProyecto.LARGOMETRAJE,
                LocalDate.of(2025,3,1), LocalDate.of(2026,12,31),
                new BigDecimal("2500000"), "Ana Gomez");
            gestor.registrarProyecto(p1);

            Etapa e1 = new Etapa(0,"Desarrollo de guion",
                Etapa.TipoEtapa.PREPRODUCCION, 1,
                LocalDate.of(2025,3,1), LocalDate.of(2025,5,31));
            gestor.agregarEtapa(p1.getId(), e1);

            Tarea t1 = new Tarea(0,"Guion literario",
                LocalDate.of(2025,3,1), LocalDate.of(2025,4,15),
                Tarea.Prioridad.ALTA, "Carlos H.");
            gestor.agregarTarea(p1.getId(), e1.getId(), t1);
            gestor.actualizarAvanceTarea(p1.getId(), e1.getId(), t1.getId(), 80);

            Tarea t2 = new Tarea(0,"Guion tecnico",
                LocalDate.of(2025,4,16), LocalDate.of(2025,5,31),
                Tarea.Prioridad.MEDIA, "Carlos H.");
            gestor.agregarTarea(p1.getId(), e1.getId(), t2);
            gestor.actualizarAvanceTarea(p1.getId(), e1.getId(), t2.getId(), 30);

            // Proyecto 2
            Proyecto p2 = new Proyecto(0,"Serie Patagonik",
                Proyecto.TipoProyecto.SERIE,
                LocalDate.of(2025,6,1), LocalDate.of(2026,6,30),
                new BigDecimal("800000"), "Laura Torres");
            gestor.registrarProyecto(p2);

            Etapa e2 = new Etapa(0,"Diseno de personajes",
                Etapa.TipoEtapa.PREPRODUCCION, 1,
                LocalDate.of(2025,6,1), LocalDate.of(2025,8,31));
            gestor.agregarEtapa(p2.getId(), e2);

            Tarea t3 = new Tarea(0,"Concept art protagonista",
                LocalDate.of(2025,6,1), LocalDate.of(2025,7,15),
                Tarea.Prioridad.ALTA, "Valentina P.");
            gestor.agregarTarea(p2.getId(), e2.getId(), t3);
            gestor.actualizarAvanceTarea(p2.getId(), e2.getId(), t3.getId(), 100);

        } catch (SgpaException ex) {
            System.err.println("Error cargando datos de demo: " + ex.getMessage());
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // HELPERS DE INTERFAZ
    // ══════════════════════════════════════════════════════════════════════════
    private void mostrarMenuPrincipal() {
        System.out.println("\n" + LINEA);
        System.out.println("  SGPA — Sistema de Gestion de Produccion Animada");
        System.out.println("  BD Cine S.A.");
        System.out.println(LINEA);
        System.out.println("  1. Gestion de Proyectos");
        System.out.println("  2. Gestion de Etapas");
        System.out.println("  3. Gestion de Tareas");
        System.out.println("  4. Reportes y Ordenamiento");
        System.out.println("  5. Demostracion OOP (polimorfismo + excepciones)");
        System.out.println("  0. Salir");
        System.out.println(LINEA);
    }

    private void header(String titulo) {
        System.out.println("\n" + LINEA);
        System.out.println("  " + titulo);
        System.out.println(LINEA_S);
    }

    private void linea() { System.out.println(LINEA_S); }

    private void pausa() {
        System.out.print("\n  [ENTER para continuar] ");
        sc.nextLine();
    }

    private String leerTexto(String prompt) {
        System.out.print("  " + prompt);
        return sc.nextLine().trim();
    }

    private int leerEnteroSeguro(String prompt, int min, int max) {
        int valor = min - 1;
        boolean valido = false;
        while (!valido) {                              // estructura repetitiva while
            try {
                System.out.print("  " + prompt);
                valor = Integer.parseInt(sc.nextLine().trim());
                if (valor >= min && valor <= max) {
                    valido = true;
                } else {
                    System.out.printf("  Ingrese un valor entre %d y %d.%n", min, max);
                }
            } catch (NumberFormatException ex) {       // manejo de excepcion
                System.out.println("  Debe ingresar un numero entero.");
            }
        }
        return valor;
    }

    private LocalDate leerFecha(String prompt) {
        while (true) {
            try {
                System.out.print("  " + prompt);
                return LocalDate.parse(sc.nextLine().trim(), FMT);
            } catch (DateTimeParseException ex) {      // manejo de excepcion
                System.out.println("  Formato invalido. Use dd/mm/aaaa.");
            }
        }
    }
}
