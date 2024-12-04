package com.example.qam2sampleapp;

import Entities.User;
import Utilities.Database.UserObject;
import java.io.IOException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoginViewController {

    @FXML
    private Text header;

    /**Very rougly guesses the user's time zone.
     * <br/>
     * I don't know if its quite accurate
     */
    @FXML
    private Label time_zone;

    /**Prompt text is multilingual*/
    @FXML
    private TextField username_input_field;

    /**Promp text is multilingual*/
    @FXML
    private PasswordField password_input_field;

    /**
     * My favorite flags: Ecuador, Pakistan, Egypt
     * The default flag is American of course
     */
    @FXML
    private ImageView flag;

    /**
     * Green login button
     * <br/>
     * When clicked with {@code onMouseClicked} it runs the {@code log_in()} fn
     * */
    @FXML
    private Button login_button;

    /**
     * 'cancel' or 'exit' button
     * <br/>
     * Need to make it red
     * <br/>
     * changes with the language too, french, spanish, and english
     * */
    @FXML
    private Button exit_button;

    /**
     * This changes with the user's language
     */
    @FXML
    private Text error_text;

    /**
     * More of a sublabel, like a subtitle.
     * */
    @FXML
    private Label label;
    /**
     * Simply for the exit button
     */
    @FXML
    void exit_app() {
        Stage stage = (Stage) exit_button.getScene().getWindow();
        stage.close();
    }

//    private final static Logger logger_object = Logger.getLogger(CalendarController.class.getName());

    //    private final static Locale this_locale =
//            new Locale ("en", "IN");

    private final static Locale this_locale = Locale.getDefault();
    private final static ResourceBundle BUNDLE =
            ResourceBundle.getBundle(
                    "com.example.qam2sampleapp.MessageBundle",
                    this_locale);

    /**
     * After logging in, it goes straight to {@code CalendarView.fxml}
     * <br/>
     * And showws you all the appmts
     * <br/>
     * Calls @{code get_user_credentials(u,p} function which calls User.java
     * */
    @FXML
    void log_in() throws IOException {

        try {
            String field1_text =  username_input_field.getText();
            String field2_text  =  password_input_field.getText();

            User current_user = get_credentials(field1_text, field2_text);

            String name =current_user.get_user();
//            logger_object.log(Level.INFO, name + " LOGGED IN.");

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("CalendarView.fxml"));
            Parent newRoot = loader.load();

            CalendarController controller = loader.getController();
            controller.initialize_calendar(current_user);

            Stage new_window = new Stage();
            new_window.setScene(new Scene(newRoot));
//            new_window.setTitle("Calendar");
            new_window.setResizable(true);
            new_window.initStyle(StageStyle.DECORATED);
            new_window.setOnCloseRequest(windowEvent -> Platform.exit());

            Stage currentStage = (Stage) login_button.getScene().getWindow();
            currentStage.close();
            new_window.show();
        }

        catch (NoSuchElementException e) {

            String wrong_input = username_input_field.getText();
            String bilingual_error_msg = BUNDLE.getString("loginFailed");

            error_text.setText(bilingual_error_msg);
        }

    }

    /**
     * Does the  multilingual labels and da flag
     * */
    @FXML
    void initialize() {

        String eng_or_french_title = BUNDLE.getString("Title");
        String subtitle=BUNDLE.getString("subtitle");
        String username_eng_or_french = BUNDLE.getString("usernameText");
        String password_eng_or_french = BUNDLE.getString("passwordText");
        String login_eng_or_french = BUNDLE.getString("login");
        String exit_eng_or_french = BUNDLE.getString("quit");

        ZoneId local_zone = ZoneId.systemDefault();

        error_text.setText("");
        login_button.setText(login_eng_or_french);
        exit_button.setText(exit_eng_or_french);

        time_zone.setText(local_zone.toString());
        label.setText(subtitle);
        header.setText(eng_or_french_title);

        username_input_field.setPromptText(username_eng_or_french);
        password_input_field.setPromptText(password_eng_or_french);

        String cntry=Locale.getDefault().getCountry();

        // some common countries and their flags
        String flag_path = switch(cntry) {
            case "UK" -> "/com/example/qam2sampleapp/flags/uk512.png";
            case "CA" -> "/com/example/qam2sampleapp/flags/canada.png";
            case "BR","PT" -> "/com/example/qam2sampleapp/flags/brazil512.png";
            case "IN" -> "/com/example/qam2sampleapp/flags/india.png";
            case "PK" -> "/com/example/qam2sampleapp/flags/pak512.png";
            case "BN" -> "/com/example/qam2sampleapp/flags/bengal512.png";
            case "ID" -> "/com/example/qam2sampleapp/flags/indo512.png";
            case "PH" -> "/com/example/qam2sampleapp/flags/ph1024.png";
            case "EG" -> "/com/example/qam2sampleapp/flags/egypt512.png";
            case "MX", "ES" -> "/com/example/qam2sampleapp/flags/mexico512.png";
            case "GT", "SV","HN", "NI","PA","CR" -> "/com/example/qam2sampleapp/flags/guatemala.png";
            case "CO", "VE", "EC" -> "/com/example/qam2sampleapp/flags/ecuador.png";
            case "NG", "ZA", "ET", "TZ","UG", "KE" -> "/com/example/qam2sampleapp/flags/africa.png";
            case "RU", "KZ", "BY" -> "/com/example/qam2sampleapp/flags/russia.png";
            case "FR", "DE", "IT", "NL", "RO", "PL" -> "/com/example/qam2sampleapp/flags/eu512.png";
            case "SY", "IQ", "DZ", "SA", "SD", "AE" -> "/com/example/qam2sampleapp/flags/arab512.png";
            default -> "/com/example/qam2sampleapp/flags/us1024.png";
        };

        try {
            flag.setImage(new Image(getClass().getResourceAsStream(flag_path)));
        }
        catch (NullPointerException e) {
            System.err.println("FLAG NOT FOUND -> " + flag_path);
            e.printStackTrace();
        }

        /**
         * Make it so the  'enter' key  works even while typing
         */

        username_input_field.addEventHandler(KeyEvent.KEY_PRESSED, keypress-> {

            if (keypress.getCode() == KeyCode.ENTER) {

                try{
                    log_in();
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
        });

        /**
         * Same idea
         */
        password_input_field.addEventHandler(KeyEvent.KEY_PRESSED, keypress-> {
            if (keypress.getCode() == KeyCode.ENTER) {
                try {
                    log_in();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        /**
         * Make  it so it works inside the button
         */
        login_button.addEventHandler(KeyEvent.KEY_PRESSED, keypress-> {
            if (keypress.getCode() == KeyCode.ENTER) {
                try{
                    log_in();
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Calls UserObject.java which takes the user data from the DB
     * */
    private User get_credentials(String username, String password) throws NoSuchElementException {

        UserObject user_object = new UserObject();
        Optional<User> user = user_object.getUserByUserNameAndPassword(username, password);
        return user.orElseThrow();
    }
}