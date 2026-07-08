package com.bdcine.sgpa.view;

import com.bdcine.sgpa.exception.SgpaException;
import com.bdcine.sgpa.model.*;
import com.bdcine.sgpa.service.GestorProyectos;
import com.bdcine.sgpa.util.Constantes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Interfaz de usuario por consola con menu de seleccion.
 * Lee y escribe directamente contra la base de datos MySQL via el Gestor.
 *
 * Demuestra:
 * - Estructuras de control: do-while, switch, while, if/else, for-each.
 * - Manejo de excepciones: try-catch-finally.
 * - Uso de ARREGLOS (Constantes.TIPOS_PROYECTO, etc.) y ArrayList.
 */
public class ConsoleUI {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String SEP = "═".repeat(65);
    private static final String SUB = "─".repeat(65);

    private final GestorProyectos gestor;
    private final Scanner         sc;

    public ConsoleUI(GestorProyectos gestor) {
        this.gestor = gestor;
        this.sc     = new Scanner(System.in);
    }

    public void iniciar() {
        int op;
        do {
            mostrarMenuPrincipal();
            op = leerEntero("Opcion: ", 0, 4);
            switch (op) {
                case 1 -> menuProyectos();
                case 2 -> menuEtapasTareas();
                case 3 -> menuReportes();
                case 4 -> menuDemoOOP();
                case 0 -> System.out.println("\n  Cerrando SGPA. ¡Hasta pronto!");
            }
        } while (op != 0);
        sc.close();
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n" + SEP);
        System.out.println("  SGPA — BD Cine S.A.   |   Conectado a MySQL");
        System.out.println(SEP);
        System.out.println("  1. Gestion de Proyectos (alta/listar/buscar/eliminar)");
        System.out.println("  2. Gestion de Etapas y Tareas");
        System.out.println("  3. Reportes y Ordenamiento");
        System.out.println("  4. Demostracion OOP + Polimorfismo");
        System.out.println("  0. Salir");
        System.out.println(SEP);
    }

    // ═════════════════════════════════════════════════════════════════════
    // PROYECTOS
    // ═════════════════════════════════════════════════════════════════════
    private void menuProyectos() {
        int op;
        do {
            header("PROYECTOS");
            System.out.println("  1. Nuevo proyecto");
            System.out.println("  2. Listar todos");
            System.out.println("  3. Ver detalle por ID");
            System.out.println("  4. Eliminar por ID");
            System.out.println("  0. Volver");
            sub();
            op = leerEntero("Opcion: ", 0, 4);
            switch (op) {
                case 1 -> nuevoProyecto();
                case 2 -> listarProyectos();
                case 3 -> verDetalleProyecto();
                case 4 -> eliminarProyecto();
            }
        } while (op != 0);
    }

