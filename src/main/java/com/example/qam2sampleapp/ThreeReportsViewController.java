package com.example.qam2sampleapp;

import Entities.Appointment;
import Entities.Contact;
import Entities.Report;
import Entities.CustomReport;
import Utilities.Database.AppointmentObject;
import Utilities.Database.ContactObject;

import java.time.*;
import java.time.format.DateTimeFormatter;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

public class ThreeReportsViewController {

    /**
     * The main dropdown at the top, controls {@code main_table}
     * <br/>
     * Which is a TableView of @{code Appointmentsl}
     * <br/>
     * Lets you see what appointments the chosen contact has
     * */
    @FXML
    private ComboBox<Contact> contact_dropdown;

    /**
     * The biggest table on the ThreeReportsView.fxml screen
     */
    @FXML
    private TableView<Appointment> main_table;
    /***
     *
     */
    @FXML
    private TableColumn<Appointment, Integer> id_col;
    @FXML
    private TableColumn<Appointment, String> title_col;
    @FXML
    private TableColumn<Appointment, String> type_col;
    @FXML
    private TableColumn<Appointment, String> desc_col;
    @FXML
    private TableColumn<Appointment, LocalDateTime> start_date_col;
    @FXML
    private TableColumn<Appointment, LocalDateTime> end_date_col;
    @FXML
    private TableColumn<Appointment, Integer> customer_id_col;

    // Table for grouping by "month"
    @FXML
    private TableView<Report> first_table;

    // Table for grouping by "type" of appmt
    @FXML
    private TableView<Report> second_table;

    @FXML
    private TableColumn<Report, String> appmt_type_column;

    @FXML
    private TableColumn<Report, Month> month_col;

    // 16 appointments_list with the 'type' N/A in January is:
    // {N/A, Jan, 16}
    @FXML
    private TableColumn<Report, Integer> by_month_total;

    @FXML
    private TableColumn<Report, String> contact_name_col;
    @FXML
    private TableColumn<Report, Year> contact_year_col;
    @FXML
    private TableColumn<Report, Integer> by_year_total;

    @FXML
    private TableView<CustomReport> div_table;
    @FXML
    private TableColumn<CustomReport, String> div_col;
    @FXML
    private TableColumn<CustomReport, String> name_col;
    @FXML
    private TableColumn<CustomReport, Integer> by_div_total;

    private final static DateTimeFormatter PROPER_DATE = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a z");

    private final ObservableList<Contact> contacts_list = FXCollections.observableArrayList();
    private final ObservableList<Appointment> appointments_list = FXCollections.observableArrayList();

    private final ObservableList month_list = FXCollections.observableArrayList();
    private final ObservableList year_list = FXCollections.observableArrayList();
    private final ObservableList div_list= FXCollections.observableArrayList();

    private final AppointmentObject appointment_class = new AppointmentObject();
    private final FilteredList<Appointment> appointments_list_filtered = new FilteredList<>(appointments_list);

    @FXML
    void show_appointments_for_a_contact() {

        Contact chosen_contact = contact_dropdown.getSelectionModel().getSelectedItem();

        // LAMBDA
        // LAMBDA
        // LAMBDA
        if (chosen_contact != null) {
            appointments_list_filtered.setPredicate(appmt -> appmt.show_contact_id() == chosen_contact.get_contact_id());
        }
    }

    @FXML
    void initialize() {

        init_contacts();
        init_main_table();
        init_contact_dropdown();

        go_month_table();
        go_year_table();
        go_div_table();

        // 1st small table
        // Use 'varname' in this.varname, not the one in the method

        first_table.setItems(month_list);
//        appmt_type_column.setCellValueFactory(new PropertyValueFactory<>("type"));
        appmt_type_column.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue()!=null ? c.getValue().get_type() : "type1"));


        Month m = LocalDateTime.now().getMonth();

//        month_col.setCellValueFactory(new PropertyValueFactory<>("date"));
        month_col.setCellValueFactory(c-> {
            Report r =c.getValue();

            if (r == null) {
                return new SimpleObjectProperty<>(m);
            }

            try {
                Month month_of_the_appointment =Month.of((int) r.get_date());

                return new SimpleObjectProperty<>(month_of_the_appointment);
//                LocalDateTime date = Instant.ofEpochMilli(r.get_date()).atZone(ZoneId.systemDefault()).toLocalDateTime();

//                String monthName = date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
//                return new SimpleObjectProperty<>(monthName);
            } catch (Exception e) {
                System.err.println("Bad date: " + r.get_date());
                return new SimpleObjectProperty<>(m); // this is DECEMBER
            }
        });


//        by_month_total.setCellValueFactory(new PropertyValueFactory<>("total"));
        by_month_total.setCellValueFactory(c-> new SimpleObjectProperty<>(
                c.getValue() != null ? c.getValue().get_total_num_of_appmts() : 12));

        Year y = Year.of(LocalDateTime.now().getYear());

        /*
        * The second "report table"
        * <br/>
        * This one counts the total number of appointments that user x has in the next month and year
        * <br/>
        * The database only comes with 2 appointments though...
        * */
        second_table.setItems(year_list);

//        contact_name_col.setCellValueFactory(new PropertyValueFactory<>("type"));
        contact_name_col.setCellValueFactory(c-> new SimpleObjectProperty<>(
                c.getValue() != null ? c.getValue().get_type(): "chat"));

//        contact_year_col.setCellValueFactory(new PropertyValueFactory<>("date"));
//        contact_year_col.setCellValueFactory(
//                c-> new SimpleObjectProperty<>(c.getValue() != null ? Year.of(Integer.parseInt(c.getValue().get_date())) : null));


        contact_year_col.setCellValueFactory(c -> {
            Report report = c.getValue();

            if (report != null) {
                try {
                    return new SimpleObjectProperty<>(Year.of((int) report.get_date()));
                } catch (NumberFormatException e) {
                    System.err.println("no valid year  ==> " + report.get_date());
                }
            }
            return new SimpleObjectProperty<>(Year.of(LocalDateTime.now().getYear()));
        });

