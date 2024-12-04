package com.example.qam2sampleapp;

//import javafx.application.Application;

import Entities.Appointment;
import Entities.Customer;
import Entities.User;
import Utilities.Database.AppointmentObject;
import Utilities.Database.CustomerObject;
import Utilities.FifteenMinAlert;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CalendarController {
    private final static Locale this_locale = Locale.getDefault();

    private final static ResourceBundle BUNDLE = ResourceBundle.getBundle(
            "com.example.qam2sampleapp.MessageBundle", this_locale);

//    private final static Logger logger_object = Logger.getLogger(CalendarController.class.getName());

    private final ObservableList<Appointment> appointments_list = FXCollections.observableArrayList();
    private final FilteredList<Appointment> filtered_appointments_list = new FilteredList<>(appointments_list);

    private final ObservableList<Customer> customers_list = FXCollections.observableArrayList();

    //    private final Customer customer_object = new Customer();

    private final CustomerObject customer_object = new CustomerObject();
    private final AppointmentObject appointment_class = new AppointmentObject();

    //11/13/2024 10:23 pm
    private final static DateTimeFormatter full_date_format = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a z");

    // MDY date format
    private final static DateTimeFormatter date_formatting = DateTimeFormatter.ofPattern("MM/dd/yyyy");


    private User user;

    @FXML
    private Label username_text;
    @FXML
    private Label date_text;

    @FXML
    private ImageView flag;
    @FXML
    private Button reports_button;

    @FXML
    private ToggleGroup calendar_toggle;

    @FXML
    private TableView<Appointment> appointments_table;
    @FXML
    private TableView<Customer> customers_table;

    @FXML
    private TableColumn<Appointment, Integer> appmt_id;
    @FXML
    private TableColumn<Appointment, String> appmt_title;
    @FXML
    private TableColumn<Appointment, String> appmt_desc;
    @FXML
    private TableColumn<Appointment, String> appmt_location;
    @FXML
    private TableColumn<Appointment, String> appmt_is_with;
    @FXML
    private TableColumn<Appointment, String> appmt_type;
    @FXML
    private TableColumn<Appointment, LocalDateTime> appmt_start_date;
    @FXML
    private TableColumn<Appointment, LocalDateTime> appmt_end_date;

    @FXML
    private TableColumn<Appointment, Integer> appmt_id2;

    // The other table
    @FXML
    private TableColumn<Customer, Integer> customer_id_column;
    @FXML
    private TableColumn<Customer, String> name_column;
    @FXML
    private TableColumn<Customer, String> address_column;

    @FXML
    private TableColumn<Customer, String> country_column;
    @FXML
    private TableColumn<Customer, String> division_column;
    @FXML
    private TableColumn<Customer, String> zip_code_column;

    @FXML
    private TableColumn<Customer, String> phone_num_column;


    @FXML
    void show_all_reports() throws IOException {

        Parent reports_view = FXMLLoader.load(
                Objects.requireNonNull(getClass().getResource(
                        "ThreeReportsView.fxml")));

        Stage new_window = new Stage();

        new_window.setScene(new Scene(reports_view));
        new_window.setResizable(false);
        new_window.setTitle(BUNDLE.getString("hi"));

        new_window.initStyle(StageStyle.DECORATED);
        new_window.show();
    }

    @FXML
    void add_appmt() throws IOException {
        FXMLLoader fxml_loader = new FXMLLoader();

        fxml_loader.setLocation(getClass().getResource("AppointmentPane.fxml"));
        Parent appointment_window = fxml_loader.load();

        AppointmentPaneController appointment_controller = fxml_loader.getController();

        Consumer<Appointment> appmt_processor = appmt -> {

            if (appointment_conflict_exists(appmt)){

                String att= BUNDLE.getString("attention");
                String no_time = BUNDLE.getString("noTime");
                FifteenMinAlert.show_info_alert(att, no_time);

                System.out.println("Conflict");
                return;
            }

//            if (user == null){
//                user= new User(9, "Sample user");
//            }

            int user_id = user.get_user_id();
            String the_user=user.get_user();

            appmt.set_author_of_appointment(the_user);
            appmt.set_appmt_user(the_user);

            appmt.set_most_recently_updated_person(the_user);
            appmt.set_appmt_user_id(user_id);

            if (appointment_class.insert(appmt)) {
                appointments_list.add(appmt);
            }

        };

//        appointment_controller.initialize_calendar(null, customers_list, appmt_processor);
        appointment_controller.initialize_appointment(null, customers_list, appmt_processor);

        Stage current_window = new Stage();
        current_window.setScene(new Scene(appointment_window));
        current_window.setTitle("Add a appointment");
        current_window.setResizable(false);
        current_window.initStyle(StageStyle.DECORATED);

        current_window.show();
    }

    @FXML
    void add_customer() throws IOException {

        FXMLLoader fxml_loader = new FXMLLoader();
        fxml_loader.setLocation(getClass().getResource("CustomerView.fxml"));

        Parent add_customer_window = fxml_loader.load();
        CustomerViewController customer_controller = fxml_loader.getController();
//        loader.setLocation(getClass().getResource("CalendarView.fxml"));

        Consumer<Customer> customer_processor = this_customer -> {

            String name = user.get_user();
            String creator = this_customer.get_created_person();

            this_customer.set_updated_person(name);

            if (creator == null){
                this_customer.set_created_person(name);
            }

            // add him to the list
            if (customer_object.insert(this_customer)){
                customers_list.add(this_customer);
            }
        };

        customer_controller.initialize_customer_data(null, customer_processor);

        String window_title=BUNDLE.getString("createCustomer");
        if (window_title==null){
            window_title="Creating a customer";
        }

        Stage current_window = new Stage();
        current_window.setScene(new Scene(add_customer_window));
        current_window.setTitle(window_title);
        current_window.setResizable(false);
        current_window.initStyle(StageStyle.DECORATED);
        current_window.show();
    }
    // If all 'ifs' pass, the 'appmt' gets removed..
    // because it makes it to the bottom of the code

    @FXML
    void delete_appmt() {

        // if "delete" isn't showing, undo the action
        if (!FifteenMinAlert.confirmation(FifteenMinAlert.ConfirmType.DELETE)){
            return;
        }

        Appointment appmt = appointments_table.getSelectionModel().getSelectedItem();

        if (appmt == null) {
            FifteenMinAlert.show_info_alert(
                    BUNDLE.getString("attention"),
                    BUNDLE.getString("noAppointment"));
            return;
        }

        if (!appointment_class.delete(appmt.get_appmt_id())) {
            FifteenMinAlert.show_error_alert(
                    BUNDLE.getString("error"),
                    BUNDLE.getString("errorDetailed"));
            return;
        }

        appointments_list.remove(appmt);
    }

    @FXML
    void remove_customer() {
        if (!FifteenMinAlert.confirmation(FifteenMinAlert.ConfirmType.DELETE)){
            System.out.println("failed");
            return;
        }

        Customer customer = customers_table.getSelectionModel().getSelectedItem();

        if (customer == null) {
            FifteenMinAlert.show_info_alert(
                    BUNDLE.getString("attention"),
                    BUNDLE.getString("noCustomer"));
            return;
        }

        if (appointments_list.stream().anyMatch(appmt -> appmt.get_customer_code() == customer.get_customer_id())) {

            if (!FifteenMinAlert.my_confirmation("Delete everything?", "Do you want to keep deleting?")){
                return;
            }

            if (!appointment_class.delete_all_appointments_of_a_customer(customer.get_customer_id())) {
                FifteenMinAlert.show_error_alert(
                        BUNDLE.getString("error"),
                        BUNDLE.getString("cantDelete"));
                return;
            }

            // LAMBDA
            appointments_list.removeIf(appmt -> appmt.get_customer_code() == customer.get_customer_id());
        }

        int cust_id = customer.get_customer_id();
        boolean delete_successful = customer_object.delete(cust_id);

        // if the deletion failed
        if ( !delete_successful) {
            FifteenMinAlert.show_error_alert(BUNDLE.getString("error"),
                    BUNDLE.getString("cantDelete"));
        }

        customers_list.remove(customer);
    }

    @FXML
    void switch_calendar_view() {

        RadioButton view_all_reports_button = (RadioButton) calendar_toggle.getSelectedToggle();

        filtered_appointments_list.setPredicate(appmt -> {

            String week_or_month = view_all_reports_button.getText();

            LocalDateTime appmt_time = appmt.get_start_hour();

            LocalDateTime now = LocalDateTime.now();
            LocalDate today = LocalDate.now();

            // don't show past days
            boolean lower_bound = appmt_time.isAfter(today.atStartOfDay());

            // Show appmts in the next week
            if (week_or_month.equals("Week")) {
                LocalDateTime one_week_in_the_future = now.plusDays(7);
                return lower_bound && appmt_time.isBefore(one_week_in_the_future);
            }

            // Show appointments coming in the next month
            // Also make sure they're not in the past

            if (week_or_month.equals("Month")) {
                return lower_bound && appmt_time.isBefore(now.plusDays(30));
            }
            return true;
        });
    }

    @FXML
    void update_appmt() throws IOException {

        // TableView.getSelectionModel()
        Appointment this_appointment = appointments_table.getSelectionModel().getSelectedItem();

        if (this_appointment == null) {
            FifteenMinAlert.show_info_alert(
                    BUNDLE.getString("attention"),
                    BUNDLE.getString("noAppointment"));
            return;
        }

        Consumer<Appointment> update_appmt_processor = updated_appointment -> {
            String name =  user.get_user();

            updated_appointment.set_author_of_appointment(name);
            updated_appointment.set_most_recently_updated_person(name);

            if (appointment_class.update(updated_appointment)) {
                int appmt_index = appointments_list.indexOf(this_appointment);
                appointments_list.set(appmt_index, updated_appointment);
            }
        };

        FXMLLoader fxml_loader = new FXMLLoader();

        fxml_loader.setLocation(getClass().getResource("AppointmentPane.fxml"));

        Parent new_load = fxml_loader.load();
        AppointmentPaneController appmt_view_controller = fxml_loader.getController();

        appmt_view_controller.initialize_appointment(this_appointment, customers_list, update_appmt_processor);

        Stage current_window = new Stage();
        current_window.setScene(new Scene(new_load));

        if (BUNDLE.getString("changeAppointment").length() > 3) {
            current_window.setTitle(BUNDLE.getString("changeAppointment"));
        }
        else {
            current_window.setTitle("Update appointment");
        }

//        current_window.setTitle(BUNDLE.getString("changeAppointment"));
        current_window.setResizable(false);
        current_window.initStyle(StageStyle.DECORATED);

        current_window.show();
    }

    @FXML
    void update_customer() throws IOException {

        Customer cust = customers_table.getSelectionModel().getSelectedItem();

        if (cust == null) {
            FifteenMinAlert.show_info_alert(
                    BUNDLE.getString("attention"),
                    BUNDLE.getString("noCustomer"));
            return;
        }


        Consumer<Customer> update_customer_processor = updated_cust -> {

            String name = user.get_user();
            updated_cust.set_updated_person(name);

            if (customer_object.update(updated_cust)){
                customers_list.set(customers_list.indexOf(cust), updated_cust);
            }
        };

        FXMLLoader fxml_loader = new FXMLLoader();
        fxml_loader.setLocation(getClass().getResource("CustomerView.fxml"));

        Parent customer_window = fxml_loader.load();
        CustomerViewController customer_controller = fxml_loader.getController();
        customer_controller.initialize_customer_data(cust, update_customer_processor);


        Stage current_window = new Stage();
        current_window.setScene(new Scene(customer_window));

        if (BUNDLE.getString("changeCustomer").length() >2){
            current_window.setTitle(BUNDLE.getString("changeCustomer"));
        }
        else current_window.setTitle("Change cust");

        current_window.setResizable(false);
        current_window.initStyle(StageStyle.DECORATED);

        current_window.show();
    }

    @FXML
    void initialize() {

        String cntry=Locale.getDefault().getCountry();

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
        String formatted_date= LocalDate.now().format(date_formatting);
        date_text.setText(formatted_date);

        load_appmts();
        load_customers();

//        ObservableList<Appointment> temp = FXCollections.observableArrayList(new Appointment(6,"temp","des","loc","type", LocalDateTime.now(), LocalDateTime.now().plusMinutes(30), LocalDateTime.now(), "pers", LocalDateTime.now(),"last",4,"cu",9,"cont",90,"us"));

//        load_appmts;

//        appointments_list.addAll(temp);
//        appointments_list.add(temp);

        init_appmt_table();
        init_customers_table();
        switch_calendar_view();
    }

    // Checks if there's appointment in the next 15 minutes
    public void initialize_calendar(User user) {
        this.user = user;
        username_text.setText(this.user.get_user());

        load_customers();
        load_appmts();

        if (has_appmt_in_15_min()) {
            FifteenMinAlert.show_info_alert(
                    BUNDLE.getString("attention"),
                    BUNDLE.getString("nearFuture"));
        }
    }

    private boolean has_appmt_in_15_min() {

        LocalDate current_day = LocalDate.now();
        LocalTime now_moment = LocalTime.now();

        // Check if any appointment meets the criteria
        // Only appointments scheduled for today

        // return appmt_time.isBefore(now_moment.plusMinutes(15)
        //    * "is within 15 minutes"
        //   * Does within 15 minutes means within 14:59 minutes?

        return appointments_list.stream()
                .filter(appmt -> appmt.get_user_id() == user.get_user_id())
                .filter(appmt-> {
                    LocalDate local_day = appmt.get_start_hour().toLocalDate();
                    return local_day.isEqual(current_day);
                }).anyMatch(appmt-> {
                    LocalTime appmt_time= appmt.get_start_hour().toLocalTime();
                    return appmt_time.isBefore(now_moment.plusMinutes(15));
                });
    }

    private void init_customers_table() {

        customers_table.setItems(customers_list);
//        customer_id_column.setCellValueFactory(new PropertyValueFactory<>("customer_id"));

        customer_id_column.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_customer_id() : 74));
        name_column.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_customer_name() : "angel"));

        address_column.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_address() : "1423 revolution dr"));

        zip_code_column.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_zip_code() : "23112"));

        phone_num_column.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue()!=null ? c.getValue().get_phone() : "8043826695"));

        division_column.setCellValueFactory(c->new SimpleObjectProperty<>(c.getValue()!=null ? c.getValue().get_division() : "Guadalajara"));

        country_column.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_country() : "Mexico"));

