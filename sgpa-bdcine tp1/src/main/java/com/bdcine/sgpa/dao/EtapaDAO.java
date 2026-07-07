package com.bdcine.sgpa.dao;

import com.bdcine.sgpa.model.Etapa;
import com.bdcine.sgpa.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Etapa.
 */
public class EtapaDAO {

    private static final String INSERT =
        "INSERT INTO etapa (id_proyecto, nombre, tipo, orden, fecha_inicio, " +
        "fecha_fin_plan, estado, descripcion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_BY_PROYECTO =
        "SELECT id, id_proyecto, nombre, tipo, orden, fecha_inicio, " +
        "fecha_fin_plan, fecha_fin_real, estado, descripcion " +
        "FROM etapa WHERE id_proyecto=? ORDER BY orden";

    private static final String SELECT_BY_ID =
        "SELECT id, id_proyecto, nombre, tipo, orden, fecha_inicio, " +
        "fecha_fin_plan, fecha_fin_real, estado, descripcion " +
        "FROM etapa WHERE id=?";

    private static final String UPDATE =
        "UPDATE etapa SET nombre=?, tipo=?, orden=?, fecha_inicio=?, " +
        "fecha_fin_plan=?, fecha_fin_real=?, estado=?, descripcion=? WHERE id=?";

    private static final String DELETE = "DELETE FROM etapa WHERE id=?";

    // ── Insertar ─────────────────────────────────────────────────────────────
    public Etapa insertar(Etapa e) throws SQLException {
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, e.getIdProyecto());
            ps.setString(2, e.getNombre());
            ps.setString(3, e.getTipo().name());
            ps.setInt(4, e.getOrden());
            ps.setDate(5, Date.valueOf(e.getFechaInicio()));
            ps.setDate(6, Date.valueOf(e.getFechaFinPlan()));
            ps.setString(7, e.getEstado().name());
            ps.setString(8, e.getDescripcion());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) e.setId(keys.getInt(1));
        }
        return e;
    }

    // ── Listar por proyecto ───────────────────────────────────────────────────
    public List<Etapa> listarPorProyecto(int idProyecto) throws SQLException {
        List<Etapa> lista = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_BY_PROYECTO)) {
            ps.setInt(1, idProyecto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // ── Buscar por ID ─────────────────────────────────────────────────────────
    public Etapa buscarPorId(int id) throws SQLException {
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    // ── Actualizar ───────────────────────────────────────────────────────────
    public boolean actualizar(Etapa e) throws SQLException {
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getTipo().name());
            ps.setInt(3, e.getOrden());
            ps.setDate(4, Date.valueOf(e.getFechaInicio()));
            ps.setDate(5, Date.valueOf(e.getFechaFinPlan()));
            if (e.getFechaFinReal() != null) ps.setDate(6, Date.valueOf(e.getFechaFinReal()));
            else ps.setNull(6, Types.DATE);
            ps.setString(7, e.getEstado().name());
            ps.setString(8, e.getDescripcion());
            ps.setInt(9, e.getId());
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

    // ── Mapeo ─────────────────────────────────────────────────────────────────
    private Etapa mapear(ResultSet rs) throws SQLException {
        Etapa e = new Etapa();
        e.setId(rs.getInt("id"));
        e.setIdProyecto(rs.getInt("id_proyecto"));
        e.setNombre(rs.getString("nombre"));
        e.setTipo(Etapa.Tipo.valueOf(rs.getString("tipo")));
        e.setOrden(rs.getInt("orden"));
        e.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
        e.setFechaFinPlan(rs.getDate("fecha_fin_plan").toLocalDate());
        Date ffr = rs.getDate("fecha_fin_real");
        if (ffr != null) e.setFechaFinReal(ffr.toLocalDate());
        e.setEstado(Etapa.Estado.valueOf(rs.getString("estado")));
        e.setDescripcion(rs.getString("descripcion"));
        return e;
    }
}
