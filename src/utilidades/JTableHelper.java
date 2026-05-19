package utilidades;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.TableColumnModel;

public class JTableHelper {
    public static void configurarTabla(JTable table) {
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
    }

    public static void ajustarAnchoColumnas(JTable table) {
        TableColumnModel m = table.getColumnModel();
        for (int i = 0; i < m.getColumnCount(); i++) {
            m.getColumn(i).setPreferredWidth(100);
        }
    }
}
