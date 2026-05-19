package vistas;

import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("VacUNO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900,600);
        setLocationRelativeTo(null);
        init();
    }

    private void init() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new DashboardView(), BorderLayout.CENTER);
    }
}
