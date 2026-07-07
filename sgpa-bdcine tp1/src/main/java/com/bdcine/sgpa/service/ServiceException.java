package com.bdcine.sgpa.service;

/**
 * Excepción de negocio del SGPA.
 * Se lanza cuando se viola una regla de negocio o se produce un error en la capa de servicio.
 */
public class ServiceException extends Exception {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
