package com.bdcine.sgpa.model;

/**
 * Clase ABSTRACTA base del modelo del SGPA.
 *
 * Demuestra:
 * - ABSTRACCION: define el contrato getDescripcion() sin implementarlo.
 * - ENCAPSULAMIENTO: atributos privados con getters/setters validados.
 */
public abstract class EntidadBase {

    private int    id;
    private String nombre;

    /** Constructor protegido: solo invocable por las subclases. */
    protected EntidadBase(int id, String nombre) {
        this.id     = id;
        this.nombre = validarNombre(nombre);
    }

    /** Metodo ABSTRACTO: cada subclase describe sus caracteristicas. */
    public abstract String getDescripcion();

    @Override
    public String toString() {
        return String.format("[ID:%d] %s", id, nombre);
    }

    // ── Encapsulamiento: validacion en setter ─────────────────────────────
    private String validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("El nombre no puede estar vacio.");
        return nombre.trim();
    }

    public int    getId()             { return id; }
    public void   setId(int id)       { this.id = id; }
    public String getNombre()         { return nombre; }
    public void   setNombre(String n) { this.nombre = validarNombre(n); }
}
