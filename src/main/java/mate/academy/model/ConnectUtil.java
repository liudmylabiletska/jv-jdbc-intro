package mate.academy.model;




import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DbConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/your_schema?serverTimezone=UTC";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

    public static Connection getConnection() throws DataProcessingException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new DataProcessingException("Failed to establish database connection", e);
        }
    }
}

