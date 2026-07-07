package com.bdcine.sgpa.exception;

/** Se lanza ante fallos de conexion o ejecucion en la base de datos. */
public class ConexionBDException extends SgpaException {
    public ConexionBDException(String mensaje) {
        super(503, mensaje);
    }
    public ConexionBDException(String mensaje, Throwable causa) {
        super(503, mensaje, causa);
    }
}
