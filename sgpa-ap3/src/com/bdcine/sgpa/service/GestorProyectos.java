package com.bdcine.sgpa.service;

import com.bdcine.sgpa.exception.EntidadNoEncontradaException;
import com.bdcine.sgpa.exception.SgpaException;
import com.bdcine.sgpa.model.Etapa;
import com.bdcine.sgpa.model.Proyecto;
import com.bdcine.sgpa.model.Tarea;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Gestor central de proyectos del SGPA.
 *
 * Demuestra:
 * - Estructura de datos: ArrayList como coleccion principal.
 * - Algoritmo de ordenacion: Bubble Sort implementado manualmente.
 * - Algoritmo de busqueda: busqueda binaria sobre lista ordenada.
 * - Manejo de excepciones: lanzamiento y captura.
 * - Estructuras de control: for, while, if/else, switch.
 */
public class GestorProyectos {

    // ── Estructura de datos principal ─────────────────────────────────────────
    private List<Proyecto> proyectos;
    private int            proximoIdProyecto;
    private int            proximoIdEtapa;
    private int            proximoIdTarea;

    // ── Constructor ───────────────────────────────────────────────────────────
    public GestorProyectos() {
        this.proyectos          = new ArrayList<>();
        this.proximoIdProyecto  = 1;
        this.proximoIdEtapa     = 1;
        this.proximoIdTarea     = 1;
    }

    // ── CRUD Proyectos ────────────────────────────────────────────────────────
    public Proyecto registrarProyecto(Proyecto p) throws SgpaException {
        if (p == null) throw new SgpaException("El proyecto no puede ser null.");
        p.setId(proximoIdProyecto++);
        proyectos.add(p);
        return p;
    }

    public Proyecto buscarProyectoPorId(int id) throws EntidadNoEncontradaException {
        for (Proyecto p : proyectos) {       // busqueda lineal
            if (p.getId() == id) return p;
        }
        throw new EntidadNoEncontradaException("Proyecto", id);
    }

    public boolean eliminarProyecto(int id) throws EntidadNoEncontradaException {
        buscarProyectoPorId(id);  // verifica existencia, lanza excepcion si no existe
        return proyectos.removeIf(p -> p.getId() == id);
    }

    public List<Proyecto> listarProyectos() {
        return new ArrayList<>(proyectos);   // copia defensiva
    }

    // ── CRUD Etapas ───────────────────────────────────────────────────────────
    public Etapa agregarEtapa(int idProyecto, Etapa etapa)
            throws EntidadNoEncontradaException, SgpaException {
        Proyecto p = buscarProyectoPorId(idProyecto);
        etapa.setId(proximoIdEtapa++);
        p.agregarEtapa(etapa);
        return etapa;
    }

    // ── CRUD Tareas ───────────────────────────────────────────────────────────
    public Tarea agregarTarea(int idProyecto, int idEtapa, Tarea tarea)
            throws EntidadNoEncontradaException, SgpaException {
        Proyecto p = buscarProyectoPorId(idProyecto);
        Etapa e = p.buscarEtapa(idEtapa);
        if (e == null) throw new EntidadNoEncontradaException("Etapa", idEtapa);
        tarea.setId(proximoIdTarea++);
        e.agregarTarea(tarea);
        return tarea;
    }

    public void actualizarAvanceTarea(int idProyecto, int idEtapa, int idTarea, int avance)
            throws EntidadNoEncontradaException, SgpaException {
        Proyecto p = buscarProyectoPorId(idProyecto);
        Etapa e = p.buscarEtapa(idEtapa);
        if (e == null) throw new EntidadNoEncontradaException("Etapa", idEtapa);
        Tarea t = e.buscarTarea(idTarea);
        if (t == null) throw new EntidadNoEncontradaException("Tarea", idTarea);

        try {
            t.setPorcentajeAvance(avance);   // puede lanzar IllegalArgumentException
        } catch (IllegalArgumentException ex) {
            throw new SgpaException(ex.getMessage());
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    // ALGORITMO DE ORDENACION: Bubble Sort (implementacion manual)
    // ══════════════════════════════════════════════════════════════════════════
    /**
     * Ordena la lista de proyectos por nombre (alfabetico) usando Bubble Sort.
     * Demuestra: algoritmo clasico con estructuras repetitivas anidadas,
     * comparacion mediante Comparable<Proyecto>.
     *
     * Complejidad temporal: O(n²) — apropiado para demostracion academica.
     */
    public List<Proyecto> ordenarPorNombreBubbleSort() {
        List<Proyecto> copia = new ArrayList<>(proyectos);
        int n = copia.size();
        boolean intercambio;

        for (int i = 0; i < n - 1; i++) {           // estructura repetitiva externa
            intercambio = false;
            for (int j = 0; j < n - i - 1; j++) {   // estructura repetitiva interna
                if (copia.get(j).compareTo(copia.get(j + 1)) > 0) {  // Comparable
                    // Intercambio
                    Proyecto temp  = copia.get(j);
                    copia.set(j,   copia.get(j + 1));
                    copia.set(j+1, temp);
                    intercambio = true;
                }
            }
            if (!intercambio) break;  // optimizacion: lista ya ordenada
        }
        return copia;
    }

    /**
     * Ordena proyectos por avance descendente usando Collections.sort + Comparator.
     * Demuestra uso de la API de colecciones de Java.
     */
    public List<Proyecto> ordenarPorAvanceDesc() {
        List<Proyecto> copia = new ArrayList<>(proyectos);
        Collections.sort(copia,
            Comparator.comparingDouble(Proyecto::calcularAvance).reversed());
        return copia;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // ALGORITMO DE BUSQUEDA: Busqueda Binaria
    // ══════════════════════════════════════════════════════════════════════════
    /**
     * Busca un proyecto por nombre usando busqueda binaria sobre lista ordenada.
     * Precondicion: la lista debe estar ordenada alfabeticamente.
     * Complejidad: O(log n).
     *
     * @return el Proyecto encontrado, o null si no existe.
     */
    public Proyecto busquedaBinariaPorNombre(String nombre) {
        List<Proyecto> ordenados = ordenarPorNombreBubbleSort();
        int izq = 0, der = ordenados.size() - 1;

        while (izq <= der) {               // estructura repetitiva
            int medio = (izq + der) / 2;
            int cmp = ordenados.get(medio).getNombre()
                               .compareToIgnoreCase(nombre);

            if (cmp == 0) {
                return ordenados.get(medio);   // encontrado
            } else if (cmp < 0) {
                izq = medio + 1;               // buscar en mitad derecha
            } else {
                der = medio - 1;               // buscar en mitad izquierda
            }
        }
        return null;  // no encontrado
    }

    // ── Estadisticas ──────────────────────────────────────────────────────────
    public int getCantidadProyectos()  { return proyectos.size(); }

    public double getAvancePromedio() {
        if (proyectos.isEmpty()) return 0.0;
        double suma = 0;
        for (Proyecto p : proyectos) suma += p.calcularAvance();
        return suma / proyectos.size();
    }
}
