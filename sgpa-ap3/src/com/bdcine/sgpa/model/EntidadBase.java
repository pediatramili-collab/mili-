package com.bdcine.sgpa.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Clase abstracta base para todas las entidades del SGPA.
 * Demuestra: ABSTRACCION y ENCAPSULAMIENTO.
 *
 * - Abstraccion: define el contrato comun (getDescripcion()) que cada subclase
 *   debe implementar a su manera.
 * - Encapsulamiento: todos los atributos son privados; el acceso se
 *   realiza exclusivamente mediante getters y setters con validacion.
 */
public abstract class EntidadBase {

    // ── Atributos privados (encapsulamiento) ──────────────────────────────────
    private int       id;
    private String    nombre;
    private LocalDate fechaCreacion;

    protected static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ── Constructor completo ──────────────────────────────────────────────────
    public EntidadBase(int id, String nombre) {
        this.id            = id;
        this.nombre        = validarNombre(nombre);
        this.fechaCreacion = LocalDate.now();
    }

    // ── Metodo abstracto (abstraccion) ─────────────────────────────────────────
    /**
     * Cada subclase describe sus propias caracteristicas.
     * Esto es ABSTRACCION: se define el "que" sin imponer el "como".
     */
    public abstract String getDescripcion();

    // ── Metodo concreto reutilizable (herencia/template) ──────────────────────
    /**
     * toString() ofrece una representacion comun para todas las entidades.
     * Las subclases pueden sobreescribirlo (polimorfismo).
     */
    @Override
    public String toString() {
        return String.format("[ID: %d] %s — Creado: %s",
                id, nombre, fechaCreacion.format(FMT));
    }

    // ── Validacion interna ────────────────────────────────────────────────────
    private String validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio.");
        }
        return nombre.trim();
    }

    // ── Getters y Setters (encapsulamiento) ───────────────────────────────────
    public int getId()                 { return id; }
    public void setId(int id)          { this.id = id; }

    public String getNombre()          { return nombre; }
    public void setNombre(String n)    { this.nombre = validarNombre(n); }

    public LocalDate getFechaCreacion(){ return fechaCreacion; }
}
