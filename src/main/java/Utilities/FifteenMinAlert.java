package Utilities;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class FifteenMinAlert {

    private final static Locale this_locale = Locale.getDefault();

    private final static ResourceBundle BUNDLE =ResourceBundle.getBundle("com.example.qam2sampleapp.MessageBundle", this_locale);

    private static Alert confirm_alert;
    public enum ConfirmType {CANCEL, DELETE, EXIT};

    public static boolean confirmation(ConfirmType exit_cancel_delete) {

        confirm_alert = new Alert(Alert.AlertType.CONFIRMATION);
        String header = "header" + exit_cancel_delete;
        String msg = "body" + exit_cancel_delete;

        String type_of_header = BUNDLE.getString(header);
        String type_of_msg =  BUNDLE.getString(msg);

        confirm_alert.setHeaderText(type_of_header);
        confirm_alert.setContentText(type_of_msg);

        Optional<ButtonType> dialog = confirm_alert.showAndWait();

        return (dialog.isPresent() && dialog.get() == ButtonType.OK);
    }

    public static boolean my_confirmation(String custom_title, String custom_msg) {

        confirm_alert = new Alert(Alert.AlertType.CONFIRMATION);
        confirm_alert.setHeaderText(custom_title);

        confirm_alert.setContentText(custom_msg);
        Optional<ButtonType> dialog = confirm_alert.showAndWait();

        return (dialog.isPresent() && dialog.get() == ButtonType.OK);
    }

    public static void show_error_alert(String title, String body ) {

        confirm_alert = new Alert(Alert.AlertType.ERROR);

        confirm_alert.setHeaderText(title);
        confirm_alert.setContentText(body);
        confirm_alert.show();
    }

    public static void show_info_alert(String bilingual_title, String bilingual_body) {
        confirm_alert = new Alert(Alert.AlertType.INFORMATION);

        confirm_alert.setHeaderText(bilingual_title);
        confirm_alert.setContentText(bilingual_body);

        confirm_alert.show();
    }
}