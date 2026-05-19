package controladores;

import dao.UsuarioDAO;
import modelos.Usuario;
import utilidades.NotificationHelper;
import utilidades.Sesion;
import vistas.LoginView;

public class LoginController {
    private LoginView view;
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public LoginController(LoginView view) {
        this.view = view;
        initEvents();
    }

    private void initEvents() {
        view.getBtnLogin().addActionListener(e -> login());
    }

    private void login() {
        String username = view.getUsername();
        String password = view.getPassword();
        try {
            Usuario u = usuarioDAO.findByUsername(username);
            if (u != null && u.getPassword() != null && u.getPassword().equals(password)) {
                Sesion.getInstance().iniciarSesion(u);
                NotificationHelper.showSuccess("Bienvenido " + u.getUsername());
                view.close();
            } else {
                NotificationHelper.showError("Usuario o contraseña inválida");
            }
        } catch (Exception ex) {
            NotificationHelper.showError("Error al autenticar: " + ex.getMessage());
        }
    }
}
