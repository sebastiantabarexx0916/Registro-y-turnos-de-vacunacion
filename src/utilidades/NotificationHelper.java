package utilidades;

import javax.swing.*;

public class NotificationHelper {
    public static void showSuccess(String msg) { JOptionPane.showMessageDialog(null, msg, "OK", JOptionPane.INFORMATION_MESSAGE); }
    public static void showError(String msg) { JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE); }
    public static void showWarning(String msg) { JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.WARNING_MESSAGE); }
    public static void showInfo(String msg) { JOptionPane.showMessageDialog(null, msg, "Info", JOptionPane.INFORMATION_MESSAGE); }
}
