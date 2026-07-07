package com.bdcine.sgpa.view;

import com.bdcine.sgpa.model.Proyecto;
import com.bdcine.sgpa.service.ProyectoService;
import com.bdcine.sgpa.service.ServiceException;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Diálogo modal para registrar o editar un proyecto.
 * Se usa tanto para alta (proyecto == null) como para edición.
 */
public class ProyectoDialog extends JDialog {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private JTextField  txtNombre;
    private JTextArea   txtDescripcion;
    private JComboBox<Proyecto.Tipo>   cmbTipo;
    private JComboBox<Proyecto.Estado> cmbEstado;
    private JTextField  txtFechaInicio;
    private JTextField  txtFechaFinPlan;
    private JTextField  txtPresupuesto;
    private JTextArea   txtObservaciones;

    private final Proyecto        proyecto;   // null = alta
    private final ProyectoService service;

    public ProyectoDialog(Frame owner, Proyecto proyecto, ProyectoService service) {
        super(owner, proyecto == null ? "Nuevo proyecto" : "Editar proyecto", true);
        this.proyecto = proyecto;
        this.service  = service;
        initUI();
        if (proyecto != null) cargarDatos();
    }

    private void initUI() {
        setSize(520, 500);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));
        panel.setBackground(new Color(245, 247, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.insets    = new Insets(5, 5, 5, 5);
        gbc.weightx   = 1.0;

        Font fontLabel = new Font("Segoe UI", Font.BOLD, 11);
        Font fontField = new Font("Segoe UI", Font.PLAIN, 12);

        // ── Fila 0: Nombre ────────────────────────────────────────────────────
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; gbc.weightx = 0.3;
        panel.add(label("Nombre *", fontLabel), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 0.7;
        txtNombre = new JTextField(30);
        txtNombre.setFont(fontField);
        panel.add(txtNombre, gbc);

        // ── Fila 1: Tipo / Estado ─────────────────────────────────────────────
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0.3;
        panel.add(label("Tipo *", fontLabel), gbc);
        gbc.gridx = 1; gbc.gridwidth = 1; gbc.weightx = 0.2;
        cmbTipo = new JComboBox<>(Proyecto.Tipo.values());
        cmbTipo.setFont(fontField);
        panel.add(cmbTipo, gbc);

        gbc.gridx = 2; gbc.weightx = 0.3;
        panel.add(label("Estado *", fontLabel), gbc);
        gbc.gridx = 3; gbc.weightx = 0.2;
        cmbEstado = new JComboBox<>(Proyecto.Estado.values());
        cmbEstado.setFont(fontField);
        panel.add(cmbEstado, gbc);

        // ── Fila 2: Fechas ────────────────────────────────────────────────────
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.weightx = 0.3;
        panel.add(label("Inicio (dd/mm/aaaa) *", fontLabel), gbc);
        gbc.gridx = 1; gbc.weightx = 0.2;
        txtFechaInicio = new JTextField(10);
        txtFechaInicio.setFont(fontField);
        panel.add(txtFechaInicio, gbc);

        gbc.gridx = 2; gbc.weightx = 0.3;
        panel.add(label("Fin plan (dd/mm/aaaa) *", fontLabel), gbc);
        gbc.gridx = 3; gbc.weightx = 0.2;
        txtFechaFinPlan = new JTextField(10);
        txtFechaFinPlan.setFont(fontField);
        panel.add(txtFechaFinPlan, gbc);

        // ── Fila 3: Presupuesto ───────────────────────────────────────────────
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; gbc.weightx = 0.3;
        panel.add(label("Presupuesto ($)", fontLabel), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 0.7;
        txtPresupuesto = new JTextField(15);
        txtPresupuesto.setFont(fontField);
        panel.add(txtPresupuesto, gbc);

        // ── Fila 4: Descripción ───────────────────────────────────────────────
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(label("Descripción", fontLabel), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 0.7;
        txtDescripcion = new JTextArea(4, 30);
        txtDescripcion.setFont(fontField);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        panel.add(new JScrollPane(txtDescripcion), gbc);

        // ── Fila 5: Observaciones ─────────────────────────────────────────────
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1; gbc.weightx = 0.3;
        panel.add(label("Observaciones", fontLabel), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 0.7;
        txtObservaciones = new JTextArea(3, 30);
        txtObservaciones.setFont(fontField);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);
        panel.add(new JScrollPane(txtObservaciones), gbc);

        // ── Fila 6: Botones ───────────────────────────────────────────────────
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnPanel.setOpaque(false);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(15, 52, 96));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorderPainted(false);
        btnGuardar.addActionListener(e -> guardar());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnCancelar.addActionListener(e -> dispose());

        btnPanel.add(btnGuardar);
        btnPanel.add(btnCancelar);
        panel.add(btnPanel, gbc);

        add(new JScrollPane(panel));
    }

    private void cargarDatos() {
        txtNombre.setText(proyecto.getNombre());
        txtDescripcion.setText(proyecto.getDescripcion());
        cmbTipo.setSelectedItem(proyecto.getTipo());
        cmbEstado.setSelectedItem(proyecto.getEstado());
        txtFechaInicio.setText(proyecto.getFechaInicio().format(FMT));
        txtFechaFinPlan.setText(proyecto.getFechaFinPlan().format(FMT));
        if (proyecto.getPresupuesto() != null)
            txtPresupuesto.setText(proyecto.getPresupuesto().toPlainString());
        txtObservaciones.setText(proyecto.getObservaciones());
    }

    private void guardar() {
        try {
            LocalDate inicio = parseFecha(txtFechaInicio.getText(), "Inicio");
            LocalDate fin    = parseFecha(txtFechaFinPlan.getText(), "Fin plan");

            Proyecto p = (proyecto != null) ? proyecto : new Proyecto();
            p.setNombre(txtNombre.getText().trim());
            p.setDescripcion(txtDescripcion.getText().trim());
            p.setTipo((Proyecto.Tipo) cmbTipo.getSelectedItem());
            p.setEstado((Proyecto.Estado) cmbEstado.getSelectedItem());
            p.setFechaInicio(inicio);
            p.setFechaFinPlan(fin);
            p.setObservaciones(txtObservaciones.getText().trim());

            String presup = txtPresupuesto.getText().trim().replace(",", ".");
            p.setPresupuesto(presup.isEmpty() ? null : new BigDecimal(presup));

            if (proyecto == null) service.registrar(p);
            else                  service.actualizar(p);

            JOptionPane.showMessageDialog(this, "Proyecto guardado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use dd/mm/aaaa.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El presupuesto debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
        }
    }

    private LocalDate parseFecha(String texto, String campo) {
        if (texto == null || texto.isBlank())
            throw new DateTimeParseException("Campo vacío: " + campo, texto == null ? "" : texto, 0);
        return LocalDate.parse(texto.trim(), FMT);
    }

    private JLabel label(String text, Font font) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        return lbl;
    }
}
