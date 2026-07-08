package com.bdcine.sgpa.dao;

import com.bdcine.sgpa.exception.SgpaException;
import java.util.List;

/**
 * INTERFAZ genérica que define el contrato del patron DAO (Data Access Object).
 *
 * El patron DAO desacopla la logica de acceso a datos de la logica de negocio.
 * Esta interfaz parametrizada permite que cada DAO (ProyectoDAO, EtapaDAO,
 * TareaDAO, ProfesionalDAO) comparta una misma estructura de metodos CRUD.
 *
 * Demuestra el uso de INTERFACES (complementario a las clases abstractas),
 * GENERICOS (List<T>) y POLIMORFISMO basado en interfaces.
 *
 * @param <T> Tipo de entidad gestionada (Proyecto, Etapa, Tarea, Profesional).
 */
public interface IRepository<T> {

    /**
     * Inserta una entidad en la base de datos.
     * @return la entidad con su ID asignado.
     */
    T insertar(T entidad) throws SgpaException;

    /** Devuelve todas las entidades existentes. */
    List<T> listarTodos() throws SgpaException;

    /** Busca una entidad por su ID, o null si no existe. */
    T buscarPorId(int id) throws SgpaException;

    /** Actualiza una entidad existente. */
    boolean actualizar(T entidad) throws SgpaException;

    /** Elimina una entidad por su ID. */
    boolean eliminar(int id) throws SgpaException;
}
