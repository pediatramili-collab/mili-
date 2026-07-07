package com.bdcine.sgpa.dao;

import com.bdcine.sgpa.model.Tarea;
import com.bdcine.sgpa.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Tarea.
 */
public class TareaDAO {

    private static final String INSERT =
        "INSERT INTO tarea (id_etapa, nombre, descripcion, fecha_inicio, " +
        "fecha_fin_plan, porcentaje_avance, estado, prioridad) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_BY_ETAPA =
        "SELECT id, id_etapa, nombre, descripcion, fecha_inicio, fecha_fin_plan, " +
        "fecha_fin_real, porcentaje_avance, estado, prioridad " +
        "FROM tarea WHERE id_etapa=? ORDER BY prioridad DESC, fecha_fin_plan";

    private static final String SELECT_BY_ID =
        "SELECT id, id_etapa, nombre, descripcion, fecha_inicio, fecha_fin_plan, " +
        "fecha_fin_real, porcentaje_avance, estado, prioridad " +
        "FROM tarea WHERE id=?";

    private static final String UPDATE_AVANCE =
        "UPDATE tarea SET porcentaje_avance=?, estado=? WHERE id=?";

    private static final String UPDATE =
        "UPDATE tarea SET nombre=?, descripcion=?, fecha_inicio=?, fecha_fin_plan=?, " +
        "fecha_fin_real=?, porcentaje_avance=?, estado=?, prioridad=? WHERE id=?";

    private static final String DELETE = "DELETE FROM tarea WHERE id=?";

    private static final String SELECT_VENCIDAS =
        "SELECT id, id_etapa, nombre, descripcion, fecha_inicio, fecha_fin_plan, " +
        "fecha_fin_real, porcentaje_avance, estado, prioridad " +
        "FROM tarea WHERE fecha_fin_plan < CURRENT_DATE AND estado NOT IN ('COMPLETADA','BLOQUEADA')";

    // ── Insertar ─────────────────────────────────────────────────────────────
    public Tarea insertar(Tarea t) throws SQLException {
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, t.getIdEtapa());
            ps.setString(2, t.getNombre());
            ps.setString(3, t.getDescripcion());
            ps.setDate(4, Date.valueOf(t.getFechaInicio()));
            ps.setDate(5, Date.valueOf(t.getFechaFinPlan()));
            ps.setInt(6, t.getPorcentajeAvance());
            ps.setString(7, t.getEstado().name());
            ps.setString(8, t.getPrioridad().name());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) t.setId(keys.getInt(1));
        }
        return t;
    }

    // ── Listar por etapa ─────────────────────────────────────────────────────
    public List<Tarea> listarPorEtapa(int idEtapa) throws SQLException {
        List<Tarea> lista = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_BY_ETAPA)) {
            ps.setInt(1, idEtapa);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // ── Buscar por ID ─────────────────────────────────────────────────────────
    public Tarea buscarPorId(int id) throws SQLException {
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    // ── Actualizar avance (operación frecuente) ───────────────────────────────
    public boolean actualizarAvance(int idTarea, int porcentaje, Tarea.Estado estado) throws SQLException {
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(UPDATE_AVANCE)) {
            ps.setInt(1, porcentaje);
            ps.setString(2, estado.name());
            ps.setInt(3, idTarea);
            return ps.executeUpdate() > 0;
        }
    }

    // ── Actualizar completo ───────────────────────────────────────────────────
    public boolean actualizar(Tarea t) throws SQLException {
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1, t.getNombre());
            ps.setString(2, t.getDescripcion());
            ps.setDate(3, Date.valueOf(t.getFechaInicio()));
            ps.setDate(4, Date.valueOf(t.getFechaFinPlan()));
            if (t.getFechaFinReal() != null) ps.setDate(5, Date.valueOf(t.getFechaFinReal()));
            else ps.setNull(5, Types.DATE);
            ps.setInt(6, t.getPorcentajeAvance());
            ps.setString(7, t.getEstado().name());
            ps.setString(8, t.getPrioridad().name());
            ps.setInt(9, t.getId());
            return ps.executeUpdate() > 0;
        }
    }

    // ── Eliminar ─────────────────────────────────────────────────────────────
    public boolean eliminar(int id) throws SQLException {
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // ── Tareas vencidas (para alertas) ────────────────────────────────────────
    public List<Tarea> listarVencidas() throws SQLException {
        List<Tarea> lista = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_VENCIDAS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    // ── Mapeo ─────────────────────────────────────────────────────────────────
    private Tarea mapear(ResultSet rs) throws SQLException {
        Tarea t = new Tarea();
        t.setId(rs.getInt("id"));
        t.setIdEtapa(rs.getInt("id_etapa"));
        t.setNombre(rs.getString("nombre"));
        t.setDescripcion(rs.getString("descripcion"));
        t.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
        t.setFechaFinPlan(rs.getDate("fecha_fin_plan").toLocalDate());
        Date ffr = rs.getDate("fecha_fin_real");
        if (ffr != null) t.setFechaFinReal(ffr.toLocalDate());
        t.setPorcentajeAvance(rs.getInt("porcentaje_avance"));
        t.setEstado(Tarea.Estado.valueOf(rs.getString("estado")));
        t.setPrioridad(Tarea.Prioridad.valueOf(rs.getString("prioridad")));
        return t;
    }
}
