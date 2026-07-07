package com.bdcine.sgpa.model;

import java.time.LocalDate;

/**
 * Clase abstracta que representa cualquier elemento del proceso de produccion
 * que tiene un estado, fechas y puede calcular su avance.
 *
 * Demuestra: HERENCIA (extiende EntidadBase) y ABSTRACCION (define
 * calcularAvance() que cada subclase implementa diferente).
 */
public abstract class ElementoProduccion extends EntidadBase {

    // ── Enum de estados (tipos de datos) ──────────────────────────────────────
    public enum Estado {
        PENDIENTE, EN_CURSO, COMPLETADO, CANCELADO;

        @Override
        public String toString() {
            return switch (this) {
                case PENDIENTE   -> "Pendiente";
                case EN_CURSO    -> "En curso";
                case COMPLETADO  -> "Completado";
                case CANCELADO   -> "Cancelado";
            };
        }
    }

    // ── Atributos privados ────────────────────────────────────────────────────
    private Estado    estado;
    private LocalDate fechaInicio;
    private LocalDate fechaFinPlan;
    private LocalDate fechaFinReal;

    // ── Constructor (llama al constructor padre con super()) ──────────────────
    public ElementoProduccion(int id, String nombre,
                              LocalDate fechaInicio, LocalDate fechaFinPlan) {
        super(id, nombre);   // invoca constructor de EntidadBase
        this.fechaInicio  = fechaInicio;
        this.fechaFinPlan = fechaFinPlan;
        this.estado       = Estado.PENDIENTE;
    }

    // ── Metodo abstracto: cada elemento calcula su avance diferente ─────────
    /**
     * ABSTRACCION: el contrato dice "debes poder calcular tu avance",
     * pero cada clase lo hace segun su logica propia.
     *
     * @return porcentaje de avance entre 0.0 y 100.0
     */
    public abstract double calcularAvance();

    // ── Metodo concreto heredado por todas las subclases ──────────────────────
    /**
     * Verifica si el elemento esta vencido (fin planificado superado y no completado).
     * Estructura condicional + uso de LocalDate.
     */
    public boolean estaVencido() {
        return fechaFinPlan != null
            && LocalDate.now().isAfter(fechaFinPlan)
            && estado != Estado.COMPLETADO
            && estado != Estado.CANCELADO;
    }

    /**
     * Avanza al siguiente estado en el flujo logico.
     * Estructura switch + validacion de estado.
     */
    public void avanzarEstado() {
        this.estado = switch (estado) {
            case PENDIENTE  -> Estado.EN_CURSO;
            case EN_CURSO   -> Estado.COMPLETADO;
            default         -> throw new IllegalStateException(
                "No se puede avanzar desde el estado: " + estado);
        };
    }

    // ── Getters y Setters ─────────────────────────────────────────────────────
    public Estado getEstado()                      { return estado; }
    public void setEstado(Estado e)                { this.estado = e; }

    public LocalDate getFechaInicio()              { return fechaInicio; }
    public void setFechaInicio(LocalDate d)        { this.fechaInicio = d; }

    public LocalDate getFechaFinPlan()             { return fechaFinPlan; }
    public void setFechaFinPlan(LocalDate d)       { this.fechaFinPlan = d; }

    public LocalDate getFechaFinReal()             { return fechaFinReal; }
    public void setFechaFinReal(LocalDate d)       { this.fechaFinReal = d; }
}
