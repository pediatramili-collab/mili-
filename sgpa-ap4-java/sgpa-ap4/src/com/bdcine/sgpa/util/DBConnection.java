package com.bdcine.sgpa.util;

import com.bdcine.sgpa.exception.ConexionBDException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Conexion a la base de datos MySQL implementada con el PATRON SINGLETON.
 *
 * El patron Singleton garantiza que exista UNA SOLA INSTANCIA de Connection
 * durante todo el ciclo de vida de la aplicacion, evitando la creacion
 * costosa de multiples conexiones y manteniendo coherencia transaccional.
 *
 * (Gamma, Helm, Johnson y Vlissides, 1994 — Design Patterns)
 */
public final class DBConnection {

    // ── Parametros de conexion ────────────────────────────────────────────
    private static final String URL =
        "jdbc:mysql://localhost:3306/sgpa_bdcine"
      + "?useSSL=false"
      + "&serverTimezone=America/Argentina/Buenos_Aires"
      + "&characterEncoding=UTF-8";

    private static final String USER     = "root";
    private static final String PASSWORD = "sgpa1234";

    /** Unica instancia de Connection (patron Singleton). */
    private static Connection instancia = null;

    /** Constructor privado: impide la creacion de instancias desde fuera. */
    private DBConnection() {}

    /**
     * Devuelve la instancia unica de Connection.
     * Si no existe o esta cerrada, la crea (lazy initialization).
     *
     * @throws ConexionBDException si no se puede establecer la conexion.
     */
    public static Connection getConnection() throws ConexionBDException {
        try {
            if (instancia == null || instancia.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                instancia = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("[DB] Conexion establecida con sgpa_bdcine.");
            }
            return instancia;
        } catch (ClassNotFoundException e) {
            throw new ConexionBDException(
                "Driver MySQL no encontrado. Agregue mysql-connector-j al classpath.", e);
        } catch (SQLException e) {
            throw new ConexionBDException(
                "Error al conectar con la base de datos: " + e.getMessage(), e);
        }
    }

    /** Cierra la conexion al apagar la aplicacion. */
    public static void closeConnection() {
        if (instancia != null) {
            try {
                instancia.close();
                System.out.println("[DB] Conexion cerrada.");
            } catch (SQLException e) {
                System.err.println("[DB] Error al cerrar conexion: " + e.getMessage());
            }
        }
    }
}
