package com.bdcine.sgpa.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Proyecto de animacion. Extiende ElementoProduccion (HERENCIA).
 *
 * Demuestra:
 * - POLIMORFISMO: implementa calcularAvance() y getDescripcion().
 * - Constructor sobrecargado (overloading).
 * - Uso de ArrayList<Etapa> como coleccion dinamica.
 */
public class Proyecto extends ElementoProduccion {

    public enum TipoProyecto { LARGOMETRAJE, CORTOMETRAJE, SERIE, PUBLICIDAD, OTRO }
    public enum Estado       { PREPRODUCCION, PRODUCCION, POSTPRODUCCION, FINALIZADO, CANCELADO }

    // ── Atributos coincidentes con la tabla proyecto ──────────────────────
    private String       descripcion;
    private TipoProyecto tipo;
    private BigDecimal   presupuesto;
    private Estado       estado;
    private Integer      idDirector;

    /** Coleccion DINAMICA de etapas (ArrayList). */
    private List<Etapa>  etapas;

    // ── Constructor completo ──────────────────────────────────────────────
    public Proyecto(int id, String nombre, String descripcion,
                    TipoProyecto tipo, LocalDate inicio, LocalDate finPlan,
                    BigDecimal presupuesto, Estado estado, Integer idDirector) {
        super(id, nombre, inicio, finPlan);
        this.descripcion = descripcion;
        this.tipo        = tipo;
        this.presupuesto = presupuesto;
        this.estado      = estado;
        this.idDirector  = idDirector;
        this.etapas      = new ArrayList<>();  // inicializacion
    }

    /** Constructor SOBRECARGADO simplificado. */
    public Proyecto(String nombre, TipoProyecto tipo,
                    LocalDate inicio, LocalDate finPlan) {
        this(0, nombre, "", tipo, inicio, finPlan,
             BigDecimal.ZERO, Estado.PREPRODUCCION, null);
    }

    // ── Polimorfismo ──────────────────────────────────────────────────────
    @Override
    public double calcularAvance() {
        if (etapas.isEmpty()) return 0.0;
        double suma = 0;
        for (Etapa e : etapas) suma += e.calcularAvance();
        return suma / etapas.size();
    }

    @Override
    public String getDescripcion() {
        return String.format("Proyecto: %-25s | Tipo: %-12s | Estado: %-13s | Avance: %5.1f%%",
                getNombre(), tipo, estado, calcularAvance());
    }

    // ── Gestion de etapas ─────────────────────────────────────────────────
    public void agregarEtapa(Etapa e) {
        if (e == null) throw new IllegalArgumentException("Etapa no puede ser null.");
        etapas.add(e);
    }
    public List<Etapa> getEtapas()     { return new ArrayList<>(etapas); }

    // ── Getters / Setters ─────────────────────────────────────────────────
    public String       getDescripcionTexto() { return descripcion; }
    public void         setDescripcionTexto(String d) { this.descripcion = d; }
    public TipoProyecto getTipo()             { return tipo; }
    public void         setTipo(TipoProyecto t) { this.tipo = t; }
    public BigDecimal   getPresupuesto()      { return presupuesto; }
    public void         setPresupuesto(BigDecimal p) { this.presupuesto = p; }
    public Estado       getEstado()           { return estado; }
    public void         setEstado(Estado e)   { this.estado = e; }
    public Integer      getIdDirector()       { return idDirector; }
    public void         setIdDirector(Integer i) { this.idDirector = i; }
}
