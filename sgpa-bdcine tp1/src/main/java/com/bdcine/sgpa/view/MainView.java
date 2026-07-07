package com.bdcine.sgpa.view;

import com.bdcine.sgpa.model.Proyecto;
import com.bdcine.sgpa.service.ProyectoService;
import com.bdcine.sgpa.service.ServiceException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Ventana principal del SGPA.
 * Muestra el listado de proyectos y permite el alta, edición y eliminación.
 */
public class MainView extends JFrame {

    // ── Colores corporativos BD Cine ─────────────────────────────────────────
    private static final Color COLOR_HEADER  = new Color(26, 26, 46);   // azul oscuro
    private static final Color COLOR_BTN     = new Color(15, 52, 96);   // azul medio
    private static final Color COLOR_BTN_HOV = new Color(30, 64, 175);  // azul hover
    private static final Color COLOR_BG      = new Color(245, 247, 255);// fondo claro
    private static final Color COLOR_WHITE   = Color.WHITE;
    private static final Font  FONT_TITLE    = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font  FONT_NORMAL   = new Font("Segoe UI", Font.PLAIN, 12);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ── Componentes ──────────────────────────────────────────────────────────
    private JTable          tabla;
    private DefaultTableModel modelo;
    private JLabel          lblEstado;

    private final ProyectoService service = new ProyectoService();

    // ── Constructor ───────────────────────────────────────────────────────────
    public MainView() {
        super("SGPA — Sistema de Gestión de Producción Animada | BD Cine S.A.");
        initUI();
        cargarProyectos();
    }

    // ── Inicialización de UI ──────────────────────────────────────────────────
    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 620);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_BG);
        setLayout(new BorderLayout(0, 0));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);
    }

    private JPanel buildHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_HEADER);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel title = new JLabel("🎬  SGPA — Gestión de Proyectos");
        title.setFont(FONT_TITLE);
        title.setForeground(COLOR_WHITE);

        JLabel sub = new JLabel("BD Cine S.A.");
        sub.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        sub.setForeground(new Color(180, 200, 230));

        JPanel left = new JPanel(new GridLayout(2, 1));
        left.setOpaque(false);
        left.add(title);
        left.add(sub);
        panel.add(left, BorderLayout.WEST);
        return panel;
    }

    private JPanel buildCenter() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(COLOR_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 16, 0, 16));

        // Toolbar
        panel.add(buildToolbar(), BorderLayout.NORTH);

        // Tabla
        String[] cols = {"ID", "Nombre", "Tipo", "Estado", "Inicio", "Fin plan", "Presupuesto ($)"};
        modelo = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setFont(FONT_NORMAL);
        tabla.setRowHeight(26);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(COLOR_BTN);
        tabla.getTableHeader().setForeground(COLOR_WHITE);
        tabla.setSelectionBackground(new Color(219, 234, 254));
        tabla.setGridColor(new Color(200, 210, 230));

        // Columna ID más angosta
        tabla.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(260);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(90);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(90);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(180, 195, 220)));
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildToolbar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        bar.setBackground(COLOR_BG);

        JButton btnNuevo    = crearBoton("＋ Nuevo proyecto",  e -> abrirDialogoNuevo());
        JButton btnEditar   = crearBoton("✎ Editar",           e -> editarSeleccionado());
        JButton btnEliminar = crearBoton("✕ Eliminar",         e -> eliminarSeleccionado());
        JButton btnAvanzar  = crearBoton("▶ Avanzar estado",   e -> avanzarEstado());
        JButton btnRefresh  = crearBoton("⟳ Actualizar",       e -> cargarProyectos());

        btnEliminar.setBackground(new Color(185, 28, 28));
        btnEliminar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnEliminar.setBackground(new Color(220, 38, 38));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btnEliminar.setBackground(new Color(185, 28, 28));
            }
        });

        bar.add(btnNuevo);
        bar.add(btnEditar);
        bar.add(btnEliminar);
        bar.add(Box.createHorizontalStrut(10));
        bar.add(btnAvanzar);
        bar.add(Box.createHorizontalStrut(10));
        bar.add(btnRefresh);
        return bar;
    }

    private JPanel buildFooter() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_HEADER);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 16, 5, 16));
        lblEstado = new JLabel("Listo.");
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblEstado.setForeground(new Color(180, 200, 230));
        panel.add(lblEstado, BorderLayout.WEST);
        return panel;
    }

    private JButton crearBoton(String texto, java.awt.event.ActionListener listener) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setBackground(COLOR_BTN);
        btn.setForeground(COLOR_WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        btn.addActionListener(listener);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn.isEnabled()) btn.setBackground(COLOR_BTN_HOV);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!btn.getBackground().equals(new Color(185, 28, 28)))
                    btn.setBackground(COLOR_BTN);
            }
        });
        return btn;
    }

    // ── Lógica de acciones ────────────────────────────────────────────────────
    private void cargarProyectos() {
        modelo.setRowCount(0);
        try {
            List<Proyecto> lista = service.listarTodos();
            for (Proyecto p : lista) {
                modelo.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(),
                    p.getTipo(),
                    p.getEstado(),
                    p.getFechaInicio().format(FMT),
                    p.getFechaFinPlan().format(FMT),
                    p.getPresupuesto() != null ? String.format("%,.2f", p.getPresupuesto()) : "—"
                });
            }
            setStatus(lista.size() + " proyecto(s) cargado(s).");
        } catch (ServiceException e) {
            mostrarError("No se pudo conectar a la base de datos.\n" + e.getMessage());
            setStatus("Error al cargar proyectos.");
        }
    }

    private void abrirDialogoNuevo() {
        ProyectoDialog dlg = new ProyectoDialog(this, null, service);
        dlg.setVisible(true);
        cargarProyectos();
    }

    private void editarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { mostrarAviso("Seleccioná un proyecto para editar."); return; }
        int id = (int) modelo.getValueAt(fila, 0);
        try {
            Proyecto p = service.buscarPorId(id);
            ProyectoDialog dlg = new ProyectoDialog(this, p, service);
            dlg.setVisible(true);
            cargarProyectos();
        } catch (ServiceException e) {
            mostrarError(e.getMessage());
        }
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { mostrarAviso("Seleccioná un proyecto para eliminar."); return; }
        int id     = (int) modelo.getValueAt(fila, 0);
        String nom = (String) modelo.getValueAt(fila, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Eliminar el proyecto \"" + nom + "\" y todas sus etapas y tareas?",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            service.eliminar(id);
            setStatus("Proyecto eliminado correctamente.");
            cargarProyectos();
        } catch (ServiceException e) {
            mostrarError(e.getMessage());
        }
    }

    private void avanzarEstado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) { mostrarAviso("Seleccioná un proyecto para avanzar su estado."); return; }
        int id     = (int) modelo.getValueAt(fila, 0);
        String nom = (String) modelo.getValueAt(fila, 1);
        try {
            service.avanzarEstado(id);
            setStatus("Estado del proyecto \"" + nom + "\" avanzado correctamente.");
            cargarProyectos();
        } catch (ServiceException e) {
            mostrarError(e.getMessage());
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private void setStatus(String msg) { lblEstado.setText(msg); }
    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
    private void mostrarAviso(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
}
