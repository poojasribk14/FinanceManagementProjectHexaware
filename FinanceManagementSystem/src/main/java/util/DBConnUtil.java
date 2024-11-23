 package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import exception.DatabaseConnectionException;

public class DBConnUtil {

    public static Connection getConnection() throws DatabaseConnectionException {
        Properties properties = DBPropertyUtil.loadProperties();
        String dbURL = properties.getProperty("db.url");
        String dbUser = properties.getProperty("db.username");
        String dbPassword = properties.getProperty("db.password");

        if (dbURL == null || dbUser == null || dbPassword == null) {
            throw new DatabaseConnectionException("Database properties are missing");
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(dbURL, dbUser, dbPassword);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new DatabaseConnectionException("Error connecting to the database", e);
        }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