//        name_column.setCellValueFactory(new PropertyValueFactory<>("name"));

//        address_column.setCellValueFactory(new PropertyValueFactory<>("home_address"));
//        zip_code_column.setCellValueFactory(new PropertyValueFactory<>("zip_code"));
//        phone_num_column.setCellValueFactory(new PropertyValueFactory<>("phone_num"));
//        division_column.setCellValueFactory(new PropertyValueFactory<>("division_name"));
//        country_column.setCellValueFactory(new PropertyValueFactory<>("country_name"));
    }

    private void init_appmt_table() {

        appointments_table.setItems(appointments_list);

//                ObservableList<Appointment> a = FXCollections.observableArrayList(new Appointment(5,"a","s","d","type", LocalDateTime.now(), LocalDateTime.now().plusMinutes(14), LocalDateTime.now(), "pers", LocalDateTime.now(),"last2",94,"c",11,"contact",94,"me"));

//        appointments_table.setItems(a);

//        Appointment app= new Appointment();
//
//        app.set_appmt_id(92);
//        app.set_appmt_title("demo");
//        app.set_appmt_text("stuff");
//        app.set_appmt_location("Lahore");
//
//        app.set_appmt_type("help");
//
//        LocalDate d=LocalDate.now().plusDays(1);
//
//        LocalDate day_of_appmt = LocalDate.now();
//        LocalDate day_of_appmt_end = d;
//
//        LocalTime go = LocalTime.now();
//        LocalTime time_of_appmt=go;
//        LocalTime time_of_appmt_end=go.plusMinutes(30);
//
////        LocalDateTime ldt=  LocalDateTime.now();
////        LocalDateTime.of(day_of_appmt, time_of_appmt)
//
//        LocalDateTime start_of_appmt = LocalDateTime.of(day_of_appmt, time_of_appmt);
//        LocalDateTime end_of_appmt = LocalDateTime.of(day_of_appmt_end,  time_of_appmt_end);
//
//        app.set_start_hour(start_of_appmt);
//        app.set_end_hour(end_of_appmt);
//
//        Contact host =new Contact(4,"karachi", "seanspy7@gmail.com");
//
//        app.set_contact_code(host.get_contact_id());
//        app.set_contact(host.get_contact_name());
//
////        Customer guest  = new Customer();
//
//        app.set_customer_code(5);
//        app.set_customer_name("sean");
//
////        LocalDateTime now_moment = LocalDateTime.now();
//
////        LocalDateTime create_time = LocalDateTime.now();
//        app.set_creation_time(LocalDateTime.now());
//        app.set_start_hour(LocalDateTime.now());

//        if (create_time == null) {
//            app.set_creation_time(now_moment);
//        }

        // this one updates either way
//        app.set_most_recently_updated_time(LocalDateTime.now());

//        filtered_appointments_list.add(app);

//        Appointment app2 = new Appointment(7, "hi", "english","lahore","meeting", LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), LocalDateTime.now(), "sen",LocalDateTime.now(), "sean", 15, "buyer", 9, "cont", 12,"useruser");

//            Appointment my_app=new Appointment(1, "Default Title 1", "Default Description 1", "Default Location 1", "type", LocalDateTime.now(),LocalDateTime.now().plusMinutes(15), LocalDateTime.now(), "created guy", LocalDateTime.now(), "guy",11,"cust",99, "cont", 91,"useruser");

//        appointments_list.add(app);
//        appointments_list.add(app2);
//        appointments_list.add(my_app);

//        System.out.println(app);
//        System.out.println(app2);
//        System.out.println(appointments_list);

//        appointments_table.setItems(filtered_appointments_list);

//    new Appointment(2, "Default Title 2", "Default Description 2", "Default Location 2", "Jane Smith", "Call")

        appmt_id.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_appmt_id() : 74));

        appmt_title.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_appmt_title() : "Sample"));

        appmt_desc.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_appmt_text() : "thingy"));
        appmt_location.setCellValueFactory(c ->new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_appmt_location() : "america"));
        appmt_is_with.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_contact_name() : "contaact"));

        appmt_type.setCellValueFactory(c ->
                new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_appmt_type() : "typey"));

        appmt_id2.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_customer_code() : 73));

