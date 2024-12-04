package com.example.qam2sampleapp;

import Entities.Appointment;
import Entities.Contact;
import Entities.Customer;
import Utilities.Database.ContactObject;
import Utilities.Database.CustomerObject;
import Utilities.FifteenMinAlert;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * {@code AppointmentPaneController} class
 * <p>Controls the appointmentpane.fxml file.
 * <br/>
 * * Uses {@code getText()} fn to retrieve text input from the user in a textfield
 * <br/>
 * * And then eg.{@code set_appmt_title()} setter to store that input into the object like {@code this.title=title}
 * </p>
 * <br/>
 * An example:
 * <br/>
 *
 * <pre>
 *      new Appointment(title: "business meeting", location:"Virginia", start: 2020-05-28);
 *      etc
 * </pre>
 */
public class AppointmentPaneController {

    /** Appointment obj, start it off with null*/
    private Appointment appointment = null;

    private Consumer<Appointment> processor;

    /**
     * List of customers used in {@code customer_dropdown}
     * */
    private final ObservableList<Customer> customers = FXCollections.observableArrayList();
    private final ObservableList<Contact> contacts = FXCollections.observableArrayList();

    private final ObservableList<LocalTime> start_hour_list = FXCollections.observableArrayList();

    private final ObservableList<LocalTime> end_hour_list = FXCollections.observableArrayList();

    /**{@code DateTimeFormatter} instance
     * <br/>
     * This gives 10:52 PM for example
     **/
    private final DateTimeFormatter time_formatting = DateTimeFormatter.ofPattern("h:mm a");

    /**Title like 'routine meeting'*/
    @FXML
    private TextField title_of_appointment;

    /**address like: 1420 something dr.*/
    @FXML
    private TextField location_of_appointment;

    /**The type, 'supplier meeting'*/
    @FXML
    private TextField type_of_appointment;

    /**
     * Description of the appointment
     * <br/>
     * {@code TextArea} cause its longer
     * <br/>
     * example: 'routine meeting for x' */
    @FXML
    private TextArea appointment_description;

    /**It looks like the database comes with 3 contacts */
    @FXML
    private ComboBox<Contact> contact_dropdown;

    /**
     * {@code customer_dropdown}
     * <br/>
     * <p>It loads the 3 customers that come in the {@code client_schedule} database</p>
     * <br/>
     */
    @FXML
    private ComboBox<Customer> customer_dropdown;

    /**{@code DatePicker},
     * selects the day, like 23 nov 2024, but not hours
     */
    @FXML
    private DatePicker start_calendar;

    //same
    @FXML
    private DatePicker end_calendar;

    //*Picks a certain moment during the day {@code 8:15 pm}*/
    @FXML
    private ComboBox<LocalTime> start_dropdown;

    //*Same thing*/
    @FXML
    private ComboBox<LocalTime> end_dropdown;

    /**Green submit button*/
    @FXML
    private Button submit_button;

    @FXML
    private Button cancel_button;

    /**
     * {@code initialize()} fn
     * <br/>
     * <p>It just calls other init functions</p>
     */
    @FXML
    void initialize() {
        initialize_calendars();
        initialize_contacts_and_customers();
        initialize_start_time();
        initialize_end_time();
    }

