package vistas;

import java.awt.*;
import javax.swing.*;

public class LoginView extends JDialog {
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;

    public LoginView() {
        setTitle("Login");
        setModal(true);
        setSize(300,180);
        setLocationRelativeTo(null);
        init();
    }

    private void init() {
        JPanel p = new JPanel(new GridLayout(3,2,5,5));
        p.add(new JLabel("Usuario:")); txtUser = new JTextField(); p.add(txtUser);
        p.add(new JLabel("Contraseña:")); txtPass = new JPasswordField(); p.add(txtPass);
        btnLogin = new JButton("Entrar"); p.add(new JLabel()); p.add(btnLogin);
        add(p);
    }

    public String getUsername() { return txtUser.getText(); }
    public String getPassword() { return new String(txtPass.getPassword()); }
    public JButton getBtnLogin() { return btnLogin; }
    public void close() { setVisible(false); dispose(); }
}
