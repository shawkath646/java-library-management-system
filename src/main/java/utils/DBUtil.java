package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/library_app";
    private static final String DB_USER = "library_app";
    private static final String DB_PASSWORD = "11111111";
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(DB_DRIVER);
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found. Add mysql-connector-java to your classpath.", e);
        }
    }
    
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    public static String getDatabaseUrl() {
        return DB_URL;
    }
}
