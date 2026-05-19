package utilidades;

import java.awt.*;
import javax.swing.*;

public class UIHelper {
    public static final Color LIGHT_COLOR = new Color(250,250,250);

    public static JPanel createCardPanel() {
        JPanel p = new JPanel();
        p.setBackground(LIGHT_COLOR);
        return p;
    }

    public static JButton createPrimaryButton(String text) {
        JButton b = new JButton(text);
        return b;
    }

    public static JButton createSecondaryButton(String text) {
        JButton b = new JButton(text);
        return b;
    }
}