    private void nuevoProyecto() {
        header("NUEVO PROYECTO");
        try {
            String nombre = leerTexto("Nombre: ");
            String desc   = leerTexto("Descripcion (opcional): ");

            // Uso del ARREGLO Constantes.TIPOS_PROYECTO
            System.out.println("  Tipos disponibles:");
            for (int i = 0; i < Constantes.TIPOS_PROYECTO.length; i++)
                System.out.printf("    %d. %s%n", i + 1, Constantes.TIPOS_PROYECTO[i]);

            int tipoIdx = leerEntero("Tipo: ", 1, Constantes.TIPOS_PROYECTO.length);
            Proyecto.TipoProyecto tipo =
                Proyecto.TipoProyecto.valueOf(Constantes.TIPOS_PROYECTO[tipoIdx - 1]);

            LocalDate ini = leerFecha("Fecha inicio (dd/mm/aaaa): ");
            LocalDate fin = leerFecha("Fecha fin plan (dd/mm/aaaa): ");
            String presStr = leerTexto("Presupuesto (0 si no se informa): ");
            BigDecimal pres = new BigDecimal(presStr.replace(",", "."));
            String idDirStr = leerTexto("ID Director (Enter si no aplica): ");
            Integer idDir = idDirStr.isBlank() ? null : Integer.parseInt(idDirStr);

            Proyecto p = new Proyecto(0, nombre, desc, tipo, ini, fin, pres,
                                       Proyecto.Estado.PREPRODUCCION, idDir);
            gestor.registrarProyecto(p);
            System.out.println("\n  ✔ Proyecto registrado con ID " + p.getId());

        } catch (SgpaException ex) {
            System.out.println("  ERROR: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            System.out.println("  ERROR: dato numerico invalido.");
        }
        pausa();
    }

    private void listarProyectos() {
        header("LISTADO DE PROYECTOS");
        try {
            List<Proyecto> lista = gestor.listarProyectos();
            if (lista.isEmpty()) System.out.println("  No hay proyectos.");
            else for (Proyecto p : lista) System.out.println("  " + p.getDescripcion());
        } catch (SgpaException ex) {
            System.out.println("  ERROR: " + ex.getMessage());
        }
        pausa();
    }

    private void verDetalleProyecto() {
        int id = leerEntero("ID proyecto: ", 1, Integer.MAX_VALUE);
        try {
            Proyecto p = gestor.buscarProyectoPorId(id);
            header("DETALLE: " + p.getNombre());
            System.out.println("  " + p.getDescripcion());
            // Polimorfismo: getDescripcion() en Etapa y Tarea
            List<Etapa> etapas = gestor.listarEtapasPorProyecto(id);
            for (Etapa e : etapas) System.out.println("    " + e.getDescripcion());
        } catch (SgpaException ex) {
            System.out.println("  ERROR: " + ex.getMessage());
        }
        pausa();
    }

    private void eliminarProyecto() {
        int id = leerEntero("ID a eliminar: ", 1, Integer.MAX_VALUE);
        try {
            gestor.eliminarProyecto(id);
            System.out.println("  ✔ Proyecto eliminado (CASCADE elimina etapas y tareas).");
        } catch (SgpaException ex) {
            System.out.println("  ERROR: " + ex.getMessage());
        }
        pausa();
    }

    // ═════════════════════════════════════════════════════════════════════
    // ETAPAS Y TAREAS
    // ═════════════════════════════════════════════════════════════════════
    private void menuEtapasTareas() {
        int op;
        do {
            header("ETAPAS Y TAREAS");
            System.out.println("  1. Agregar etapa a proyecto");
            System.out.println("  2. Agregar tarea a etapa");
            System.out.println("  3. Actualizar avance de tarea");
            System.out.println("  0. Volver");
            sub();
            op = leerEntero("Opcion: ", 0, 3);
            switch (op) {
                case 1 -> nuevaEtapa();
                case 2 -> nuevaTarea();
                case 3 -> actualizarAvance();
            }
        } while (op != 0);
    }

    private void nuevaEtapa() {
        try {
            int idProy = leerEntero("ID proyecto: ", 1, Integer.MAX_VALUE);
            String nombre = leerTexto("Nombre etapa: ");
            System.out.println("  Tipos: 1-Preproduccion  2-Produccion  3-Postproduccion");
            int t = leerEntero("Tipo: ", 1, 3);
            Etapa.TipoEtapa tipo = Etapa.TipoEtapa.values()[t - 1];
            int orden = leerEntero("Orden: ", 1, 99);
            LocalDate ini = leerFecha("Inicio (dd/mm/aaaa): ");
            LocalDate fin = leerFecha("Fin plan (dd/mm/aaaa): ");

            Etapa e = new Etapa(0, idProy, nombre, tipo, orden, ini, fin,
                                 Etapa.Estado.PENDIENTE);
            gestor.agregarEtapa(e);
            System.out.println("  ✔ Etapa creada con ID " + e.getId());
        } catch (SgpaException ex) {
            System.out.println("  ERROR: " + ex.getMessage());
        }
        pausa();
    }

    private void nuevaTarea() {
        try {
            int idEtapa = leerEntero("ID etapa: ", 1, Integer.MAX_VALUE);
            String nombre = leerTexto("Nombre tarea: ");
            LocalDate ini = leerFecha("Inicio: ");
            LocalDate fin = leerFecha("Fin plan: ");
            System.out.println("  Prioridades: 1-Baja  2-Media  3-Alta  4-Critica");
            int p = leerEntero("Prioridad: ", 1, 4);
            Tarea.Prioridad pri = Tarea.Prioridad.values()[p - 1];

            Tarea t = new Tarea(idEtapa, nombre, ini, fin, pri);
            gestor.agregarTarea(t);
            System.out.println("  ✔ Tarea creada con ID " + t.getId());
        } catch (SgpaException ex) {
            System.out.println("  ERROR: " + ex.getMessage());
        }
        pausa();
    }

    private void actualizarAvance() {
        try {
            int id = leerEntero("ID tarea: ", 1, Integer.MAX_VALUE);
            int pct = leerEntero("Nuevo avance (0-100): ", 0, 100);
            gestor.actualizarAvance(id, pct);
            System.out.println("  ✔ Avance actualizado.");
        } catch (SgpaException ex) {
            System.out.println("  ERROR: " + ex.getMessage());
        }
        pausa();
    }

    // ═════════════════════════════════════════════════════════════════════
    // REPORTES
    // ═════════════════════════════════════════════════════════════════════
    private void menuReportes() {
        int op;
        do {
            header("REPORTES");
            System.out.println("  1. Proyectos ordenados por nombre (usando Array)");
            System.out.println("  2. Proyectos ordenados por avance (usando ArrayList)");
            System.out.println("  0. Volver");
            sub();
            op = leerEntero("Opcion: ", 0, 2);
            switch (op) {
                case 1 -> reporteArrayOrden();
                case 2 -> reporteListaAvance();
            }
        } while (op != 0);
    }

    private void reporteArrayOrden() {
        header("PROYECTOS ORDENADOS POR NOMBRE (Bubble Sort sobre ARRAY)");
        try {
            Proyecto[] arr = gestor.ordenarPorNombreUsandoArray();
            for (int i = 0; i < arr.length; i++)
                System.out.printf("  %d. %s%n", i + 1, arr[i].getNombre());
        } catch (SgpaException ex) {
            System.out.println("  ERROR: " + ex.getMessage());
        }
        pausa();
    }

    private void reporteListaAvance() {
        header("PROYECTOS POR AVANCE DESCENDENTE (ArrayList + Comparator)");
        try {
            List<Proyecto> lista = gestor.ordenarPorAvanceDesc();
            for (Proyecto p : lista)
                System.out.printf("  [%.1f%%] %s%n", p.calcularAvance(), p.getNombre());
        } catch (SgpaException ex) {
            System.out.println("  ERROR: " + ex.getMessage());
        }
        pausa();
    }

    // ═════════════════════════════════════════════════════════════════════
    // DEMO OOP
    // ═════════════════════════════════════════════════════════════════════
    private void menuDemoOOP() {
        header("DEMOSTRACION DE POLIMORFISMO Y EXCEPCIONES");
        try {
            List<Proyecto> proyectos = gestor.listarProyectos();
            for (Proyecto p : proyectos) {
                // Polimorfismo: getDescripcion() en cada nivel
                System.out.println("\n>> " + p.getDescripcion());
                List<Etapa> etapas = gestor.listarEtapasPorProyecto(p.getId());
                for (Etapa e : etapas)
                    System.out.println("   -> " + e.getDescripcion());
            }

            System.out.println("\n  Demostrando captura de excepciones:");
            try {
                gestor.buscarProyectoPorId(99999);
            } catch (SgpaException ex) {
                System.out.println("  CAPTURADO: " + ex);
            } finally {
                System.out.println("  [finally] siempre se ejecuta.");
            }
        } catch (SgpaException ex) {
            System.out.println("  ERROR: " + ex.getMessage());
        }
        pausa();
    }

    // ═════════════════════════════════════════════════════════════════════
    // Helpers
    // ═════════════════════════════════════════════════════════════════════
    private void header(String t) {
        System.out.println("\n" + SEP);
        System.out.println("  " + t);
        System.out.println(SUB);
    }
    private void sub() { System.out.println(SUB); }
    private void pausa() {
        System.out.print("\n  [ENTER para continuar] ");
        sc.nextLine();
    }
    private String leerTexto(String prompt) {
        System.out.print("  " + prompt);
        return sc.nextLine().trim();
    }
    private int leerEntero(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print("  " + prompt);
                int v = Integer.parseInt(sc.nextLine().trim());
                if (v >= min && v <= max) return v;
                System.out.printf("  Ingrese entre %d y %d.%n", min, max);
            } catch (NumberFormatException ex) {
                System.out.println("  Debe ser un numero entero.");
            }
        }
    }
    private LocalDate leerFecha(String prompt) {
        while (true) {
            try {
                System.out.print("  " + prompt);
                return LocalDate.parse(sc.nextLine().trim(), FMT);
            } catch (DateTimeParseException ex) {
                System.out.println("  Formato invalido. Use dd/mm/aaaa.");
            }
        }
    }
}
