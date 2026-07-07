package com.bdcine.sgpa.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton que gestiona la conexión a la base de datos MySQL.
 * Utiliza el patrón de instancia única para evitar múltiples conexiones.
 */
public class DBConnection {

    private static final String URL      = "jdbc:mysql://localhost:3306/sgpa_bdcine?useSSL=false&serverTimezone=America/Argentina/Buenos_Aires";
    private static final String USER     = "root";
    private static final String PASSWORD = "sgpa1234";

    private static Connection instance = null;

    private DBConnection() {}

    /**
     * Devuelve la conexión activa. Si no existe o está cerrada, crea una nueva.
     */
    public static Connection getConnection() throws SQLException {
        if (instance == null || instance.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                instance = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("[DB] Conexión establecida con sgpa_bdcine.");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver MySQL no encontrado. Agregue mysql-connector-j al classpath.", e);
            }
        }
        return instance;
    }

    /** Cierra la conexión si está abierta. */
    public static void closeConnection() {
        if (instance != null) {
            try {
                instance.close();
                System.out.println("[DB] Conexión cerrada.");
            } catch (SQLException e) {
                System.err.println("[DB] Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}
