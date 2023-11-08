//DatabaseConnection.java
package com.example.mylogin;

import android.annotation.SuppressLint;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // JDBC URL, username, and password of SQL Server
    private static final String JDBC_URL = "jdbc:jtds:sqlserver://csunstudy-db.cbef1ecg07tv.us-west-1.rds.amazonaws.com:1433/csunstudy;ssl=true;sslfactory=org.jtds.ssl.MD5TdsTlsSocketFactory";
    private static final String USERNAME = "cjjk";
    private static final String PASSWORD = "comp490fall2023";

    @SuppressLint("NewApi")
    public static Connection connect() {
        Connection connection = null;
        try {
            // Register the jTDS driver
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            System.out.println("made it here 1");
            // Create the connection
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            System.out.println("made it here 2");

            if (connection != null) {
                System.out.println("Connected to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to the database. Error: " + e.getMessage());
            System.err.println("Failed to connect to the database. Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("jTDS driver not found. Make sure to add the jTDS driver JAR to your project.");
            System.err.println("jTDS driver not found. Make sure to add the jTDS driver JAR to your project.");
        }
        System.out.println("made it here 3");
        return connection;
    }

    public static void disconnect(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Disconnected from the database.");
            } catch (SQLException e) {
                System.err.println("Failed to disconnect from the database. Error: " + e.getMessage());
            }
        }
    }
}

