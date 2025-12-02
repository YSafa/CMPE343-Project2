package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

/**
 * Utility class for managing MySQL database connections.
 * Ensures a single, centralized way to connect and close JDBC connections.
 */
public final class DBUtils {

    // Prevent instantiation
    private DBUtils() {}

    /** JDBC connection URL */
    public static final String DB_URL = "jdbc:mysql://localhost:3306/cmpe343_proj2";
    /** Database username */
    public static final String USER = "myuser";
    /** Database password */
    public static final String PASS = "1234"; // Change according to your MySQL setup

    /**
     * Establishes a new database connection.
     */
    public static Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Safely closes the given connection.
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    // ====== EXECUTE QUERIES ======
    /**
     * Executes a SELECT query with optional parameters and returns a ResultSet.
     * The caller is responsible for closing the ResultSet and its Connection.
     *
     * @param sql    the SQL query to execute
     * @param params optional parameters for PreparedStatement
     * @return a {@link ResultSet} if successful, null otherwise
     */
    public static ResultSet executeQuery(String sql, Object... params) {
        try {
            Connection conn = connect();
            if (conn == null) return null;

            PreparedStatement stmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            return stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("DB ERROR Query failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Executes an INSERT, UPDATE, or DELETE query with parameters.
     * Automatically closes the connection after execution.
     *
     * @param sql    the SQL query
     * @param params parameters for PreparedStatement
     * @return number of affected rows, or -1 if failed
     */
    public static int executeUpdate(String sql, Object... params) {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            return stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("DB ERROR Update failed: " + e.getMessage());
            return -1;
        }
    }
}
