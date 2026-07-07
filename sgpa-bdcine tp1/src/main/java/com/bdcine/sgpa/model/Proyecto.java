package com.bdcine.sgpa.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad Proyecto — representa un proyecto de animación de BD Cine S.A.
 */
public class Proyecto {

    public enum Estado {
        PREPRODUCCION, PRODUCCION, POSTPRODUCCION, FINALIZADO, CANCELADO;

        @Override
        public String toString() {
            return switch (this) {
                case PREPRODUCCION  -> "Preproducción";
                case PRODUCCION     -> "Producción";
                case POSTPRODUCCION -> "Postproducción";
                case FINALIZADO     -> "Finalizado";
                case CANCELADO      -> "Cancelado";
            };
        }
    }

    public enum Tipo {
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
    private int         id;
    private String      nombre;
    private String      descripcion;
    private Tipo        tipo;
    private LocalDate   fechaInicio;
    private LocalDate   fechaFinPlan;
    private LocalDate   fechaFinReal;
    private BigDecimal  presupuesto;
    private Estado      estado;
    private Integer     idDirector;
    private String      observaciones;

    // ── Constructores ─────────────────────────────────────────────────────────
    public Proyecto() {}

    public Proyecto(String nombre, String descripcion, Tipo tipo,
                    LocalDate fechaInicio, LocalDate fechaFinPlan,
                    BigDecimal presupuesto) {
        this.nombre      = nombre;
        this.descripcion = descripcion;
        this.tipo        = tipo;
        this.fechaInicio = fechaInicio;
        this.fechaFinPlan = fechaFinPlan;
        this.presupuesto = presupuesto;
        this.estado      = Estado.PREPRODUCCION;
    }

    // ── Getters y Setters ─────────────────────────────────────────────────────
    public int getId()                      { return id; }
    public void setId(int id)               { this.id = id; }

    public String getNombre()               { return nombre; }
    public void setNombre(String n)         { this.nombre = n; }

    public String getDescripcion()          { return descripcion; }
    public void setDescripcion(String d)    { this.descripcion = d; }

    public Tipo getTipo()                   { return tipo; }
    public void setTipo(Tipo t)             { this.tipo = t; }

    public LocalDate getFechaInicio()       { return fechaInicio; }
    public void setFechaInicio(LocalDate d) { this.fechaInicio = d; }

    public LocalDate getFechaFinPlan()      { return fechaFinPlan; }
    public void setFechaFinPlan(LocalDate d){ this.fechaFinPlan = d; }

    public LocalDate getFechaFinReal()      { return fechaFinReal; }
    public void setFechaFinReal(LocalDate d){ this.fechaFinReal = d; }

    public BigDecimal getPresupuesto()      { return presupuesto; }
    public void setPresupuesto(BigDecimal p){ this.presupuesto = p; }

    public Estado getEstado()               { return estado; }
    public void setEstado(Estado e)         { this.estado = e; }

    public Integer getIdDirector()          { return idDirector; }
    public void setIdDirector(Integer id)   { this.idDirector = id; }

    public String getObservaciones()        { return observaciones; }
    public void setObservaciones(String o)  { this.observaciones = o; }

    @Override
    public String toString() {
        return String.format("[%d] %s — %s (%s)", id, nombre, tipo, estado);
    }
}
