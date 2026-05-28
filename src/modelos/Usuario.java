package modelos;

public class Usuario {
    protected int id;
    protected String nombre;
    protected String email;
    protected String contrasena;
    protected String rol;
    protected String cargo;
    protected String telefono;
    protected String cedula;

    public Usuario() {}

    public Usuario(int id, String nombre, String email, String contrasena, String rol, String cargo, String telefono, String cedula) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.rol = rol;
        this.cargo = cargo;
        this.telefono = telefono;
        this.cedula = cedula;
    }

    public boolean autenticar(String email, String contrasena) {
        return this.email.equalsIgnoreCase(email) && this.contrasena.equals(contrasena);
    }

    public void cerrarSesion() {
        System.out.println("Sesión cerrada para el usuario: " + this.nombre + " (" + this.rol + ").");
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
}