//        appmt_id.setCellValueFactory(new PropertyValueFactory<>("id"));

//        appmt_title.setCellValueFactory(new PropertyValueFactory<>("title"));
//        appmt_desc.setCellValueFactory(new PropertyValueFactory<>("desc"));

//        appmt_location.setCellValueFactory(new PropertyValueFactory<>("location"));
//        appmt_is_with.setCellValueFactory(new PropertyValueFactory<>("contact"));

//        appmt_type.setCellValueFactory(new PropertyValueFactory<>("type"));


//        appmt_start_date.setCellValueFactory(new PropertyValueFactory<>("start"));

//        appmt_start_date.setCellFactory(new Callback<>() {
//            @Override
//            public TableCell<Appointment, LocalDateTime> call(TableColumn<Appointment, LocalDateTime> col) {
//                return new TableCell<>() {
//                    @Override
//                    protected void updateItem(LocalDateTime time, boolean is_empty) {
//                        super.updateItem(time, is_empty);
//                        System.out.println(LocalDateTime.of(LocalDate.now(),LocalTime.now()).format(full_date_format));
//
//                        if (is_empty) {
//                            setText(null);
//                        }
//                        else {
//                            setText(time.atZone(ZoneId.systemDefault()).format(full_date_format));
//
//                        }
//                    }
//                };
//            }
//        });


