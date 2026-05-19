package vistas;

import java.awt.*;
import javax.swing.*;

public class DashboardView extends JPanel {
    public DashboardView() {
        setLayout(new BorderLayout());
        add(new JLabel("Dashboard - VacUNO", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
