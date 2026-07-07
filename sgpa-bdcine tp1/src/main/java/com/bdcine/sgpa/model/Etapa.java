package com.bdcine.sgpa.model;

import java.time.LocalDate;

/**
 * Entidad Etapa — representa una fase de producción dentro de un proyecto.
 */
public class Etapa {

    public enum Tipo {
        PREPRODUCCION, PRODUCCION, POSTPRODUCCION;

        @Override
        public String toString() {
            return switch (this) {
                case PREPRODUCCION  -> "Preproducción";
                case PRODUCCION     -> "Producción";
                case POSTPRODUCCION -> "Postproducción";
            };
        }
    }

    public enum Estado {
        PENDIENTE, EN_CURSO, COMPLETADA, BLOQUEADA;

        @Override
        public String toString() {
            return switch (this) {
                case PENDIENTE  -> "Pendiente";
                case EN_CURSO   -> "En curso";
                case COMPLETADA -> "Completada";
                case BLOQUEADA  -> "Bloqueada";
            };
        }
    }

    // ── Atributos ─────────────────────────────────────────────────────────────
    private int       id;
    private int       idProyecto;
    private String    nombre;
    private Tipo      tipo;
    private int       orden;
    private LocalDate fechaInicio;
    private LocalDate fechaFinPlan;
    private LocalDate fechaFinReal;
    private Estado    estado;
    private String    descripcion;

    // ── Constructores ─────────────────────────────────────────────────────────
    public Etapa() {}

    public Etapa(int idProyecto, String nombre, Tipo tipo, int orden,
                 LocalDate fechaInicio, LocalDate fechaFinPlan) {
        this.idProyecto  = idProyecto;
        this.nombre      = nombre;
        this.tipo        = tipo;
        this.orden       = orden;
        this.fechaInicio = fechaInicio;
        this.fechaFinPlan = fechaFinPlan;
        this.estado      = Estado.PENDIENTE;
    }

    // ── Getters y Setters ─────────────────────────────────────────────────────
    public int getId()                       { return id; }
    public void setId(int id)                { this.id = id; }

    public int getIdProyecto()               { return idProyecto; }
    public void setIdProyecto(int p)         { this.idProyecto = p; }

    public String getNombre()                { return nombre; }
    public void setNombre(String n)          { this.nombre = n; }

    public Tipo getTipo()                    { return tipo; }
    public void setTipo(Tipo t)              { this.tipo = t; }

    public int getOrden()                    { return orden; }
    public void setOrden(int o)              { this.orden = o; }

    public LocalDate getFechaInicio()        { return fechaInicio; }
    public void setFechaInicio(LocalDate d)  { this.fechaInicio = d; }

    public LocalDate getFechaFinPlan()       { return fechaFinPlan; }
    public void setFechaFinPlan(LocalDate d) { this.fechaFinPlan = d; }

    public LocalDate getFechaFinReal()       { return fechaFinReal; }
    public void setFechaFinReal(LocalDate d) { this.fechaFinReal = d; }

    public Estado getEstado()                { return estado; }
    public void setEstado(Estado e)          { this.estado = e; }

    public String getDescripcion()           { return descripcion; }
    public void setDescripcion(String d)     { this.descripcion = d; }

    @Override
    public String toString() {
        return String.format("[%d] %s (%s) — %s", id, nombre, tipo, estado);
    }
}
