package com.bdcine.sgpa.exception;

/** Se lanza cuando no se encuentra una entidad por su ID. */
public class EntidadNoEncontradaException extends SgpaException {
    public EntidadNoEncontradaException(String tipo, int id) {
        super(404, tipo + " con ID " + id + " no encontrado.");
    }
}
