package com.bdcine.sgpa.model;

import java.time.LocalDate;

public class Tarea extends ElementoProduccion {

    public enum Estado    { PENDIENTE, EN_CURSO, EN_REVISION, COMPLETADA, BLOQUEADA }
    public enum Prioridad { BAJA, MEDIA, ALTA, CRITICA }

    private int       idEtapa;
    private int       porcentajeAvance;
    private Estado    estado;
    private Prioridad prioridad;

    public Tarea(int id, int idEtapa, String nombre, LocalDate inicio,
                 LocalDate finPlan, int porcentajeAvance,
                 Estado estado, Prioridad prioridad) {
        super(id, nombre, inicio, finPlan);
        this.idEtapa          = idEtapa;
        setPorcentajeAvance(porcentajeAvance);
        this.estado           = estado;
        this.prioridad        = prioridad;
    }

    /** Constructor sobrecargado. */
    public Tarea(int idEtapa, String nombre,
                 LocalDate inicio, LocalDate finPlan, Prioridad prioridad) {
        this(0, idEtapa, nombre, inicio, finPlan, 0, Estado.PENDIENTE, prioridad);
    }

    @Override
    public double calcularAvance() { return porcentajeAvance; }

    @Override
    public String getDescripcion() {
        return String.format("Tarea: %-25s | Avance: %3d%% | Prioridad: %-7s | %s",
                getNombre(), porcentajeAvance, prioridad, estado);
    }

    /** Encapsulamiento: validacion de negocio en el setter. */
    public void setPorcentajeAvance(int p) {
        if (p < 0 || p > 100)
            throw new IllegalArgumentException("Avance debe estar entre 0 y 100.");
        this.porcentajeAvance = p;
        if (p == 100) this.estado = Estado.COMPLETADA;
        else if (p > 0 && this.estado == Estado.PENDIENTE) this.estado = Estado.EN_CURSO;
    }

    public int       getIdEtapa()          { return idEtapa; }
    public void      setIdEtapa(int i)     { this.idEtapa = i; }
    public int       getPorcentajeAvance() { return porcentajeAvance; }
    public Estado    getEstado()           { return estado; }
    public void      setEstado(Estado e)   { this.estado = e; }
    public Prioridad getPrioridad()        { return prioridad; }
    public void      setPrioridad(Prioridad p) { this.prioridad = p; }
}
