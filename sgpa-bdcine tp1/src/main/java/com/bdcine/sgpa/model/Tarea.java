package com.bdcine.sgpa.model;

import java.time.LocalDate;

/**
 * Entidad Tarea — representa una tarea específica dentro de una etapa.
 */
public class Tarea {

    public enum Estado {
        PENDIENTE, EN_CURSO, EN_REVISION, COMPLETADA, BLOQUEADA;

        @Override
        public String toString() {
            return switch (this) {
                case PENDIENTE   -> "Pendiente";
                case EN_CURSO    -> "En curso";
                case EN_REVISION -> "En revisión";
                case COMPLETADA  -> "Completada";
                case BLOQUEADA   -> "Bloqueada";
            };
        }
    }

    public enum Prioridad {
        BAJA, MEDIA, ALTA, CRITICA;

        @Override
        public String toString() {
            return switch (this) {
                case BAJA    -> "Baja";
                case MEDIA   -> "Media";
                case ALTA    -> "Alta";
                case CRITICA -> "Crítica";
            };
        }
    }

    // ── Atributos ─────────────────────────────────────────────────────────────
    private int       id;
    private int       idEtapa;
    private String    nombre;
    private String    descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFinPlan;
    private LocalDate fechaFinReal;
    private int       porcentajeAvance;
    private Estado    estado;
    private Prioridad prioridad;

    // ── Constructores ─────────────────────────────────────────────────────────
    public Tarea() {}

    public Tarea(int idEtapa, String nombre, String descripcion,
                 LocalDate fechaInicio, LocalDate fechaFinPlan, Prioridad prioridad) {
        this.idEtapa          = idEtapa;
        this.nombre           = nombre;
        this.descripcion      = descripcion;
        this.fechaInicio      = fechaInicio;
        this.fechaFinPlan     = fechaFinPlan;
        this.prioridad        = prioridad;
        this.estado           = Estado.PENDIENTE;
        this.porcentajeAvance = 0;
    }

    // ── Getters y Setters ─────────────────────────────────────────────────────
    public int getId()                         { return id; }
    public void setId(int id)                  { this.id = id; }

    public int getIdEtapa()                    { return idEtapa; }
    public void setIdEtapa(int e)              { this.idEtapa = e; }

    public String getNombre()                  { return nombre; }
    public void setNombre(String n)            { this.nombre = n; }

    public String getDescripcion()             { return descripcion; }
    public void setDescripcion(String d)       { this.descripcion = d; }

    public LocalDate getFechaInicio()          { return fechaInicio; }
    public void setFechaInicio(LocalDate d)    { this.fechaInicio = d; }

    public LocalDate getFechaFinPlan()         { return fechaFinPlan; }
    public void setFechaFinPlan(LocalDate d)   { this.fechaFinPlan = d; }

    public LocalDate getFechaFinReal()         { return fechaFinReal; }
    public void setFechaFinReal(LocalDate d)   { this.fechaFinReal = d; }

    public int getPorcentajeAvance()           { return porcentajeAvance; }
    public void setPorcentajeAvance(int p)     {
        if (p < 0 || p > 100) throw new IllegalArgumentException("El avance debe estar entre 0 y 100.");
        this.porcentajeAvance = p;
    }

    public Estado getEstado()                  { return estado; }
    public void setEstado(Estado e)            { this.estado = e; }

    public Prioridad getPrioridad()            { return prioridad; }
    public void setPrioridad(Prioridad p)      { this.prioridad = p; }

    @Override
    public String toString() {
        return String.format("[%d] %s — %d%% — %s [%s]",
                id, nombre, porcentajeAvance, estado, prioridad);
    }
}
