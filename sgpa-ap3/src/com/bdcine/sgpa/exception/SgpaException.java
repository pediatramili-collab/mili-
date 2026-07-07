package com.bdcine.sgpa.exception;

/**
 * Excepcion base del SGPA.
 * Demuestra: MANEJO DE EXCEPCIONES personalizado con jerarquia propia.
 */
public class SgpaException extends Exception {

    private final int codigo;

    public SgpaException(int codigo, String mensaje) {
        super(mensaje);
        this.codigo = codigo;
    }

    public SgpaException(String mensaje) {
        this(100, mensaje);
    }

    public int getCodigo() { return codigo; }

    @Override
    public String toString() {
        return String.format("[Error %d] %s", codigo, getMessage());
    }
}
