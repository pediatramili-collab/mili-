package com.bdcine.sgpa.util;

/**
 * Constantes del SGPA usando ARREGLOS (arrays) de tamano fijo.
 *
 * Demuestra el uso COMPLEMENTARIO de arreglos y ArrayList:
 * - Los arreglos se usan para datos FIJOS de tamano CONOCIDO (estados, tipos).
 * - Los ArrayList se usan en las colecciones DINAMICAS (proyectos, etapas, tareas).
 */
public final class Constantes {

    private Constantes() {} // clase utilitaria, no instanciable

    // ── Arreglos de estados validos ─────────────────────────────────────────
    /** Estados posibles de un Proyecto (orden secuencial). */
    public static final String[] ESTADOS_PROYECTO = {
        "PREPRODUCCION",
        "PRODUCCION",
        "POSTPRODUCCION",
        "FINALIZADO",
        "CANCELADO"
    };

    /** Tipos de Etapa permitidos. */
    public static final String[] TIPOS_ETAPA = {
        "PREPRODUCCION",
        "PRODUCCION",
        "POSTPRODUCCION"
    };

    /** Estados validos de una Tarea. */
    public static final String[] ESTADOS_TAREA = {
        "PENDIENTE",
        "EN_CURSO",
        "EN_REVISION",
        "COMPLETADA",
        "BLOQUEADA"
    };

    /** Prioridades de Tarea (ordenadas de menor a mayor). */
    public static final String[] PRIORIDADES_TAREA = {
        "BAJA", "MEDIA", "ALTA", "CRITICA"
    };

    /** Tipos de proyecto disponibles. */
    public static final String[] TIPOS_PROYECTO = {
        "LARGOMETRAJE", "CORTOMETRAJE", "SERIE", "PUBLICIDAD", "OTRO"
    };

    // ── Metodos utilitarios sobre arreglos ──────────────────────────────────
    /**
     * Verifica si un valor pertenece a un arreglo (busqueda lineal).
     * Demuestra el uso de la estructura de control for-each sobre arreglos.
     */
    public static boolean contiene(String[] arr, String valor) {
        for (String s : arr) {           // for-each sobre arreglo
            if (s.equalsIgnoreCase(valor)) return true;
        }
        return false;
    }

    /**
     * Devuelve el indice de un valor en un arreglo, o -1 si no existe.
     */
    public static int indiceDe(String[] arr, String valor) {
        for (int i = 0; i < arr.length; i++) {   // for clasico con indice
            if (arr[i].equalsIgnoreCase(valor)) return i;
        }
        return -1;
    }
}
