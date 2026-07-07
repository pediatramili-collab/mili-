package com.bdcine.sgpa.service;

import com.bdcine.sgpa.dao.ProyectoDAO;
import com.bdcine.sgpa.model.Proyecto;

import java.sql.SQLException;
import java.util.List;

/**
 * Capa de servicio para Proyecto.
 * Centraliza la lógica de negocio y actúa como intermediario entre la vista y el DAO.
 */
public class ProyectoService {

    private final ProyectoDAO dao = new ProyectoDAO();

    /**
     * Registra un nuevo proyecto aplicando validaciones de negocio.
     */
    public Proyecto registrar(Proyecto proyecto) throws ServiceException {
        validar(proyecto);
        try {
            return dao.insertar(proyecto);
        } catch (SQLException e) {
            throw new ServiceException("Error al registrar el proyecto: " + e.getMessage(), e);
        }
    }

    /**
     * Devuelve todos los proyectos ordenados por fecha de inicio descendente.
     */
    public List<Proyecto> listarTodos() throws ServiceException {
        try {
            return dao.listarTodos();
        } catch (SQLException e) {
            throw new ServiceException("Error al listar proyectos: " + e.getMessage(), e);
        }
    }

    /**
     * Busca un proyecto por su ID.
     */
    public Proyecto buscarPorId(int id) throws ServiceException {
        try {
            Proyecto p = dao.buscarPorId(id);
            if (p == null) throw new ServiceException("No existe un proyecto con ID " + id);
            return p;
        } catch (SQLException e) {
            throw new ServiceException("Error al buscar el proyecto: " + e.getMessage(), e);
        }
    }

    /**
     * Filtra proyectos por estado.
     */
    public List<Proyecto> listarPorEstado(Proyecto.Estado estado) throws ServiceException {
        try {
            return dao.listarPorEstado(estado);
        } catch (SQLException e) {
            throw new ServiceException("Error al filtrar proyectos: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza un proyecto existente.
     */
    public void actualizar(Proyecto proyecto) throws ServiceException {
        validar(proyecto);
        try {
            if (!dao.actualizar(proyecto))
                throw new ServiceException("No se encontró el proyecto a actualizar.");
        } catch (SQLException e) {
            throw new ServiceException("Error al actualizar el proyecto: " + e.getMessage(), e);
        }
    }

    /**
     * Avanza el estado del proyecto al siguiente en el flujo de producción.
     */
    public void avanzarEstado(int idProyecto) throws ServiceException {
        Proyecto p = buscarPorId(idProyecto);
        Proyecto.Estado siguiente = switch (p.getEstado()) {
            case PREPRODUCCION  -> Proyecto.Estado.PRODUCCION;
            case PRODUCCION     -> Proyecto.Estado.POSTPRODUCCION;
            case POSTPRODUCCION -> Proyecto.Estado.FINALIZADO;
            default -> throw new ServiceException("El proyecto no puede avanzar desde el estado: " + p.getEstado());
        };
        p.setEstado(siguiente);
        actualizar(p);
    }

    /**
     * Elimina un proyecto por su ID.
     */
    public void eliminar(int id) throws ServiceException {
        try {
            if (!dao.eliminar(id))
                throw new ServiceException("No se encontró el proyecto a eliminar.");
        } catch (SQLException e) {
            throw new ServiceException("Error al eliminar el proyecto: " + e.getMessage(), e);
        }
    }

    // ── Validaciones de negocio ───────────────────────────────────────────────
    private void validar(Proyecto p) throws ServiceException {
        if (p.getNombre() == null || p.getNombre().isBlank())
            throw new ServiceException("El nombre del proyecto es obligatorio.");
        if (p.getFechaInicio() == null)
            throw new ServiceException("La fecha de inicio es obligatoria.");
        if (p.getFechaFinPlan() == null)
            throw new ServiceException("La fecha de fin planificada es obligatoria.");
        if (p.getFechaFinPlan().isBefore(p.getFechaInicio()))
            throw new ServiceException("La fecha de fin no puede ser anterior a la de inicio.");
        if (p.getTipo() == null)
            throw new ServiceException("El tipo de proyecto es obligatorio.");
    }
}
