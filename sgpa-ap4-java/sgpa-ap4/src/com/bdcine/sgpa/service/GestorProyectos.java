package com.bdcine.sgpa.service;

import com.bdcine.sgpa.dao.EtapaDAO;
import com.bdcine.sgpa.dao.IRepository;
import com.bdcine.sgpa.dao.ProyectoDAO;
import com.bdcine.sgpa.dao.TareaDAO;
import com.bdcine.sgpa.exception.EntidadNoEncontradaException;
import com.bdcine.sgpa.exception.SgpaException;
import com.bdcine.sgpa.model.Etapa;
import com.bdcine.sgpa.model.Proyecto;
import com.bdcine.sgpa.model.Tarea;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Capa de servicio del SGPA. Coordina la logica de negocio entre la
 * interfaz de usuario y los DAOs.
 *
 * Las dependencias se declaran como IRepository (interfaz, no implementacion
 * concreta), demostrando polimorfismo basado en interfaces.
 */
public class GestorProyectos {

    private final IRepository<Proyecto> proyectoDAO;
    private final EtapaDAO              etapaDAO;
    private final TareaDAO              tareaDAO;

    public GestorProyectos() {
        // Inyeccion manual de dependencias
        this.proyectoDAO = new ProyectoDAO();
        this.etapaDAO    = new EtapaDAO();
        this.tareaDAO    = new TareaDAO();
    }

    // ── Validaciones + persistencia ────────────────────────────────────────
    public Proyecto registrarProyecto(Proyecto p) throws SgpaException {
        validarProyecto(p);
        return proyectoDAO.insertar(p);
    }

    public Proyecto buscarProyectoPorId(int id) throws SgpaException {
        Proyecto p = proyectoDAO.buscarPorId(id);
        if (p == null) throw new EntidadNoEncontradaException("Proyecto", id);
        return p;
    }

    public List<Proyecto> listarProyectos() throws SgpaException {
        return proyectoDAO.listarTodos();
    }

    public boolean eliminarProyecto(int id) throws SgpaException {
        buscarProyectoPorId(id);  // valida existencia
        return proyectoDAO.eliminar(id);
    }

    public Etapa agregarEtapa(Etapa e) throws SgpaException {
        if (e.getFechaFinPlan().isBefore(e.getFechaInicio()))
            throw new SgpaException("Fecha fin no puede ser anterior a la de inicio.");
        return etapaDAO.insertar(e);
    }

    public List<Etapa> listarEtapasPorProyecto(int idProyecto) throws SgpaException {
        return etapaDAO.listarPorProyecto(idProyecto);
    }

    public Tarea agregarTarea(Tarea t) throws SgpaException {
        return tareaDAO.insertar(t);
    }

    public boolean actualizarAvance(int idTarea, int avance) throws SgpaException {
        if (avance < 0 || avance > 100)
            throw new SgpaException("Avance debe estar entre 0 y 100.");
        Tarea.Estado nuevoEstado = (avance == 100)
            ? Tarea.Estado.COMPLETADA
            : (avance > 0 ? Tarea.Estado.EN_CURSO : Tarea.Estado.PENDIENTE);
        return tareaDAO.actualizarAvance(idTarea, avance, nuevoEstado);
    }

    // ── Validacion de negocio ──────────────────────────────────────────────
    private void validarProyecto(Proyecto p) throws SgpaException {
        if (p == null)
            throw new SgpaException("Proyecto no puede ser null.");
        if (p.getNombre() == null || p.getNombre().isBlank())
            throw new SgpaException("Nombre del proyecto es obligatorio.");
        if (p.getFechaInicio() == null || p.getFechaFinPlan() == null)
            throw new SgpaException("Fechas obligatorias.");
        if (p.getFechaFinPlan().isBefore(p.getFechaInicio()))
            throw new SgpaException("Fecha fin no puede ser anterior a la de inicio.");
        if (p.getTipo() == null)
            throw new SgpaException("Tipo de proyecto obligatorio.");
    }

    // ── Ordenamiento usando ARRAY (para demostracion) ──────────────────────
    /**
     * Convierte la lista de proyectos en un ARREGLO y lo ordena con
     * un algoritmo manual (Bubble Sort).
     * Demuestra el uso COMPLEMENTARIO de arreglos junto a ArrayList.
     */
    public Proyecto[] ordenarPorNombreUsandoArray() throws SgpaException {
        List<Proyecto> lista = listarProyectos();
        Proyecto[] arr = lista.toArray(new Proyecto[0]);   // ArrayList → Array

        // Bubble Sort sobre el arreglo
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].getNombre().compareToIgnoreCase(arr[j+1].getNombre()) > 0) {
                    Proyecto tmp = arr[j];
                    arr[j]   = arr[j+1];
                    arr[j+1] = tmp;
                }
            }
        }
        return arr;
    }

    public List<Proyecto> ordenarPorAvanceDesc() throws SgpaException {
        List<Proyecto> lista = new ArrayList<>(listarProyectos());
        Collections.sort(lista,
            Comparator.comparingDouble(Proyecto::calcularAvance).reversed());
        return lista;
    }
}
