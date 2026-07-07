package com.bdcine.sgpa.exception;

/** Excepcion base del SGPA. */
public class SgpaException extends Exception {
    private final int codigo;
    public SgpaException(int codigo, String mensaje) {
        super(mensaje);
        this.codigo = codigo;
    }
    public SgpaException(int codigo, String mensaje, Throwable causa) {
        super(mensaje, causa);
        this.codigo = codigo;
    }
    public SgpaException(String mensaje) { this(100, mensaje); }
    public int getCodigo() { return codigo; }

    @Override
    public String toString() {
        return String.format("[Error %d] %s", codigo, getMessage());
    }
}
