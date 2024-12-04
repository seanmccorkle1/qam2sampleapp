package Utilities.Database;

import Entities.Customer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class CustomerObject implements DataObject<Customer> {


    private Customer show_customer(ResultSet results) throws SQLException {

        String cntry = results.getString("Country");
        int cntry_id = results.getInt("Country_ID");

        LocalDateTime created_time = results.getTimestamp("Create_Date").toLocalDateTime();
        LocalDateTime last_update_time = results.getTimestamp("Last_Update").toLocalDateTime();

        String person_who_created_it = results.getString("Created_By");
        String div_name = results.getString("Division");

        int div_code = results.getInt("Division_ID");
        int customer_id = results.getInt("Customer_ID");

        String person_who_updated_it = results.getString("Last_Updated_By");
        String person_name = results.getString("Customer_Name");
        String phone_num = results.getString("Phone");

        String zip_code = results.getString("Postal_Code");
        String address = results.getString("Address");

        // return new Customer(cntry, cntry_id, created_time, person_who_created_it, div_name, div_code, customer_id,
        //         last_update_time, person_who_updated_it, person_name, phone_num, zip_code, address);

        // public Customer(int customer_id, int division_code, int country_code, LocalDateTime time_created, LocalDateTime time_updated, String name, String phone_num, String home_address, String zip_code, String division_name, String country_name, String person_who_created, String person_who_updated) {

//        return new Customer(customer_id, person_name, address, zip_code,phone_num,
//                created_time, person_who_created_it,
//                last_update_time,person_who_updated_it, div_code);

        return new Customer(
                customer_id, div_code, cntry_id, created_time, last_update_time, person_name, phone_num, address,
                zip_code, div_name, cntry, person_who_created_it,person_who_updated_it);

        // return new Customer(
        // results.getInt("Customer_ID"),
        // results.getString("Customer_Name"),
        // results.getString("Address"),
        // results.getString("Postal_Code"),
        // results.getString("Phone"),
        // results.getTimestamp("Create_Date").toLocalDateTime(),
        // results.getString("Created_By"),
        // results.getTimestamp("Last_Update").toLocalDateTime(),
        // results.getString("Last_Updated_By"),
        // results.getInt("Division_ID"),
        // results.getString("Division"),
        // results.getInt("Country_ID"),
        // results.getString("Country")
        // );
    }



    // Gets one customer from the DB
    @Override
    public Optional<Customer> fetch(int id) {
        try (Connection connection = MySQLConnector.open_sql_connection()) {
            Statement statement = connection.createStatement();

            String cols= "customers.*, countries.Country, first_level_divisions.Country_ID, first_level_divisions.Division ";
            String tables=  "customers, countries, first_level_divisions ";

            String cond1= "first_level_divisions.Division_ID=customers.Division_ID ";
            String cond2 = "first_level_divisions.Country_ID=countries.Country_ID ";
            String cond3="customer.Customer_ID=";

            ResultSet results = statement.executeQuery(
                    "SELECT " + cols  + "FROM "  +tables+"WHERE "+ cond1 +"AND "+cond2+"AND "+cond3 + id);
            if (results.next()) {
                return Optional.of(show_customer(results));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public ObservableList<Customer> get_every() {
        try (Connection connection = MySQLConnector.open_sql_connection()) {
            Statement statement = connection.createStatement();
            //COUNTRY_ID?
            ResultSet results = statement.executeQuery("SELECT customers.*, first_level_divisions.Division, first_level_divisions.Country_ID, countries.Country FROM customers, first_level_divisions, countries WHERE customers.Division_ID=first_level_divisions.Division_ID AND first_level_divisions.Country_ID=countries.Country_ID");
            ObservableList<Customer> array_of_customers = FXCollections.observableArrayList();

            while (results.next()) {
                Customer customer = show_customer(results);
                array_of_customers.add(customer);
            }

            return array_of_customers;

        } catch (SQLException err) {
            err.printStackTrace();
        }

        return FXCollections.observableArrayList();
    }

    @Override
    public boolean insert (Customer cust) {
        try (Connection connection = MySQLConnector.open_sql_connection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO customers VALUE (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, cust.get_customer_name());
            statement.setString(2, cust.get_address());
            statement.setString(3, cust.get_zip_code());
            statement.setString(4, cust.get_phone());
            statement.setTimestamp(5, Timestamp.valueOf(cust.get_created_time()));
            statement.setString(6, cust.get_created_person());
            statement.setTimestamp(7, Timestamp.valueOf(cust.get_updated_time()));
            statement.setString(8, cust.get_updated_person());
            statement.setInt(9, cust.get_division_code());

            int result = statement.executeUpdate();
            ResultSet results = statement.getGeneratedKeys();

//            if (result == 1 & results.next()) {
            if (result == 1 && results.next()) {
                cust.set_customer_id(results.getInt(1));
                return true;
            }

        } catch (SQLException err) {
            err.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean update(Customer cust) {

        try (Connection connection = MySQLConnector.open_sql_connection()) {

            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE customers SET Customer_Name=?, Address=?, Division_ID=?, Postal_Code=?, Phone=?, Create_Date=?, Created_By=?, Last_Update=?, Last_Updated_By=? WHERE Customer_ID=?");
            statement.setString(1, cust.get_customer_name());
            statement.setString(2, cust.get_address());

            statement.setInt(3, cust.get_division_code());
            statement.setString(4, cust.get_zip_code());
            statement.setString(5, cust.get_phone());

            statement.setTimestamp(6, Timestamp.valueOf(cust.get_created_time()));

            statement.setString(7, cust.get_created_person());

            statement.setTimestamp(8, Timestamp.valueOf(cust.get_updated_time()));

            statement.setString(9, cust.get_updated_person());
            statement.setInt(10, cust.get_customer_id());

            int result = statement.executeUpdate();

            if (result == 1)
                return true;

        } catch (SQLException err) {
            err.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(int id) {

        try (Connection connection = MySQLConnector.open_sql_connection()) {
            Statement statement = connection.createStatement();

            int result = statement.executeUpdate("DELETE FROM customers WHERE Customer_ID=" + id);

            if (result == 1)
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}