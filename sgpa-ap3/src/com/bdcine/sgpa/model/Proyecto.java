package com.bdcine.sgpa.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un proyecto de animacion en BD Cine S.A.
 *
 * Demuestra:
 * - HERENCIA: extiende ElementoProduccion.
 * - POLIMORFISMO: sobreescribe calcularAvance() y getDescripcion().
 * - Comparable<Proyecto>: permite ordenacion con algoritmos de ordenamiento.
 * - Estructura de datos ArrayList<Etapa>.
 */
public class Proyecto extends ElementoProduccion implements Comparable<Proyecto> {

    // ── Enum tipo de proyecto ─────────────────────────────────────────────────
    public enum TipoProyecto {
        LARGOMETRAJE, CORTOMETRAJE, SERIE, PUBLICIDAD, OTRO;

        @Override
        public String toString() {
            return switch (this) {
                case LARGOMETRAJE -> "Largometraje";
                case CORTOMETRAJE -> "Cortometraje";
                case SERIE        -> "Serie";
                case PUBLICIDAD   -> "Publicidad";
                case OTRO         -> "Otro";
            };
        }
    }

    // ── Atributos ─────────────────────────────────────────────────────────────
    private TipoProyecto   tipo;
    private BigDecimal     presupuesto;
    private String         director;
    private List<Etapa>    etapas;   // estructura de datos: ArrayList

    // ── Constructor completo ──────────────────────────────────────────────────
    public Proyecto(int id, String nombre, TipoProyecto tipo,
                    LocalDate inicio, LocalDate finPlan,
                    BigDecimal presupuesto, String director) {
        super(id, nombre, inicio, finPlan);
        this.tipo        = tipo;
        this.presupuesto = presupuesto;
        this.director    = director;
        this.etapas      = new ArrayList<>();
    }

    // ── Constructor sobrecargado (overloading) ─────────────────────────────────
    /** Constructor simplificado sin presupuesto ni director definidos. */
    public Proyecto(int id, String nombre, TipoProyecto tipo,
                    LocalDate inicio, LocalDate finPlan) {
        this(id, nombre, tipo, inicio, finPlan, BigDecimal.ZERO, "Por definir");
    }

    // ── Implementacion de metodos abstractos (polimorfismo) ───────────────────
    /**
     * POLIMORFISMO: Proyecto calcula su avance como promedio del avance
     * de sus etapas (que a su vez promedian sus tareas).
     * Estructura repetitiva + llamada polimorfica.
     */
    @Override
    public double calcularAvance() {
        if (etapas.isEmpty()) return 0.0;

        double suma = 0;
        for (Etapa e : etapas) {
            suma += e.calcularAvance();   // polimorfismo: Etapa.calcularAvance()
        }
        return suma / etapas.size();
    }

    @Override
    public String getDescripcion() {
        return String.format(
            "Proyecto: %-30s | Tipo: %-13s | Estado: %-11s | " +
            "Avance: %5.1f%% | Dir: %s",
            getNombre(), tipo, getEstado(), calcularAvance(), director);
    }

    @Override
    public String toString() {
        return String.format(
            "[#%d] %-30s (%s) — %s — Avance: %.1f%%",
            getId(), getNombre(), tipo, getEstado(), calcularAvance());
    }

    // ── Comparable: permite ordenacion por nombre ──────────────────────────────
    /**
     * Implementacion de Comparable<Proyecto> para algoritmos de ordenacion.
     * Ordena por nombre alfabeticamente (ignorando mayusculas).
     */
    @Override
    public int compareTo(Proyecto otro) {
        return this.getNombre().compareToIgnoreCase(otro.getNombre());
    }

    // ── Gestion de etapas ─────────────────────────────────────────────────────
    public void agregarEtapa(Etapa e) {
        if (e == null) throw new IllegalArgumentException("La etapa no puede ser null.");
        etapas.add(e);
    }

    public boolean eliminarEtapa(int idEtapa) {
        return etapas.removeIf(e -> e.getId() == idEtapa);
    }

    public Etapa buscarEtapa(int idEtapa) {
        for (Etapa e : etapas) {
            if (e.getId() == idEtapa) return e;
        }
        return null;
    }

    // ── Getters y Setters ─────────────────────────────────────────────────────
    public TipoProyecto getTipo()          { return tipo; }
    public void setTipo(TipoProyecto t)    { this.tipo = t; }

    public BigDecimal getPresupuesto()     { return presupuesto; }
    public void setPresupuesto(BigDecimal p){ this.presupuesto = p; }

    public String getDirector()            { return director; }
    public void setDirector(String d)      { this.director = d; }

    public List<Etapa> getEtapas()         { return new ArrayList<>(etapas); }
    public int getCantidadEtapas()         { return etapas.size(); }
}
