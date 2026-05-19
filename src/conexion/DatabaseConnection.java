package conexion;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/db_vacunacion?useSSL=false&serverTimezone=UTC";
    private static final String DEFAULT_USER = "root";
    private static final String DEFAULT_PASS = "";

    public static Connection getConnection() throws SQLException {
        try {
            // Load optional config from classpath resource db.properties
            Properties props = new Properties();
            try (InputStream is = DatabaseConnection.class.getResourceAsStream("/db.properties")) {
                if (is != null) props.load(is);
            } catch (Exception ignored) {}

            String url = props.getProperty("db.url", DEFAULT_URL);
            String user = props.getProperty("db.user", DEFAULT_USER);
            String pass = props.getProperty("db.pass", DEFAULT_PASS);

            // Ensure driver auto-registered (modern drivers do this automatically)
            return DriverManager.getConnection(url, user, pass);
        } catch (SQLException ex) {
            throw ex;
        }
    }
}
