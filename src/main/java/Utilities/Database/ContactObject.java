package Utilities.Database;

import Entities.Contact;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ContactObject implements DataObject<Contact> {

    private Contact show_contact(ResultSet results) throws SQLException {

        int contact_code = results.getInt("Contact_ID");
//        int contact_code = results.getInt("Contact_ID");  Contact UNDERSCORE ID
        String contact_name = results.getString("Contact_Name");
        String email = results.getString("Email");

        return new Contact(contact_code, contact_name, email);
    }

    @Override
    public Optional<Contact> fetch(int id) {
        Connection connection = MySQLConnector.open_sql_connection();
        try {
            Statement statement = connection.createStatement();
            ResultSet results =
                    statement.executeQuery(
                            "SELECT  * FROM contacts WHERE Contact_ID=" + id);

            if (results.next()) {
                return Optional.of(show_contact(results));
            }
        } catch (SQLException error) {
            error.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public ObservableList<Contact> get_every() {

        try (Connection connection = MySQLConnector.open_sql_connection()) {

            Statement statement = connection.createStatement();
//            ResultSet all_contacts = statement.executeQuery("SELECT * FROM contacts");

            ResultSet all_contacts = statement.executeQuery("SELECT * FROM client_schedule.contacts");

            ObservableList<Contact> contacts = FXCollections.observableArrayList();

            while (all_contacts.next()) {
                contacts.add(show_contact(all_contacts));
            }

            return contacts;

        } catch (SQLException error) {
            error.printStackTrace();
        }

        return FXCollections.observableArrayList();
    }

    // not ready, these 3
    @Override
    public boolean insert(Contact contact) {
        return false;
    }

    @Override
    public boolean update(Contact contact) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}