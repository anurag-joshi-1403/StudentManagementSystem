package com.anurag.sms.menu;

import java.sql.Connection;

import com.anurag.sms.config.DBConnection;

public class TestConnection {
    public static void main(String[] args) {
        Connection connection = DBConnection.getConnection();

        if(connection != null){
            System.out.println("Database Connected Successfully!");
        }
        else {
            System.out.println("Connection Failed!");
        }
    }
}
