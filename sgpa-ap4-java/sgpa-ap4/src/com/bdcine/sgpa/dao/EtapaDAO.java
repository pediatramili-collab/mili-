package com.bdcine.sgpa.dao;

import com.bdcine.sgpa.exception.ConexionBDException;
import com.bdcine.sgpa.exception.SgpaException;
import com.bdcine.sgpa.model.Etapa;
import com.bdcine.sgpa.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** DAO de Etapa con persistencia real en MySQL. */
public class EtapaDAO implements IRepository<Etapa> {

    private static final String SQL_INSERT =
        "INSERT INTO etapa (id_proyecto, nombre, tipo, orden, " +
        "fecha_inicio, fecha_fin_plan, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_SELECT_BY_ID =
        "SELECT id, id_proyecto, nombre, tipo, orden, fecha_inicio, " +
        "fecha_fin_plan, fecha_fin_real, estado FROM etapa WHERE id = ?";

    private static final String SQL_SELECT_ALL =
        "SELECT id, id_proyecto, nombre, tipo, orden, fecha_inicio, " +
        "fecha_fin_plan, fecha_fin_real, estado FROM etapa ORDER BY id_proyecto, orden";

    private static final String SQL_SELECT_BY_PROYECTO =
        "SELECT id, id_proyecto, nombre, tipo, orden, fecha_inicio, " +
        "fecha_fin_plan, fecha_fin_real, estado FROM etapa " +
        "WHERE id_proyecto = ? ORDER BY orden";

    private static final String SQL_UPDATE =
        "UPDATE etapa SET nombre=?, tipo=?, orden=?, fecha_inicio=?, " +
        "fecha_fin_plan=?, fecha_fin_real=?, estado=? WHERE id=?";

    private static final String SQL_DELETE = "DELETE FROM etapa WHERE id=?";

    @Override
    public Etapa insertar(Etapa e) throws SgpaException {
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, e.getIdProyecto());
            ps.setString(2, e.getNombre());
            ps.setString(3, e.getTipo().name());
            ps.setInt(4, e.getOrden());
            ps.setDate(5, Date.valueOf(e.getFechaInicio()));
            ps.setDate(6, Date.valueOf(e.getFechaFinPlan()));
            ps.setString(7, e.getEstado().name());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) e.setId(keys.getInt(1));
            }
            return e;
        } catch (SQLException ex) {
            throw new ConexionBDException("Error al insertar etapa: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Etapa> listarTodos() throws SgpaException {
        List<Etapa> lista = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
            return lista;
        } catch (SQLException ex) {
            throw new ConexionBDException("Error al listar etapas: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Etapa buscarPorId(int id) throws SgpaException {
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
            return null;
        } catch (SQLException ex) {
            throw new ConexionBDException("Error al buscar etapa: " + ex.getMessage(), ex);
        }
    }

    /** Metodo especifico de este DAO: listar etapas de un proyecto. */
    public List<Etapa> listarPorProyecto(int idProyecto) throws SgpaException {
        List<Etapa> lista = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_PROYECTO)) {
            ps.setInt(1, idProyecto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
            return lista;
        } catch (SQLException ex) {
            throw new ConexionBDException("Error al listar etapas por proyecto: " + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean actualizar(Etapa e) throws SgpaException {
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getTipo().name());
            ps.setInt(3, e.getOrden());
            ps.setDate(4, Date.valueOf(e.getFechaInicio()));
            ps.setDate(5, Date.valueOf(e.getFechaFinPlan()));
            if (e.getFechaFinReal() != null)
                ps.setDate(6, Date.valueOf(e.getFechaFinReal()));
            else ps.setNull(6, Types.DATE);
            ps.setString(7, e.getEstado().name());
            ps.setInt(8, e.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new ConexionBDException("Error al actualizar etapa: " + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean eliminar(int id) throws SgpaException {
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new ConexionBDException("Error al eliminar etapa: " + ex.getMessage(), ex);
        }
    }

    private Etapa mapear(ResultSet rs) throws SQLException {
        Etapa e = new Etapa(
            rs.getInt("id"),
            rs.getInt("id_proyecto"),
            rs.getString("nombre"),
            Etapa.TipoEtapa.valueOf(rs.getString("tipo")),
            rs.getInt("orden"),
            rs.getDate("fecha_inicio").toLocalDate(),
            rs.getDate("fecha_fin_plan").toLocalDate(),
            Etapa.Estado.valueOf(rs.getString("estado"))
        );
        Date ffr = rs.getDate("fecha_fin_real");
        if (ffr != null) e.setFechaFinReal(ffr.toLocalDate());
        return e;
    }
}
