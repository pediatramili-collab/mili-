package com.bdcine.sgpa.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una etapa dentro de un proyecto de animacion.
 * Demuestra: herencia, polimorfismo (calcularAvance usa las tareas),
 * estructura de datos ArrayList, iteracion con for-each.
 */
public class Etapa extends ElementoProduccion {

    // ── Enum tipo de etapa ────────────────────────────────────────────────────
    public enum TipoEtapa {
        PREPRODUCCION, PRODUCCION, POSTPRODUCCION;

        @Override
        public String toString() {
            return switch (this) {
                case PREPRODUCCION  -> "Preproduccion";
                case PRODUCCION     -> "Produccion";
                case POSTPRODUCCION -> "Postproduccion";
            };
        }
    }

    // ── Atributos ─────────────────────────────────────────────────────────────
    private TipoEtapa    tipoEtapa;
    private int          orden;
    private List<Tarea>  tareas;   // estructura de datos: ArrayList

    // ── Constructor ───────────────────────────────────────────────────────────
    public Etapa(int id, String nombre, TipoEtapa tipo, int orden,
                 LocalDate inicio, LocalDate finPlan) {
        super(id, nombre, inicio, finPlan);
        this.tipoEtapa = tipo;
        this.orden     = orden;
        this.tareas    = new ArrayList<>();  // inicializacion de estructura de datos
    }

    // ── Implementacion de metodos abstractos ──────────────────────────────────
    /**
     * POLIMORFISMO: Etapa calcula su avance como promedio del avance
     * de todas sus tareas (estructura repetitiva for-each).
     */
    @Override
    public double calcularAvance() {
        if (tareas.isEmpty()) return 0.0;

        double suma = 0;
        for (Tarea t : tareas) {    // estructura repetitiva
            suma += t.calcularAvance();  // polimorfismo en accion
        }
        return suma / tareas.size();
    }

    /**
     * POLIMORFISMO: implementacion propia de getDescripcion().
     */
    @Override
    public String getDescripcion() {
        return String.format(
            "Etapa [%d] %-25s | Tipo: %-14s | Estado: %-11s | Avance: %.1f%% | Tareas: %d",
            orden, getNombre(), tipoEtapa, getEstado(),
            calcularAvance(), tareas.size());
    }

    @Override
    public String toString() {
        return String.format("[Etapa #%d - Orden %d] %s (%s) — %.1f%%",
                getId(), orden, getNombre(), tipoEtapa, calcularAvance());
    }

    // ── Gestion de tareas ─────────────────────────────────────────────────────
    public void agregarTarea(Tarea t) {
        if (t == null) throw new IllegalArgumentException("La tarea no puede ser null.");
        tareas.add(t);
    }

    public boolean eliminarTarea(int idTarea) {
        return tareas.removeIf(t -> t.getId() == idTarea);
    }

    public Tarea buscarTarea(int idTarea) {
        for (Tarea t : tareas) {         // busqueda lineal
            if (t.getId() == idTarea) return t;
        }
        return null;
    }

    public List<Tarea> getTareas()     { return new ArrayList<>(tareas); }  // copia defensiva
    public int getCantidadTareas()     { return tareas.size(); }
    public TipoEtapa getTipoEtapa()    { return tipoEtapa; }
    public int getOrden()              { return orden; }
    public void setOrden(int o)        { this.orden = o; }
}
