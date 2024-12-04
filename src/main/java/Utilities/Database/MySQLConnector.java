package Utilities.Database;
// import mysql-connector-j-8.3.0.jar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;


import com.mysql.cj.jdbc.Driver;
//import com.mysql.jdbc.Driver;
//import com.mysql.cj.jdbc.Driver;

public class MySQLConnector {

    private static final String URL = "jdbc:mysql://localhost:3306/client_schedule";

    private static final String USER  = "sqlUser";
    private static final String PASSWORD = "Passw0rd!";

    private static Connection sql_connection;

    public static Connection open_sql_connection() {
        try {
            DriverManager.registerDriver(new Driver());

            if (sql_connection == null || sql_connection.isClosed()){
                sql_connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }

            return sql_connection;
        } catch (SQLException error) {
            throw new RuntimeException("Connection error: ", error);
        }
    }

    public static void cancel_sql_connection() {
        try {

            if (sql_connection != null && !sql_connection.isClosed())
                sql_connection.close();
        }

        catch (SQLException e) {
            throw new RuntimeException("DB connection failed, ", e);
        }
    }
}