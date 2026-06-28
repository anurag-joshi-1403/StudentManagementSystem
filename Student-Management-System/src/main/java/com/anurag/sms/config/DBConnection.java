package com.anurag.sms.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/student_management";
    private static final String USER = "root";
    private static final String PASSWORD = "Anur@g66";

    public static Connection getConnection() {
        try {

            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (SQLException e) {

            System.out.println("Database Connection Failed!");
            System.out.println("Error code : "+ e.getErrorCode());
            System.out.println("SQL State : "+ e.getSQLState());
            System.out.println("Message : "+ e.getMessage());

            e.printStackTrace();
            
            return null;
        }
    }
}
