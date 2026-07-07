package com.bdcine.sgpa.model;

import java.time.LocalDate;

/**
 * Representa una tarea especifica dentro de una etapa de produccion.
 * Demuestra: herencia de ElementoProduccion, implementacion de metodos
 * abstractos, encapsulamiento, constructores sobrecargados (overloading).
 */
public class Tarea extends ElementoProduccion {

    // ── Enum de prioridad ─────────────────────────────────────────────────────
    public enum Prioridad {
        BAJA, MEDIA, ALTA, CRITICA;

        @Override
        public String toString() {
            return switch (this) {
                case BAJA    -> "Baja";
                case MEDIA   -> "Media";
                case ALTA    -> "Alta";
                case CRITICA -> "CRITICA";
            };
        }
    }

    // ── Atributos ─────────────────────────────────────────────────────────────
    private int       porcentajeAvance;
    private Prioridad prioridad;
    private String    responsable;

    // ── Constructor completo ──────────────────────────────────────────────────
    public Tarea(int id, String nombre, LocalDate inicio, LocalDate finPlan,
                 Prioridad prioridad, String responsable) {
        super(id, nombre, inicio, finPlan);
        this.prioridad        = prioridad;
        this.responsable      = responsable;
        this.porcentajeAvance = 0;
    }

    // ── Constructor sobrecargado (overloading) ─────────────────────────────────
    /** Constructor simplificado con prioridad MEDIA por defecto. */
    public Tarea(int id, String nombre, LocalDate inicio, LocalDate finPlan) {
        this(id, nombre, inicio, finPlan, Prioridad.MEDIA, "Sin asignar");
    }

    // ── Implementacion de metodos abstractos (polimorfismo) ───────────────────
    /**
     * POLIMORFISMO: Tarea implementa calcularAvance() retornando
     * directamente su porcentaje (es la unidad basica del avance).
     */
    @Override
    public double calcularAvance() {
        return porcentajeAvance;
    }

    /**
     * POLIMORFISMO: sobreescribe getDescripcion() de EntidadBase.
     */
    @Override
    public String getDescripcion() {
        return String.format(
            "Tarea: %-30s | Avance: %3d%% | Prioridad: %-7s | Estado: %-11s | Resp: %s",
            getNombre(), porcentajeAvance, prioridad, getEstado(), responsable);
    }

    /**
     * Sobreescritura de toString() (polimorfismo).
     */
    @Override
    public String toString() {
        return String.format("[Tarea #%d] %s — %d%% (%s)",
                getId(), getNombre(), porcentajeAvance, prioridad);
    }

    // ── Setter con validacion (encapsulamiento) ───────────────────────────────
    public void setPorcentajeAvance(int p) {
        if (p < 0 || p > 100) {
            throw new IllegalArgumentException(
                "El avance debe estar entre 0 y 100. Valor recibido: " + p);
        }
        if (p < this.porcentajeAvance) {
            throw new IllegalArgumentException(
                "El avance no puede disminuir. Actual: " + this.porcentajeAvance);
        }
        this.porcentajeAvance = p;
        if (p == 100) setEstado(Estado.COMPLETADO);
        else if (p > 0) setEstado(Estado.EN_CURSO);
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public int getPorcentajeAvance()  { return porcentajeAvance; }
    public Prioridad getPrioridad()   { return prioridad; }
    public void setPrioridad(Prioridad p) { this.prioridad = p; }
    public String getResponsable()    { return responsable; }
    public void setResponsable(String r){ this.responsable = r; }
}
