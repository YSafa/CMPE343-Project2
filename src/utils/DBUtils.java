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


}
