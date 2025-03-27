package databasePart1;

import java.sql.*;

public class DatabaseHelper {

    // JDBC driver and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/FoundationDatabase";

    // Database credentials
    static final String USER = "sa";
    static final String PASS = "";

    private Connection connection = null;
    private Statement statement = null;

    // Connect to the database
    public void connectToDatabase() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER); // Load the JDBC driver
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            statement = connection.createStatement();

            createTables();  // Create necessary tables if not already created
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
        }
    }

    // Create tables if not exist
    private void createTables() throws SQLException {
        String userTable = "CREATE TABLE IF NOT EXISTS cse360users ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "userName VARCHAR(255) UNIQUE, "
                + "name VARCHAR(255), "
                + "email VARCHAR(255), "
                + "password VARCHAR(255), "
                + "roles VARCHAR(63), "
                + "isTempPassword BOOLEAN DEFAULT FALSE)";
        statement.execute(userTable);
    }

    // Check if a user exists
    public boolean doesUserExist(String userName) throws SQLException {
        if (connection == null) {
            System.err.println("Database connection is not initialized.");
            return false;
        }

        String query = "SELECT COUNT(*) FROM cse360users WHERE userName = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;  // If count > 0, user exists
            }
        }

        return false;
    }
}