//        by_year_total.setCellValueFactory(new PropertyValueFactory<>("total"));
        by_year_total.setCellValueFactory(
                c-> new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_total_num_of_appmts() : 120));

        div_table.setItems(div_list);

        div_col.setCellValueFactory(c-> new SimpleObjectProperty<>(c.getValue()!= null ? c.getValue().get_state() :  "Guayaquil"));
        name_col.setCellValueFactory(c-> new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_person() : "Andres"));
        by_div_total.setCellValueFactory(c-> new SimpleObjectProperty<>(c.getValue()!= null ? c.getValue().get_total() : 1));
    }

    private void init_contacts() {
        ContactObject contact_obj = new ContactObject();
        contacts_list.addAll(contact_obj.get_every());
    }

    private void init_main_table() {

        appointments_list.addAll(appointment_class.get_every());
        main_table.setItems(appointments_list_filtered);

//        id_col.setCellValueFactory(new PropertyValueFactory<>("id"));
//        title_col.setCellValueFactory(new PropertyValueFactory<>("title"));
//        type_col.setCellValueFactory(new PropertyValueFactory<>("type"));
//        desc_col.setCellValueFactory(new PropertyValueFactory<>("desc"));

        id_col.setCellValueFactory(c-> new SimpleObjectProperty<>(c.getValue()!= null ? c.getValue().get_appmt_id() : 34));
        title_col.setCellValueFactory(c-> new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_appmt_title() : "thing"));
        type_col.setCellValueFactory(c-> new SimpleObjectProperty<>(c.getValue()!= null ? c.getValue().get_appmt_type() : "general"));
        desc_col.setCellValueFactory(c-> new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_appmt_text() : "text"));


//        start_date_col.setCellValueFactory(new PropertyValueFactory<>("start"));
        start_date_col.setCellValueFactory(
                c-> new SimpleObjectProperty<>(c.getValue()!= null ? c.getValue().get_start_hour() : LocalDateTime.now()));

        start_date_col.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Appointment, LocalDateTime> call(TableColumn<Appointment, LocalDateTime> ldt) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(LocalDateTime time, boolean is_empty) {
                        super.updateItem(time, is_empty);

//                        if ( is_empty||time==null) {
                        if ( is_empty ) {
                            setText(null);
                        }
                        else {
                            ZonedDateTime zone = time.atZone(ZoneId.systemDefault());
                            setText(zone.format(PROPER_DATE));
                        }
                    }
                };
            }
        });

//        end_date_col.setCellValueFactory(new PropertyValueFactory<>("end"));
        end_date_col.setCellValueFactory(
                c-> new SimpleObjectProperty<>(c.getValue()!= null ? c.getValue().get_end_hour() : LocalDateTime.now().plusMinutes(30)));

        end_date_col.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Appointment, LocalDateTime> call(TableColumn<Appointment, LocalDateTime> ldt) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(LocalDateTime time, boolean is_empty) {
                        super.updateItem(time, is_empty);

//                        if (is_empty || time==null) {
                        if (is_empty) {
                            setText(null);
                        }
                        else {
                            ZonedDateTime zone = time.atZone(ZoneId.systemDefault());
                            setText(zone.format(PROPER_DATE));
                        }
                    }
                };
            }
        });
//        customer_id_col.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
//        contact_id_col.setCellValueFactory(new PropertyValueFactory<>("contact_id"));

        customer_id_col.setCellValueFactory(
                c-> new SimpleObjectProperty<>(c.getValue() != null ? c.getValue().get_customer_code() : 22));
    }

    private void init_contact_dropdown() {
        contact_dropdown.setItems(contacts_list);
        contact_dropdown.setCellFactory(new Callback<>() {

            @Override
            public ListCell<Contact> call(ListView<Contact> lc) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Contact cont, boolean is_empty) {
                        super.updateItem(cont, is_empty);

                        if (is_empty || cont==null) {
                            setText(null);
                        }
                        else {
                            String name=cont.get_contact_name();
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
                if (is_empty || cont == null) {
                    setText(null);
                }
                else {
                    String name = cont.get_contact_name();
                    setText(name);
                }
            }
        });
//        contact_dropdown.getSelectionModel().selectFirst();
        contact_dropdown.getSelectionModel().selectLast();
        show_appointments_for_a_contact();
    }

    /**
     * A table that can group by:
     * <br/>
     * 1. month, eg. december 2019 or december 2020
     * <br/>
     * 2.The contact, whoever was selected as the 'contact' of the appointment.
     * <br/>
     * "Contact_Name" is one of the columns in the database thingy
     * <br/>
     * Implemented by just doing {@code GROUP BY month, total} in the sql statement
     * <br/>
     * It's one of the things in the rubric
     * */
    private void go_month_table() {
        month_list.addAll(appointment_class.group_by_month());
    }

    /**
     * custom table thingy, in the rubric
     * <br/>
     * I made it count the amount of people (the amount of customers) that are from each subdivision,
     * <br/>
     * eg. 2 from virginia, 1 from london
     */
    private void go_div_table() {
        div_list.addAll(appointment_class.group_by_country());
    }

    /**
     * The custom table that can group by  year of the appointment
     * <br/>
     * I think 'year' is a better one than month anyway, month can be december 1966 or december 2024
     * <br/>
     * Also one of the things in the rubric
     * */
    private void go_year_table() {
        year_list.addAll(appointment_class.group_by_year());
    }
}