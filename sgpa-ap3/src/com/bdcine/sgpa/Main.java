package com.bdcine.sgpa;

import com.bdcine.sgpa.menu.ConsoleUI;
import com.bdcine.sgpa.service.GestorProyectos;

/**
 * Punto de entrada del SGPA — Actividad Practica N.3
 * Sistema de Gestion de Produccion Animada — BD Cine S.A.
 *
 * Para compilar y ejecutar desde consola:
 *   javac -d out -sourcepath src src/com/bdcine/sgpa/Main.java
 *   java -cp out com.bdcine.sgpa.Main
 */
public class Main {

    public static void main(String[] args) {
        GestorProyectos gestor = new GestorProyectos();
        ConsoleUI ui = new ConsoleUI(gestor);
        ui.iniciar();
    }
}
