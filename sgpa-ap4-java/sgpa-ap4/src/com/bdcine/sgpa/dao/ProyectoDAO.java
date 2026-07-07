package com.bdcine.sgpa.dao;

import com.bdcine.sgpa.exception.ConexionBDException;
import com.bdcine.sgpa.exception.SgpaException;
import com.bdcine.sgpa.model.Proyecto;
import com.bdcine.sgpa.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion del PATRON DAO para la entidad Proyecto.
 *
 * Implementa la interfaz IRepository<Proyecto>, garantizando que todos los
 * DAOs del sistema compartan un contrato comun (polimorfismo basado en
 * interfaces).
 *
 * Demuestra el USO REAL DE JDBC: conexiones, PreparedStatement, ResultSet,
 * manejo de excepciones SQLException con transformacion a SgpaException.
 */
public class ProyectoDAO implements IRepository<Proyecto> {

    // ── Sentencias SQL ─────────────────────────────────────────────────────
    private static final String SQL_INSERT =
        "INSERT INTO proyecto (nombre, descripcion, tipo, fecha_inicio, " +
        "fecha_fin_plan, presupuesto, estado, id_director) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_SELECT_ALL =
        "SELECT id, nombre, descripcion, tipo, fecha_inicio, fecha_fin_plan, " +
        "fecha_fin_real, presupuesto, estado, id_director " +
        "FROM proyecto ORDER BY fecha_inicio DESC";

    private static final String SQL_SELECT_BY_ID =
        "SELECT id, nombre, descripcion, tipo, fecha_inicio, fecha_fin_plan, " +
        "fecha_fin_real, presupuesto, estado, id_director " +
        "FROM proyecto WHERE id = ?";

    private static final String SQL_UPDATE =
        "UPDATE proyecto SET nombre=?, descripcion=?, tipo=?, fecha_inicio=?, " +
        "fecha_fin_plan=?, fecha_fin_real=?, presupuesto=?, estado=?, " +
        "id_director=? WHERE id=?";

    private static final String SQL_DELETE =
        "DELETE FROM proyecto WHERE id=?";

    // ══════════════════════════════════════════════════════════════════════
    // INSERTAR — establece conexion, ejecuta INSERT, recupera ID generado
    // ══════════════════════════════════════════════════════════════════════
    @Override
    public Proyecto insertar(Proyecto p) throws SgpaException {
        Connection conn = DBConnection.getConnection();   // obtiene conexion
        try (PreparedStatement ps = conn.prepareStatement(
                SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcionTexto());
            ps.setString(3, p.getTipo().name());
            ps.setDate(4, Date.valueOf(p.getFechaInicio()));
            ps.setDate(5, Date.valueOf(p.getFechaFinPlan()));
            ps.setBigDecimal(6, p.getPresupuesto());
            ps.setString(7, p.getEstado().name());
            if (p.getIdDirector() != null) ps.setInt(8, p.getIdDirector());
            else                            ps.setNull(8, Types.INTEGER);

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) p.setId(keys.getInt(1));
            }
            return p;
        } catch (SQLException e) {
            throw new ConexionBDException(
                "Error al insertar proyecto: " + e.getMessage(), e);
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // LISTAR TODOS — SELECT y mapeo a objetos
    // ══════════════════════════════════════════════════════════════════════
    @Override
    public List<Proyecto> listarTodos() throws SgpaException {
        List<Proyecto> lista = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
            return lista;
        } catch (SQLException e) {
            throw new ConexionBDException(
                "Error al listar proyectos: " + e.getMessage(), e);
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // BUSCAR POR ID
    // ══════════════════════════════════════════════════════════════════════
    @Override
    public Proyecto buscarPorId(int id) throws SgpaException {
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new ConexionBDException(
                "Error al buscar proyecto: " + e.getMessage(), e);
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // ACTUALIZAR
    // ══════════════════════════════════════════════════════════════════════
    @Override
    public boolean actualizar(Proyecto p) throws SgpaException {
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcionTexto());
            ps.setString(3, p.getTipo().name());
            ps.setDate(4, Date.valueOf(p.getFechaInicio()));
            ps.setDate(5, Date.valueOf(p.getFechaFinPlan()));
            if (p.getFechaFinReal() != null)
                ps.setDate(6, Date.valueOf(p.getFechaFinReal()));
            else ps.setNull(6, Types.DATE);
            ps.setBigDecimal(7, p.getPresupuesto());
            ps.setString(8, p.getEstado().name());
            if (p.getIdDirector() != null) ps.setInt(9, p.getIdDirector());
            else                            ps.setNull(9, Types.INTEGER);
            ps.setInt(10, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ConexionBDException(
                "Error al actualizar proyecto: " + e.getMessage(), e);
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // ELIMINAR
    // ══════════════════════════════════════════════════════════════════════
    @Override
    public boolean eliminar(int id) throws SgpaException {
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new ConexionBDException(
                "Error al eliminar proyecto: " + e.getMessage(), e);
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // Mapeo ResultSet → Proyecto
    // ══════════════════════════════════════════════════════════════════════
    private Proyecto mapear(ResultSet rs) throws SQLException {
        Proyecto p = new Proyecto(
            rs.getInt("id"),
            rs.getString("nombre"),
            rs.getString("descripcion"),
            Proyecto.TipoProyecto.valueOf(rs.getString("tipo")),
            rs.getDate("fecha_inicio").toLocalDate(),
            rs.getDate("fecha_fin_plan").toLocalDate(),
            rs.getBigDecimal("presupuesto"),
            Proyecto.Estado.valueOf(rs.getString("estado")),
            (Integer) rs.getObject("id_director")
        );
        Date ffr = rs.getDate("fecha_fin_real");
        if (ffr != null) p.setFechaFinReal(ffr.toLocalDate());
        return p;
    }
}
