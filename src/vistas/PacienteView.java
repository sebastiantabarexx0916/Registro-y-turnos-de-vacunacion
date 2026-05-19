package vistas;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelos.Paciente;

public class PacienteView extends JPanel {
    private JTable tabla;
    private DefaultTableModel model;

    public PacienteView() {
        setLayout(new BorderLayout());
        model = new DefaultTableModel(new Object[]{"ID","Nombre","Apellido","DNI"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tabla = new JTable(model);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    public void setPacientes(List<Paciente> pacientes) {
        model.setRowCount(0);
        for (Paciente p : pacientes) {
            model.addRow(new Object[]{p.getId(), p.getNombre(), p.getApellido(), p.getDni()});
        }
    }
}
