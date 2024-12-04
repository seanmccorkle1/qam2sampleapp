module com.example.qam2sampleapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens com.example.qam2sampleapp to javafx.fxml;
    exports com.example.qam2sampleapp;
}