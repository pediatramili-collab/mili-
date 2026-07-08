package com.bdcine.sgpa.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Etapa extends ElementoProduccion {

    public enum TipoEtapa { PREPRODUCCION, PRODUCCION, POSTPRODUCCION }
    public enum Estado    { PENDIENTE, EN_CURSO, COMPLETADA, BLOQUEADA }

    private int          idProyecto;
    private TipoEtapa    tipo;
    private int          orden;
    private Estado       estado;
    private List<Tarea>  tareas;   // ArrayList: coleccion dinamica de tareas

    public Etapa(int id, int idProyecto, String nombre, TipoEtapa tipo,
                 int orden, LocalDate inicio, LocalDate finPlan, Estado estado) {
        super(id, nombre, inicio, finPlan);
        this.idProyecto = idProyecto;
        this.tipo       = tipo;
        this.orden      = orden;
        this.estado     = estado;
        this.tareas     = new ArrayList<>();
    }

    @Override
    public double calcularAvance() {
        if (tareas.isEmpty()) return 0.0;
        double suma = 0;
        for (Tarea t : tareas) suma += t.calcularAvance();
        return suma / tareas.size();
    }

    @Override
    public String getDescripcion() {
        return String.format("Etapa #%d %-25s | %-14s | %-11s | %.1f%%",
                orden, getNombre(), tipo, estado, calcularAvance());
    }

    public void agregarTarea(Tarea t) {
        if (t == null) throw new IllegalArgumentException("Tarea null.");
        tareas.add(t);
    }
    public List<Tarea> getTareas()        { return new ArrayList<>(tareas); }
    public int         getIdProyecto()    { return idProyecto; }
    public void        setIdProyecto(int i){ this.idProyecto = i; }
    public TipoEtapa   getTipo()          { return tipo; }
    public void        setTipo(TipoEtapa t){ this.tipo = t; }
    public int         getOrden()         { return orden; }
    public void        setOrden(int o)    { this.orden = o; }
    public Estado      getEstado()        { return estado; }
    public void        setEstado(Estado e){ this.estado = e; }
}
