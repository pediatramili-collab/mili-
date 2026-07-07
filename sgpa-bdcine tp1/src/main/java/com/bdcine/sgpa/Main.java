package com.bdcine.sgpa;

import com.bdcine.sgpa.util.DBConnection;
import com.bdcine.sgpa.view.MainView;

import javax.swing.*;

/**
 * Punto de entrada del SGPA.
 * Inicializa el Look & Feel y lanza la ventana principal.
 */
public class Main {

    public static void main(String[] args) {
        // Look & Feel nativo del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si falla, se usa el L&F por defecto de Swing
            System.err.println("No se pudo establecer el Look & Feel: " + e.getMessage());
        }

        // Lanzar en el Event Dispatch Thread (buena práctica Swing)
        SwingUtilities.invokeLater(() -> {
            MainView ventana = new MainView();
            ventana.setVisible(true);
        });

        // Hook para cerrar la conexión a la BD al salir
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DBConnection.closeConnection();
            System.out.println("[SGPA] Aplicación cerrada correctamente.");
        }));
    }
}
