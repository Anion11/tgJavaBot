package org.example.DB;

import java.sql.*;

public class DB extends config {
    public Connection getConnection() {
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, login, password);
        } catch (Exception e) {
            connection = null;
        }
        return connection;
    }
}