    /**
     * Initialize the two {@code DatePicker}s used for choosing the day of the appointment
     * <br/>
     * * Make them different if the current day is a weekend, because it's closed then
     */
    private void initialize_calendars() {
        start_calendar.setValue(find_curr_day());
        start_calendar.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate curr_date, boolean empty) {
                boolean is_sat = curr_date.getDayOfWeek() == DayOfWeek.SATURDAY;
                boolean is_sun = curr_date.getDayOfWeek() == DayOfWeek.SUNDAY;

                super.updateItem(curr_date, empty);

                if (empty || is_sat || is_sun){
                    setDisable(true);
                }
            }
        });

        start_calendar.setEditable(false);

        // appointments should last just one day
        end_calendar.setValue(find_curr_day());

        end_calendar.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate curr_date, boolean empty) {
                super.updateItem(curr_date, empty);

                boolean is_sat = curr_date.getDayOfWeek() == DayOfWeek.SATURDAY;
                boolean is_sun = curr_date.getDayOfWeek() == DayOfWeek.SUNDAY;

                if (empty || is_sat || is_sun) {
                    setDisable(true);
                }
            }
        });
        end_calendar.setEditable(false);
    }

    /**
     * Populate {@code contact_dropdown} and {@code customer_dropdown} with data
     * */
    private void initialize_contacts_and_customers() {

        //ComboBox.setItems()
        contact_dropdown.setItems(contacts);
        contact_dropdown.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Contact> call(ListView<Contact> lv) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Contact cont, boolean is_empty) {
                        super.updateItem(cont, is_empty);

                        if (cont==null || is_empty){
                            setText(null);
                        }
                        else {
                            String name = cont.get_contact_name();
                            setText(name);
                        }
                    }
                };
            }
        });

        contact_dropdown.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Contact cont, boolean is_empty) {
                super.updateItem(cont, is_empty);

                if (cont==null || is_empty) {
                    setText(null);
                }
                else {
                    setText(cont.get_contact_name());
                }
            }
        });

        customer_dropdown.setItems(customers);
        customer_dropdown.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Customer> call(ListView<Customer> lv) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Customer cust, boolean is_empty) {
                        super.updateItem(cust, is_empty);

                        if (cust==null || is_empty) {
                            setText(null);
                        }
                        else {
                            setText(cust.get_customer_name());
                        }
                    }
                };
            }
        });

        customer_dropdown.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Customer cust, boolean is_empty) {
                super.updateItem(cust, is_empty);

                if (cust==null || is_empty) {
                    setText(null);
                }
                else {
                    setText(cust.get_customer_name());
                }
            }
        });
    }

    /**
     * @return LocalDate, a day like 2024-12-03 aka 3 December 2024
     * <br/>
     * But adjust for this: <b>"business hours defined as 8 AM to 10 PM EST"</b>
     * <br/>
     * So if it's 11 pm on friday, the first available day should be monday morning, aka 2.5 or 3 days
     */
    private LocalDate find_curr_day() {

        LocalDate day  = LocalDate.now();
        DayOfWeek day_name = day.getDayOfWeek();

        LocalTime ten_pm = LocalTime.of(22, 0);
        LocalTime now = LocalTime.now();

        if (now.isAfter(ten_pm)) {

            if (day_name == DayOfWeek.FRIDAY){
                return day.plus(3, ChronoUnit.DAYS);
            }

            if (day_name == DayOfWeek.SATURDAY){
                return day.plus(2, ChronoUnit.DAYS);
            }

            if (day_name == DayOfWeek.SUNDAY){
                return day.plus(1, ChronoUnit.DAYS);
            }
        }
        // Go to the next day
        return day;
    }

    /**
     * Start the beginning times in {@code start_dropdown} loser to the current time in the day
     * <br/>
     * * If it's already 5 pm, start it near 5:00pm then 7:00am */
    private void initialize_start_time() {
        start_dropdown.setItems(start_hour_list);
        start_dropdown.setCellFactory(new Callback<>() {
            @Override
            public ListCell<LocalTime> call(ListView<LocalTime> lv) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(LocalTime time, boolean is_empty) {
                        super.updateItem(time, is_empty);

                        if (time == null) {
                            setText(null);
                        }
                        else {
                            setText(time.format(time_formatting));
                        }

                    }
                };
            }
        });

        start_dropdown.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(LocalTime time, boolean is_empty) {
                super.updateItem(time, is_empty);
                if (time == null || is_empty){
                    setText(null);
                }
                else setText(time.format(time_formatting));
            }
        });
    }

    /** Same idea, just +15 or +30 minutes*/
    private void initialize_end_time() {
        end_dropdown.setItems(end_hour_list);
        end_dropdown.setCellFactory(new Callback<>() {
            @Override
            public ListCell<LocalTime> call(ListView<LocalTime> time_list) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(LocalTime time, boolean is_empty) {
                        super.updateItem(time, is_empty);

                        if (time==null) {
                            setText(null);
                        }
                        else {
                            setText(time.format(time_formatting));
                        }

                    }
                };
            }
        });
        end_dropdown.setButtonCell(new ListCell<>() {

            @Override
            protected void updateItem(LocalTime time, boolean is_empty) {

                super.updateItem(time, is_empty);

                if (time==null || is_empty) {
                    setText(null);
                }
                else {
                    setText(time.format(time_formatting));
                }
            }
        });
    }

    /**
     * @param appointment The appointment to init
     * @param customers The customers that correspond with the appmt
     * @param processor oncomplete processor
     * @return nothing
     *<br/>
     * <p>Runs after an appointment is added, or when {@code add_appointment} is called
     * </p>
     */
    public void initialize_appointment(Appointment appointment, ObservableList<Customer> customers, Consumer<Appointment> processor) {

        this.processor = processor;
        this.customers.addAll(customers);

//        load_all_customers();

        load_all_contacts();
        load_times();
        fix_appmt_times();
        load_appmt(appointment);
    }

    /**calls  {@code Contact.get_every()} fn to load the 3 contacts that come pre-stored in the db
     */
    private void load_all_contacts() {

        ContactObject my_contacts = new ContactObject();
        contacts.addAll(my_contacts.get_every());
    }

    /**Calls  {@code Customer.get_every()} fn to load the customers that come
     *<br/>
     * Same idea as above
     */
    private void load_all_customers() {

        CustomerObject my_customers = new CustomerObject();
        ObservableList<Customer> list_of_customers= my_customers.get_every();
        customers.addAll(list_of_customers);
    }

    /** This one uses 15 min intervals and puts those intervals into the ComboBox {@code start_dropdown} and {@code end_dropdown}
     * <br/>
     * 4 intervals for every hour.
     * <br/>
     * So it shows eg.
     * <ul>
     *     <li> 8:00 am</li>
     *     <li> 8:15 am</li>
     *     <li> 8:30 am</li>
     * </ul>
     * <br/>
     * in the boxes in {@code AppointmentPane.fxml}
     * */
    private void load_times() {

        int[] hour_steps = { 0, 15, 30, 45 };

        // i=8 j=0,1,2,3
        // i=9  j=0,1,2,3

        LocalDate today = LocalDate.now();

        // 8 AM to 10 PM
        for (int idx= 8; idx < 23; idx++) {

            int hour=idx;

            for (int minutes_idx = 0; minutes_idx < hour_steps.length; minutes_idx++) {

                int minutes = hour_steps[minutes_idx];
                LocalTime now = LocalTime.of(hour, minutes);

                LocalDateTime ldt = LocalDateTime.of(today, now);
                ZonedDateTime zdt = ldt.atZone(ZoneId.of("America/New_York"));

                // use the user's time zone
                ZonedDateTime zoned_time = zdt.withZoneSameInstant(ZoneId.systemDefault());
                LocalTime final_time = zoned_time.toLocalTime(); // leave out the zone part

                start_hour_list.add(final_time);
                end_hour_list.add(final_time);
            }
        }

        LocalDateTime closing_time= LocalDateTime.of(today, LocalTime.of(22,0));

        ZonedDateTime closing_time_with_zone = closing_time.atZone(ZoneId.of("America/New_York"));
        ZonedDateTime point_in_time_corresponding_with_close = closing_time_with_zone.withZoneSameInstant(ZoneId.systemDefault());

        end_hour_list.add(point_in_time_corresponding_with_close.toLocalTime());
    }

    /**Make the defualt-selected time (in the dropdown/ComboBox) closer to the user's local time
     * Don't start it at 7am unless it is already 7am
     * */
    private void fix_appmt_times() {

        LocalTime right_now = LocalTime.now();

        start_dropdown.getSelectionModel().select(0);
        end_dropdown.getSelectionModel().select(2);    // +30 minutes from above

        for (LocalTime interval_of_15 : start_hour_list) {

            // making sure the event lies in the future

            if (right_now.isBefore(interval_of_15)) {

                // start_hour_list.interval_of_15

                int plus_0_idx= start_hour_list.indexOf(interval_of_15);
                int plus_15_idx = end_hour_list.indexOf(interval_of_15.plusMinutes(15));

                start_dropdown.getSelectionModel().select(plus_0_idx);
                end_dropdown.getSelectionModel().select(plus_15_idx);

                // start_dropdown.getSelectionModel().select(1);
                // end_dropdown.getSelectionModel().select(1);

                // select a good time  once and and break
                break;
            }
        }
    }

    /**loads the appointment with setter functions defined in {@code Appointment.java}*/
    private void load_appmt(Appointment appmt) {

        if (appmt == null) {
            return;
        }

        String title =appmt.get_appmt_title();
        title_of_appointment.setText(title);

        String desc =appmt.get_appmt_text();
        appointment_description.setText(desc);

        String loc  =appmt.get_appmt_location();
        location_of_appointment.setText(loc);
        // location_of_appointment.setText(appmt.get_appmt_text());

        String form= appmt.get_appmt_type();
        type_of_appointment.setText(form);

        Optional<Customer> matching_customer = customers.stream().filter(cust -> {
            int cust_id  = cust.get_customer_id();
            int matching_cust_id =appmt.get_customer_code();
            return cust_id == matching_cust_id;
        }).findAny();

        Optional<Contact> matching_contact = contacts.stream().filter(cont-> {

            int cont_id_A = cont.get_contact_id();
            int cont_id_B  = appmt .show_contact_id();

            return cont_id_A ==cont_id_B;
        }).findAny();

        customer_dropdown.getSelectionModel().select(matching_customer.orElse(null));
        contact_dropdown.getSelectionModel().select(matching_contact.orElse(null));

        LocalDateTime appmt_start_datetime = appmt.get_start_hour();
        LocalDateTime appmt_end_datetime = appmt.get_end_hour();

        start_calendar.setValue(appmt_start_datetime.toLocalDate());
        start_dropdown.getSelectionModel().select(appmt_start_datetime.toLocalTime());

        end_calendar.setValue(appmt_end_datetime.toLocalDate());
        end_dropdown.getSelectionModel().select(appmt_end_datetime.toLocalTime());
    }

    /**
     * for the 'cancel' or 'quit' button
     */
    @FXML
    void cancel() {
        if (FifteenMinAlert.confirmation(FifteenMinAlert.ConfirmType.CANCEL)) {
            Stage stage = (Stage) cancel_button.getScene().getWindow();
            stage.close();
        }
    }


    /**for the 'submit' button on {@code AppointmentPane.fxml}
     * runs with onClick
     */

    @FXML
    void submit_appointment() {
        processor.accept(get_appointment());
        Stage stage = (Stage) submit_button.getScene().getWindow();
        stage.close();
    }

    /**
     * This runs all the setters in {@code Appointment.java}
     * <br/>
     * To store the info, given from the user's input
     * */
    private Appointment get_appointment() {

        // if it's a new apptmt
        if (appointment == null) {
            appointment = new Appointment();
        }

        appointment.set_appmt_title(title_of_appointment.getText());
        appointment.set_appmt_text(appointment_description.getText());
        appointment.set_appmt_location(location_of_appointment.getText());
        appointment.set_appmt_type(type_of_appointment.getText());

        LocalDate day_of_appmt = start_calendar.getValue();
        LocalDate day_of_appmt_end = end_calendar.getValue();

        LocalTime time_of_appmt=start_dropdown.getSelectionModel().getSelectedItem();
        LocalTime time_of_appmt_end=end_dropdown.getSelectionModel().getSelectedItem();

        LocalDateTime start_of_appmt = LocalDateTime.of(day_of_appmt, time_of_appmt);
        LocalDateTime end_of_appmt = LocalDateTime.of(day_of_appmt_end,  time_of_appmt_end);

        appointment.set_start_hour(start_of_appmt);
        appointment.set_end_hour(end_of_appmt);

        Contact host = contact_dropdown.getSelectionModel().getSelectedItem();

        appointment.set_contact_code(host.get_contact_id());
        appointment.set_contact(host.get_contact_name());

        // Get customer stuff
        Customer guest  =customer_dropdown.getSelectionModel().getSelectedItem();
        appointment.set_customer_code(guest.get_customer_id());
        appointment.set_customer_name(guest.get_customer_name());

        LocalDateTime now_moment = LocalDateTime.now();
        LocalDateTime create_time = appointment.get_creation_time();

        if (create_time == null) {
            appointment.set_creation_time(now_moment);
        }

        // this one updates either way
        appointment.set_most_recently_updated_time(now_moment);

        return appointment;
    }

}