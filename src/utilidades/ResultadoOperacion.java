package utilidades;

public class ResultadoOperacion {
    private boolean success;
    private String message;
    private Object data;

    public ResultadoOperacion() {}
    public ResultadoOperacion(boolean success, String message) { this.success = success; this.message = message; }
    public ResultadoOperacion(boolean success, String message, Object data) { this.success = success; this.message = message; this.data = data; }

    public static ResultadoOperacion success(String message) { return new ResultadoOperacion(true, message); }
    public static ResultadoOperacion success(String message, Object data) { return new ResultadoOperacion(true, message, data); }
    public static ResultadoOperacion error(String message) { return new ResultadoOperacion(false, message); }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Object getData() { return data; }
}
