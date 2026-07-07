package com.bdcine.sgpa.dao;

import com.bdcine.sgpa.model.Proyecto;
import com.bdcine.sgpa.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para la entidad Proyecto.
 * Encapsula todas las operaciones CRUD contra la tabla `proyecto`.
 */
public class ProyectoDAO {

    // ── SQL ───────────────────────────────────────────────────────────────────
    private static final String INSERT =
        "INSERT INTO proyecto (nombre, descripcion, tipo, fecha_inicio, " +
        "fecha_fin_plan, presupuesto, estado, id_director, observaciones) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL =
        "SELECT id, nombre, descripcion, tipo, fecha_inicio, fecha_fin_plan, " +
        "fecha_fin_real, presupuesto, estado, id_director, observaciones " +
        "FROM proyecto ORDER BY fecha_inicio DESC";

    private static final String SELECT_BY_ID =
        "SELECT id, nombre, descripcion, tipo, fecha_inicio, fecha_fin_plan, " +
        "fecha_fin_real, presupuesto, estado, id_director, observaciones " +
        "FROM proyecto WHERE id = ?";

    private static final String UPDATE =
        "UPDATE proyecto SET nombre=?, descripcion=?, tipo=?, fecha_inicio=?, " +
        "fecha_fin_plan=?, presupuesto=?, estado=?, id_director=?, observaciones=? " +
        "WHERE id=?";

    private static final String DELETE =
        "DELETE FROM proyecto WHERE id=?";

    private static final String SELECT_BY_ESTADO =
        "SELECT id, nombre, descripcion, tipo, fecha_inicio, fecha_fin_plan, " +
        "fecha_fin_real, presupuesto, estado, id_director, observaciones " +
        "FROM proyecto WHERE estado=? ORDER BY fecha_inicio";

    // ── Insertar ─────────────────────────────────────────────────────────────
    public Proyecto insertar(Proyecto p) throws SQLException {
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setString(3, p.getTipo().name());
            ps.setDate(4, Date.valueOf(p.getFechaInicio()));
            ps.setDate(5, Date.valueOf(p.getFechaFinPlan()));
            ps.setBigDecimal(6, p.getPresupuesto());
            ps.setString(7, p.getEstado().name());
            if (p.getIdDirector() != null) ps.setInt(8, p.getIdDirector());
            else ps.setNull(8, Types.INTEGER);
            ps.setString(9, p.getObservaciones());

            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) p.setId(keys.getInt(1));
        }
        return p;
    }

    // ── Listar todos ─────────────────────────────────────────────────────────
    public List<Proyecto> listarTodos() throws SQLException {
        List<Proyecto> lista = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    // ── Buscar por ID ─────────────────────────────────────────────────────────
    public Proyecto buscarPorId(int id) throws SQLException {
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    // ── Listar por estado ────────────────────────────────────────────────────
    public List<Proyecto> listarPorEstado(Proyecto.Estado estado) throws SQLException {
        List<Proyecto> lista = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SELECT_BY_ESTADO)) {
            ps.setString(1, estado.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // ── Actualizar ───────────────────────────────────────────────────────────
    public boolean actualizar(Proyecto p) throws SQLException {
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(UPDATE)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setString(3, p.getTipo().name());
            ps.setDate(4, Date.valueOf(p.getFechaInicio()));
            ps.setDate(5, Date.valueOf(p.getFechaFinPlan()));
            ps.setBigDecimal(6, p.getPresupuesto());
            ps.setString(7, p.getEstado().name());
            if (p.getIdDirector() != null) ps.setInt(8, p.getIdDirector());
            else ps.setNull(8, Types.INTEGER);
            ps.setString(9, p.getObservaciones());
            ps.setInt(10, p.getId());
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

    // ── Mapeo ResultSet → Proyecto ────────────────────────────────────────────
    private Proyecto mapear(ResultSet rs) throws SQLException {
        Proyecto p = new Proyecto();
        p.setId(rs.getInt("id"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setTipo(Proyecto.Tipo.valueOf(rs.getString("tipo")));
        p.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
        p.setFechaFinPlan(rs.getDate("fecha_fin_plan").toLocalDate());
        Date ffr = rs.getDate("fecha_fin_real");
        if (ffr != null) p.setFechaFinReal(ffr.toLocalDate());
        p.setPresupuesto(rs.getBigDecimal("presupuesto"));
        p.setEstado(Proyecto.Estado.valueOf(rs.getString("estado")));
        int dir = rs.getInt("id_director");
        if (!rs.wasNull()) p.setIdDirector(dir);
        p.setObservaciones(rs.getString("observaciones"));
        return p;
    }
}
