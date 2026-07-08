package com.bdcine.sgpa;

import com.bdcine.sgpa.exception.SgpaException;
import com.bdcine.sgpa.service.GestorProyectos;
import com.bdcine.sgpa.util.DBConnection;
import com.bdcine.sgpa.view.ConsoleUI;

/**
 * Punto de entrada del SGPA — Actividad Practica N.4
 * BD Cine S.A.
 *
 * Compilar y ejecutar:
 *   javac -cp ".:lib/mysql-connector-j-8.x.jar" -d out src/com/bdcine/sgpa/Main.java
 *   java  -cp "out:lib/mysql-connector-j-8.x.jar" com.bdcine.sgpa.Main
 */
public class Main {

    public static void main(String[] args) {
        // Verificar conexion a la DB antes de iniciar la UI
        try {
            DBConnection.getConnection();
            System.out.println("[SGPA] Iniciando aplicacion...");

            GestorProyectos gestor = new GestorProyectos();
            new ConsoleUI(gestor).iniciar();

        } catch (SgpaException e) {
            System.err.println("\nERROR FATAL: No se pudo conectar a la base de datos.");
            System.err.println(e.getMessage());
            System.err.println("\nVerifique:");
            System.err.println("  1. Que el servicio MySQL este corriendo.");
            System.err.println("  2. Que el esquema sgpa_bdcine exista (sql/schema.sql).");
            System.err.println("  3. Las credenciales en util/DBConnection.java.");
            System.exit(1);
        } finally {
            DBConnection.closeConnection();
        }
    }
}
