package com.example.qam2sampleapp;

import Entities.Country;
import Entities.Customer;
import Entities.Division;
import Utilities.Database.CountryObject;
import Utilities.Database.DivisionObject;
import Utilities.FifteenMinAlert;
import java.time.LocalDateTime;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

public class CustomerViewController {

    private Customer customer = null;

    private Consumer<Customer> onComplete;

    private final ObservableList<Country> countries_list = FXCollections.observableArrayList();

    private final ObservableList<Division> divisions_list = FXCollections.observableArrayList();

    @FXML
    private Text customer_text;

    @FXML
    private Label customer_label;

    @FXML
    private TextField customer_id;
    @FXML
    private TextField customer_name;
    @FXML
    private TextField customer_address;

    @FXML
    private TextField phone_num;
    @FXML
    private TextField zip_code;

    @FXML
    private ComboBox<Country> country_box;

    @FXML
    private ComboBox<Division> first_level_division_box;

    @FXML
    private Button submit_button;

    @FXML
    private Button cancel_button;

    @FXML
    void add_a_customer() {
        if (customer == null) {
            customer = new Customer();
        }
        LocalDateTime now_moment = LocalDateTime.now();

        String cust_address_text = customer_address.getText();
        String cust_name_text = customer_name.getText();
        String cust_zip = zip_code.getText();
        String cust_phone = phone_num.getText();
        LocalDateTime customer_create_time= customer.get_created_time();


        customer.set_customer_name(cust_name_text);
        customer.set_address(cust_address_text);
        customer.set_zip_code(cust_zip);
        customer.set_phone(cust_phone);

        Division div = first_level_division_box.getValue();
        Country cntry = country_box.getValue();

        int div_code = div.get_division_code();
        int cntry_code = cntry.get_country_code();

        String div_name = div.get_division();
        String cntry_name  = cntry.get_country_name();

        customer.set_division_code(div_code);
        customer.set_division(div_name);

        customer.set_country_code(cntry_code);
        customer.set_country(cntry_name);

        if (customer_create_time == null) {
            customer.set_created_time(now_moment);
        }

        customer.set_updated_time(now_moment);

        onComplete.accept(customer);

        Stage stage = (Stage) submit_button.getScene().getWindow();
        stage.close();
    }

    @FXML
    void cancel() {
        if (FifteenMinAlert.confirmation(FifteenMinAlert.ConfirmType.CANCEL)) {
            Stage stage = (Stage) cancel_button.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    void initialize() {

        first_level_division_box.setItems(divisions_list);

        first_level_division_box.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Division> call(ListView<Division> lv) {

                return new ListCell<>() {
                    @Override
                    protected void updateItem(Division subdivision, boolean is_empty) {
                        super.updateItem(subdivision, is_empty);

                        if (subdivision == null || is_empty) {
                            setText(null);
                        } else {
                            setText(subdivision.get_division());
                        }
                    }
                };
            }
        });

        first_level_division_box.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Division subdivision, boolean is_empty) {
                super.updateItem(subdivision, is_empty);
                if (subdivision != null && !is_empty) {
                    setText(subdivision.get_division());
                } else {
                    setText(null);
                }
            }
        });

        country_box.setItems(countries_list);

        country_box.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Country> call(ListView<Country> lv) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Country state, boolean is_empty) {
                        super.updateItem(state, is_empty);

                        if (state != null && !is_empty) {
                            setText(state.get_country_name());
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });

        country_box.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Country state, boolean is_empty) {
                super.updateItem(state, is_empty);

                if (state != null && !is_empty) {
                    String state_name = state.get_country_name();
                    setText(state_name);
                }
                else setText(null);
            }
        });

        country_box.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                int country_code = country_box.getSelectionModel().getSelectedItem().get_country_code();

                ObservableList<Division> divisions_of_a_country_list = FXCollections.observableArrayList();

                for (Division division : divisions_list) {

                    int country_code_of_division = division.get_country_code();

                    if (country_code_of_division == country_code) {
                        divisions_of_a_country_list.add(division);
                    }
                }

                first_level_division_box.setItems(divisions_of_a_country_list);
            }
        });

    }

    public void initialize_customer_data(Customer customer, Consumer<Customer> onComplete) {
        this.customer = customer;
        this.onComplete = onComplete;

        load_countries_list();
        load_subdivisions();
        load_customer_info();
    }

    private void load_countries_list() {
        CountryObject country_obj = new CountryObject();
        ObservableList<Country> all_countries = country_obj.get_every();
        all_countries.addAll(all_countries);
    }

    // Loads subdivisions
    private void load_subdivisions() {
        DivisionObject division_class = new DivisionObject();
        ObservableList<Division> list_of_subdivisions = division_class.get_every();
        divisions_list.addAll(list_of_subdivisions);
    }

    private void load_customer_info() {

        if (customer != null) {

            String cust_id = String.valueOf(customer.get_customer_id());
            String cust_name = customer.get_customer_name();
            String cust_phone = customer.get_phone();
            String cust_address= customer.get_address();
            String cust_zip_code= customer.get_zip_code();

            customer_id.setText(cust_id);
            customer_name.setText(cust_name);
            phone_num.setText(cust_phone);
            customer_address.setText(cust_address);
            zip_code.setText(cust_zip_code);

            for (Division subdivision : divisions_list) {

                int subdivision_code_A = subdivision.get_division_code();
                int subdivision_code_B =  customer.get_division_code();

                if (subdivision_code_A != subdivision_code_B){
                    continue; // then skip it
                }

                first_level_division_box.setValue(subdivision);

                for (Country country : countries_list) {
                    int country_code = country.get_country_code();

                    if (country_code != subdivision.get_country_code()){
                        continue;
                    }
                    country_box.setValue(country);
                    // break;
                }
            }
        }
    }
}