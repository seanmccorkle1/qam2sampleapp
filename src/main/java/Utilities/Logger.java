package Utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.LogManager;

public class Logger {

    public static void initializeLogManager() throws IOException {
        LogManager.getLogManager().readConfiguration(
                new FileInputStream(
                        "./src/main/java/Resource/logging.properties"));
//        LogManager.getLogManager().readConfiguration(
//                new FileInputStream(
//                        "logging.properties"));

        //        LogManager.getLogManager().readConfiguration(new FileInputStream("./src/Resource/logging.properties"));
    }
}