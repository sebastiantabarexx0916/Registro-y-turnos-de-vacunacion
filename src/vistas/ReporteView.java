package vistas;

import servicios.ReporteService;
import utilidades.JTableHelper;
import utilidades.NotificationHelper;
import utilidades.UIHelper;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

// Nota: requiere la librería de calendario (JDateChooser) en el classpath si se usa en la UI real.
public class ReporteView extends JPanel {
    private ReporteService service;

    private JComboBox<String> cmbTipoReporte;
    private JComponent dcFechaInicio;
    private JComponent dcFechaFin;
    private JComboBox<Object> cmbPaciente;
    private JSpinner spnDias;
    private JButton btnGenerar;
    private JButton btnExportar;
    private JTable tblResultados;
    private DefaultTableModel model;

    public ReporteView() {
        this.service = new ReporteService();
        setLayout(new BorderLayout());
        setBackground(UIHelper.LIGHT_COLOR);
        initComponents();
    }

    private void initComponents() {
        JPanel filtrosPanel = UIHelper.createCardPanel();
        filtrosPanel.setLayout(new GridBagLayout());
        filtrosPanel.setBorder(BorderFactory.createTitledBorder("Filtros de Reporte"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cmbTipoReporte = new JComboBox<>(new String[]{
                "Vacunaciones por fecha",
                "Historial por paciente",
                "Vacunas más aplicadas",
                "Cobertura por edad",
                "Lotes próximos a vencer"
        });

        // Usamos simples JSpinner/JTextField como fallback si JDateChooser no está presente
        dcFechaInicio = new JTextField(10);
        dcFechaFin = new JTextField(10);

        cmbPaciente = new JComboBox<>();
        cargarPacientes();

        spnDias = new JSpinner(new SpinnerNumberModel(30, 1, 365, 1));

        btnGenerar = UIHelper.createPrimaryButton("Generar Reporte");
        btnExportar = UIHelper.createSecondaryButton("Exportar a CSV");
        btnExportar.setEnabled(false);

        gbc.gridx = 0; gbc.gridy = 0;
        filtrosPanel.add(new JLabel("Tipo de reporte:"), gbc);
        gbc.gridx = 1;
        filtrosPanel.add(cmbTipoReporte, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        filtrosPanel.add(new JLabel("Fecha inicio:"), gbc);
        gbc.gridx = 1;
        filtrosPanel.add(dcFechaInicio, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        filtrosPanel.add(new JLabel("Fecha fin:"), gbc);
        gbc.gridx = 1;
        filtrosPanel.add(dcFechaFin, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        filtrosPanel.add(new JLabel("Paciente:"), gbc);
        gbc.gridx = 1;
        filtrosPanel.add(cmbPaciente, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        filtrosPanel.add(new JLabel("Días anticipación:"), gbc);
        gbc.gridx = 1;
        filtrosPanel.add(spnDias, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnGenerar);
        buttonPanel.add(btnExportar);

        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        filtrosPanel.add(buttonPanel, gbc);

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblResultados = new JTable(model);
        JTableHelper.configurarTabla(tblResultados);

        JScrollPane scrollPane = new JScrollPane(tblResultados);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Resultados"));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(filtrosPanel, BorderLayout.NORTH);
        topPanel.add(scrollPane, BorderLayout.CENTER);

        add(topPanel, BorderLayout.CENTER);

        cmbTipoReporte.addActionListener(e -> ajustarVisibilidadFiltros());
        btnGenerar.addActionListener(e -> generarReporte());
        btnExportar.addActionListener(e -> exportarReporte());

        ajustarVisibilidadFiltros();
    }

    private void ajustarVisibilidadFiltros() {
        int tipo = cmbTipoReporte.getSelectedIndex();
        dcFechaInicio.setVisible(tipo == 0);
        dcFechaFin.setVisible(tipo == 0);
        cmbPaciente.setVisible(tipo == 1);
        spnDias.setVisible(tipo == 4);
    }

    private void cargarPacientes() {
        try {
            dao.PacienteDAO dao = new dao.PacienteDAO();
            java.util.List<modelos.Paciente> pacientes = dao.listarTodos();
            cmbPaciente.removeAllItems();
            for (modelos.Paciente p : pacientes) {
                cmbPaciente.addItem(new ComboItem(p.getId(), p.getNombre() + " " + p.getApellido()));
            }
        } catch (Exception e) {
            NotificationHelper.showError("Error al cargar pacientes: " + e.getMessage());
        }
    }

    private void generarReporte() {
        int tipo = cmbTipoReporte.getSelectedIndex();
        utilidades.ResultadoOperacion result = null;

        try {
            switch (tipo) {
                case 0: // Vacunaciones por fecha
                    LocalDate inicio = LocalDate.now();
                    LocalDate fin = LocalDate.now();
                    try {
                        inicio = LocalDate.parse(((JTextField)dcFechaInicio).getText(), java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        fin = LocalDate.parse(((JTextField)dcFechaFin).getText(), java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    } catch (Exception ex) {
                        NotificationHelper.showWarning("Formato fecha inválido. Use yyyy-MM-dd");
                        return;
                    }
                    result = service.reporteVacunacionesPorFecha(inicio, fin);
                    if (result.isSuccess()) {
                        Map<String, Object> datos = (Map<String, Object>) result.getData();
                        mostrarVacunaciones((List<modelos.Vacunacion>) datos.get("datos"));
                    }
                    break;
                case 1:
                    ComboItem item = (ComboItem) cmbPaciente.getSelectedItem();
                    if (item != null) {
                        result = service.reporteVacunacionesPorPaciente(item.getId());
                        if (result.isSuccess()) {
                            Map<String, Object> datos = (Map<String, Object>) result.getData();
                            mostrarVacunaciones((List<modelos.Vacunacion>) datos.get("datos"));
                        }
                    } else {
                        NotificationHelper.showWarning("Seleccione un paciente");
                        return;
                    }
                    break;
                case 2:
                    result = service.reporteVacunasMasAplicadas();
                    if (result.isSuccess()) {
                        Map<String, Object> datos = (Map<String, Object>) result.getData();
                        mostrarEstadisticasVacunas((List<Map<String, Object>>) datos.get("datos"));
                    }
                    break;
                case 3:
                    result = service.reporteCoberturaPorEdad();
                    if (result.isSuccess()) {
                        Map<String, Object> datos = (Map<String, Object>) result.getData();
                        mostrarCoberturaEdad((List<Map<String, Object>>) datos.get("datos"));
                    }
                    break;
                case 4:
                    int dias = (int) spnDias.getValue();
                    result = service.reporteLotesProximosAVencer(dias);
                    if (result.isSuccess()) {
                        Map<String, Object> datos = (Map<String, Object>) result.getData();
                        mostrarLotes((List<modelos.Lote>) datos.get("datos"));
                    }
                    break;
            }

            if (result != null && result.isSuccess()) {
                NotificationHelper.showSuccess(result.getMessage());
                btnExportar.setEnabled(true);
            } else if (result != null) {
                NotificationHelper.showError(result.getMessage());
            }

        } catch (Exception e) {
            NotificationHelper.showError("Error: " + e.getMessage());
        }
    }

    private void mostrarVacunaciones(List<modelos.Vacunacion> vacunaciones) {
        String[] columnas = {"ID", "Paciente", "Vacuna", "Lote", "Fecha", "Dosis", "Usuario", "Estado"};
        model.setColumnIdentifiers(columnas);
        model.setRowCount(0);

        for (modelos.Vacunacion v : vacunaciones) {
            model.addRow(new Object[]{
                    v.getId(),
                    v.getPacienteNombre(),
                    v.getVacunaNombre(),
                    v.getLoteCodigo(),
                    v.getFechaAplicacion() != null ? v.getFechaAplicacion().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "",
                    v.getNumeroDosis(),
                    v.getUsuarioNombre(),
                    v.getEstado()
            });
        }
        JTableHelper.ajustarAnchoColumnas(tblResultados);
    }

    private void mostrarEstadisticasVacunas(List<Map<String, Object>> estadisticas) {
        String[] columnas = {"Vacuna", "Total Aplicaciones"};
        model.setColumnIdentifiers(columnas);
        model.setRowCount(0);

        for (Map<String, Object> row : estadisticas) {
            model.addRow(new Object[]{ row.get("vacuna"), row.get("total") });
        }
        JTableHelper.ajustarAnchoColumnas(tblResultados);
    }

    private void mostrarCoberturaEdad(List<Map<String, Object>> cobertura) {
        String[] columnas = {"Rango de Edad", "Total Vacunados"};
        model.setColumnIdentifiers(columnas);
        model.setRowCount(0);

        for (Map<String, Object> row : cobertura) {
            model.addRow(new Object[]{ row.get("rango"), row.get("total") });
        }
        JTableHelper.ajustarAnchoColumnas(tblResultados);
    }

    private void mostrarLotes(List<modelos.Lote> lotes) {
        String[] columnas = {"ID", "Vacuna", "Código Lote", "Vencimiento", "Stock"};
        model.setColumnIdentifiers(columnas);
        model.setRowCount(0);

        for (modelos.Lote l : lotes) {
            model.addRow(new Object[]{
                    l.getId(),
                    l.getVacunaNombre(),
                    l.getCodigoLote(),
                    l.getFechaVencimiento() != null ? l.getFechaVencimiento().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "",
                    l.getCantidadDisponible()
            });
        }
        JTableHelper.ajustarAnchoColumnas(tblResultados);
    }

    private void exportarReporte() {
        List<modelos.Vacunacion> vacunaciones = new java.util.ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            modelos.Vacunacion v = new modelos.Vacunacion();
            Object idObj = model.getValueAt(i, 0);
            if (idObj instanceof Integer) v.setId((Integer) idObj);
            v.setPacienteNombre(String.valueOf(model.getValueAt(i, 1)));
            v.setVacunaNombre(String.valueOf(model.getValueAt(i, 2)));
            v.setLoteCodigo(String.valueOf(model.getValueAt(i, 3)));
            vacunaciones.add(v);
        }

        String nombreArchivo = "reporte_" + System.currentTimeMillis();
        utilidades.ResultadoOperacion result = service.exportarACSV(vacunaciones, nombreArchivo);

        if (result.isSuccess()) NotificationHelper.showSuccess(result.getMessage());
        else NotificationHelper.showError(result.getMessage());
    }

    private static class ComboItem {
        private int id;
        private String text;
        public ComboItem(int id, String text) { this.id = id; this.text = text; }
        public int getId() { return id; }
        @Override public String toString() { return text; }
    }
}
