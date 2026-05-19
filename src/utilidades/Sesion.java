package utilidades;

import modelos.Usuario;

public class Sesion {
    private static Sesion instance;
    private Usuario usuarioActual;

    private Sesion() {}

    public static synchronized Sesion getInstance() {
        if (instance == null) instance = new Sesion();
        return instance;
    }

    public void iniciarSesion(Usuario u) { this.usuarioActual = u; }
    public Usuario getUsuarioActual() { return usuarioActual; }
    public void cerrarSesion() { this.usuarioActual = null; }
    public boolean tienePermiso(String rol) {
        if (usuarioActual == null) return false;
        return usuarioActual.getRol() != null && usuarioActual.getRol().equalsIgnoreCase(rol);
    }
}