//        appmt_end_date.setCellValueFactory(new PropertyValueFactory<>("end"));
//        appmt_end_date.setCellFactory(new Callback<>() {
//            @Override
//            public TableCell<Appointment, LocalDateTime> call(TableColumn<Appointment, LocalDateTime> col) {
//                return new TableCell<>() {
//                    @Override
//                    protected void updateItem(LocalDateTime time, boolean is_empty) {
//                        super.updateItem(time, is_empty);
//
//                        if (is_empty) {
//                            setText(null);
//                        }
//                        else {
//                            ZonedDateTime zoned = time.atZone(ZoneId.systemDefault());
//                            String formatted = zoned.format(full_date_format);
//                            setText(formatted);
//                        }
//                    }
//                };
//            }
//        });


//        appmt_id2.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
    }

    private boolean appointment_conflict_exists(Appointment result) {
        return appointments_list.stream()
                .filter(appmt -> {
                    LocalDateTime appmt_start_time  = appmt.get_start_hour();
                    LocalDateTime appmt_start_time_check = result.get_start_hour();


                    LocalDateTime appmt_end_time = appmt.get_end_hour();
                    LocalDateTime appmt_end_time_check = result.get_end_hour();

                    boolean codes_match = appmt.get_customer_code() == result.get_customer_code();
                    boolean start_days_match = appmt_start_time.toLocalDate().equals(appmt_start_time_check.toLocalDate());
                    boolean end_days_match  = appmt_end_time.toLocalDate().equals(appmt_end_time_check.toLocalDate());

                    return codes_match && (start_days_match || end_days_match);
                }).anyMatch(appmt -> {

                    LocalDateTime appmt_start_datetime= appmt.get_start_hour();
                    LocalDateTime appmt_end_datetime=  appmt.get_end_hour();

                    boolean start_times_match= appmt_start_datetime.equals(result.get_start_hour());
                    boolean end_times_match  = appmt_end_datetime.equals(result.get_end_hour());

                    // there could be a conflict, with eg. only 15 minutes between the start of both appointments
                    // if it's a lot of time, then no big deal
                    boolean starts_could_overlap = appmt_start_datetime.isBefore(result.get_start_hour());

                    boolean ends_could_overlap = appmt_end_datetime.isBefore(result.get_end_hour());

                    return  (start_times_match || end_times_match || starts_could_overlap || ends_could_overlap);
                });
    }

    private void load_customers() {
        ObservableList<Customer> customers_observable_list = customer_object.get_every();

        customers_list.addAll(customers_observable_list);

    }


    private void load_appmts() {

        ObservableList<Appointment> temp_list = appointment_class.get_every();
//        System.out.println(temp_list);
//        System.err.println(temp_list);
//        ObservableList<Appointment> temp_list = FXCollections.observableArrayList(new Appointment(6,"t","d","loc","type", LocalDateTime.now(), LocalDateTime.now().plusMinutes(14), LocalDateTime.now(), "pers", LocalDateTime.now(),"last",4,"cu",9,"cont",90,"us"));

        appointments_list.addAll(temp_list);
    }
}