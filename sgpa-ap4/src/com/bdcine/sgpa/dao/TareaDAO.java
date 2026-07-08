package com.bdcine.sgpa.dao;

import com.bdcine.sgpa.exception.ConexionBDException;
import com.bdcine.sgpa.exception.SgpaException;
import com.bdcine.sgpa.model.Tarea;
import com.bdcine.sgpa.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TareaDAO implements IRepository<Tarea> {

    private static final String SQL_INSERT =
        "INSERT INTO tarea (id_etapa, nombre, fecha_inicio, fecha_fin_plan, " +
        "porcentaje_avance, estado, prioridad) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_SELECT_ALL =
        "SELECT id, id_etapa, nombre, fecha_inicio, fecha_fin_plan, " +
        "fecha_fin_real, porcentaje_avance, estado, prioridad " +
        "FROM tarea ORDER BY id_etapa, prioridad DESC";

    private static final String SQL_SELECT_BY_ID =
        "SELECT id, id_etapa, nombre, fecha_inicio, fecha_fin_plan, " +
        "fecha_fin_real, porcentaje_avance, estado, prioridad " +
        "FROM tarea WHERE id = ?";

    private static final String SQL_UPDATE_AVANCE =
        "UPDATE tarea SET porcentaje_avance=?, estado=? WHERE id=?";

    private static final String SQL_UPDATE =
        "UPDATE tarea SET nombre=?, fecha_inicio=?, fecha_fin_plan=?, " +
        "fecha_fin_real=?, porcentaje_avance=?, estado=?, prioridad=? WHERE id=?";

    private static final String SQL_DELETE = "DELETE FROM tarea WHERE id=?";

    @Override
    public Tarea insertar(Tarea t) throws SgpaException {
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, t.getIdEtapa());
            ps.setString(2, t.getNombre());
            ps.setDate(3, Date.valueOf(t.getFechaInicio()));
            ps.setDate(4, Date.valueOf(t.getFechaFinPlan()));
            ps.setInt(5, t.getPorcentajeAvance());
            ps.setString(6, t.getEstado().name());
            ps.setString(7, t.getPrioridad().name());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) t.setId(keys.getInt(1));
            }
            return t;
        } catch (SQLException ex) {
            throw new ConexionBDException("Error al insertar tarea: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Tarea> listarTodos() throws SgpaException {
        List<Tarea> lista = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
            return lista;
        } catch (SQLException ex) {
            throw new ConexionBDException("Error al listar tareas: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Tarea buscarPorId(int id) throws SgpaException {
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
            return null;
        } catch (SQLException ex) {
            throw new ConexionBDException("Error al buscar tarea: " + ex.getMessage(), ex);
        }
    }

    /** Actualizacion eficiente del avance (operacion frecuente). */
    public boolean actualizarAvance(int idTarea, int avance, Tarea.Estado estado)
            throws SgpaException {
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SQL_UPDATE_AVANCE)) {
            ps.setInt(1, avance);
            ps.setString(2, estado.name());
            ps.setInt(3, idTarea);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new ConexionBDException("Error al actualizar avance: " + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean actualizar(Tarea t) throws SgpaException {
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, t.getNombre());
            ps.setDate(2, Date.valueOf(t.getFechaInicio()));
            ps.setDate(3, Date.valueOf(t.getFechaFinPlan()));
            if (t.getFechaFinReal() != null)
                ps.setDate(4, Date.valueOf(t.getFechaFinReal()));
            else ps.setNull(4, Types.DATE);
            ps.setInt(5, t.getPorcentajeAvance());
            ps.setString(6, t.getEstado().name());
            ps.setString(7, t.getPrioridad().name());
            ps.setInt(8, t.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new ConexionBDException("Error al actualizar tarea: " + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean eliminar(int id) throws SgpaException {
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new ConexionBDException("Error al eliminar tarea: " + ex.getMessage(), ex);
        }
    }

    private Tarea mapear(ResultSet rs) throws SQLException {
        Tarea t = new Tarea(
            rs.getInt("id"),
            rs.getInt("id_etapa"),
            rs.getString("nombre"),
            rs.getDate("fecha_inicio").toLocalDate(),
            rs.getDate("fecha_fin_plan").toLocalDate(),
            rs.getInt("porcentaje_avance"),
            Tarea.Estado.valueOf(rs.getString("estado")),
            Tarea.Prioridad.valueOf(rs.getString("prioridad"))
        );
        Date ffr = rs.getDate("fecha_fin_real");
        if (ffr != null) t.setFechaFinReal(ffr.toLocalDate());
        return t;
    }
}
