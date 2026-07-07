package com.bdcine.sgpa.model;

import java.time.LocalDate;

/** Clase abstracta intermedia: elementos con estado y fechas. */
public abstract class ElementoProduccion extends EntidadBase {

    private LocalDate fechaInicio;
    private LocalDate fechaFinPlan;
    private LocalDate fechaFinReal;

    protected ElementoProduccion(int id, String nombre,
                                 LocalDate fechaInicio, LocalDate fechaFinPlan) {
        super(id, nombre);
        this.fechaInicio  = fechaInicio;
        this.fechaFinPlan = fechaFinPlan;
    }

    /** Metodo ABSTRACTO: cada subclase calcula su avance a su manera. */
    public abstract double calcularAvance();

    /** Verifica si el elemento esta vencido. */
    public boolean estaVencido() {
        return fechaFinPlan != null
            && LocalDate.now().isAfter(fechaFinPlan)
            && calcularAvance() < 100.0;
    }

    public LocalDate getFechaInicio()           { return fechaInicio; }
    public void      setFechaInicio(LocalDate d){ this.fechaInicio = d; }
    public LocalDate getFechaFinPlan()          { return fechaFinPlan; }
    public void      setFechaFinPlan(LocalDate d){ this.fechaFinPlan = d; }
    public LocalDate getFechaFinReal()          { return fechaFinReal; }
    public void      setFechaFinReal(LocalDate d){ this.fechaFinReal = d; }
}
