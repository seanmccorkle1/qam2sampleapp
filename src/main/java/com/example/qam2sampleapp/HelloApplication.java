package com.example.qam2sampleapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.stage.StageStyle;

import java.util.Objects;

import Utilities.Database.MySQLConnector;
//import Utilities.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.scene.Parent;

import com.mysql.cj.jdbc.Driver;

//import com.mysql.cj.jdbc.*;
//import mysql-connector-j-8.3.0.jar;


public class HelloApplication extends Application {

    private static final String URL = "jdbc:mysql://localhost:3306/client_schedule";
    private static final String USER = "sqlUser";
    private static final String PASSWORD = "Passw0rd!";

    @Override
    public void start(Stage primaryStage) throws Exception{
//        Logger.initializeLogManager();

//        FXMLLoader my_loader=new FXMLLoader(
//                HelloApplication.class.getResource(
//                        "hello-view.fxml"));

//        Parent root = FXMLLoader.load(Objects.requireNonNull(
//                getClass().getResource(
//                        "ThreeReportsView.fxml")));

//        Parent root = FXMLLoader.load(Objects.requireNonNull(
//                getClass().getResource(
//                        "LoginView.fxml")));

//        Parent root = FXMLLoader.load(
//                Objects.requireNonNull(
//                        getClass().getResource("CalendarView.fxml")));

//        Parent root = FXMLLoader.load(Objects.requireNonNull(
//                getClass().getResource(
//                        "AppointmentPane.fxml")));

//        Parent root = FXMLLoader.load(Objects.requireNonNull(
//                getClass().getResource(
//                        "CustomerView.fxml")));

//        primaryStage.setTitle("Welcome")
//        primaryStage.setScene(new Scene(root));

//        Parent root = FXMLLoader.load(Objects.requireNonNull(
//                getClass().getResource(
//                        "AppointmentPane.fxml")));

//        FXMLLoader my_loader=new FXMLLoader(
//                HelloApplication.class.getResource(s
//                        "LoginView.fxml"));

        FXMLLoader my_loader=new FXMLLoader(
                HelloApplication.class.getResource(
                        "CalendarView.fxml"));

//        FXMLLoader my_loader=new FXMLLoader(
//                HelloApplication.class.getResource(
//                        "CustomerView.fxml"));

//        FXMLLoader my_loader=new FXMLLoader(
//                HelloApplication.class.getResource(
//                        "AppointmentPane.fxml"));

//        FXMLLoader my_loader=new FXMLLoader(
//                HelloApplication.class.getResource(
//                        "ThreeReportsView.fxml"));

        Scene my_scene= new Scene(my_loader.load());

        primaryStage.setScene(my_scene);
//        primaryStage.setScene(new Scene(root));


        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }

    public static void main(String[] args) {

        String query = "SELECT * FROM client_schedule.appointments";

        try {

            Connection connect = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement world_db_query = connect.prepareStatement(query);
            ResultSet sql_result = world_db_query.executeQuery();

            System.out.println(sql_result);

            while (sql_result.next()) {
//                int id  = sql_result.getInt("Country_ID");
//                String str = sql_result.getString("Division");
//                System.out.println(id+"\t"+str+"\t");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Connection current_connection = MySQLConnector.open_sql_connection();
            if ( current_connection != null && !current_connection.isClosed() ) {
                System.out.println("Connected to the DB.");
            }
            else System.out.println("Connection failed.");

        }

        catch (Exception e) {e.printStackTrace();}
        finally {MySQLConnector.cancel_sql_connection();}

        launch(args);
        MySQLConnector.cancel_sql_connection();
    }
}