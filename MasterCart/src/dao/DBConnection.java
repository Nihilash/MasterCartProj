package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/MasterCart?useSSL=false&serverTimezone=UTC";
    private static final String USER = "MasterCart"; // replace with your actual DB username
    private static final String PASSWORD = "1234567";

    // Get DB connection
    public static Connection getConnection() throws SQLException {
        try {
            // Ensure MySQL driver is loaded
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}