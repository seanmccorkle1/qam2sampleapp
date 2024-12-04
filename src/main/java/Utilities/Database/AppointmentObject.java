package Utilities.Database;

import Entities.Appointment;
import Entities.CustomReport;
import Entities.Customer;
import Entities.Report;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// don't change column labels
public class AppointmentObject implements DataObject<Appointment>{

    private static Appointment show_appointment(ResultSet results) throws SQLException {

        // in this order:
        // Appointment_ID
        // Title
        // Description

        int appmt_id = results.getInt("Appointment_ID");
        int cust_id  = results.getInt("Customer_ID");
        int user_id  = results.getInt("User_ID");
        int cont_id  = results.getInt("Contact_ID");

        String appmt_title= results.getString("Title");
        String appmt_desc= results.getString("Description");
        String appmt_place=  results.getString("Location");
        String appmt_type = results.getString("Type");
        String cust =  results.getString("Customer_Name");
        String cont =  results.getString("Contact_Name");
        String user = results.getString("User_Name");

        String appmt_created_person = results.getString("Created_By");
        String appmt_updated_person = results.getString("Last_Updated_By");

        LocalDateTime  appmt_creation_time =
                results.getTimestamp("Create_Date").toLocalDateTime();
        LocalDateTime  appmt_start_time =
                results.getTimestamp("Start").toLocalDateTime();
        LocalDateTime  appmt_end_time  =
                results.getTimestamp("End").toLocalDateTime();

        LocalDateTime  appmt_last_updated_time   =
                results.getTimestamp("Last_Update").toLocalDateTime();

        return new Appointment(appmt_id, appmt_title,appmt_desc,appmt_place,appmt_type, appmt_start_time,appmt_end_time,appmt_creation_time,appmt_created_person, appmt_last_updated_time,appmt_updated_person,cust_id, cust, cont_id, cont, user_id,user);

    }

    @Override
    public Optional<Appointment> fetch(int id) {

        try (Connection db_connect = MySQLConnector.open_sql_connection()){

            Statement statement = db_connect.createStatement();

            String col1= "appointments.*, ";
            String col2= "User_Name, ";
            String col3="Contact_Name, ";
            String col4= "Customer_Name ";

            String table1= "client_schedule.appointments, ";
            String table2= "client_schedule.customers, ";
            String table3="client_schedule.contacts, ";
            String table4= "client_schedule.users ";

            String cond1= "customers.Customer_ID=appointments.Customer_ID ";
            String cond2= "contacts.Contact_ID=appointments.Contact_ID ";
            String cond3= "users.User_ID=appointments.User_ID ";
            String cond4= "appointments.Appointment_ID=";
            // String cond4= "appointments.Appointments_ID=";

            ResultSet results = statement.executeQuery(
                    "SELECT " + col1 +col2+col3+col4 +
                            "FROM "+table1+table2+table3+table4+
                            "WHERE " +cond1+"AND "+ cond2+"AND " + cond3+
                            "AND "+cond4+id);

            if (results.next()) {
                return Optional.of(show_appointment(results));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public ObservableList<Appointment> get_every() {

        try (Connection db_connect = MySQLConnector.open_sql_connection()){

            Statement statement = db_connect.createStatement();

            String col1 = "appointments.*, ";
            String col2 = "User_Name, ";
            String col3= "Contact_Name, ";
            String col4= "Customer_Name ";

            String table1 ="client_schedule.appointments, ";
            String table2 = "client_schedule.customers, ";
            String table3= "client_schedule.contacts, ";
            String table4="client_schedule.users ";

            String cond1= "customers.Customer_ID=appointments.Customer_ID ";
            String cond2= "contacts.Contact_ID=appointments.Contact_ID ";
            String cond3= "users.User_ID=appointments.User_ID";

            ResultSet results = statement.executeQuery("SELECT " +col1+col2+col3+col4+"FROM "+table1+table2+table3+table4+"WHERE " +cond1+ "AND "+cond2+"AND "+cond3);

            ObservableList<Appointment> appointments =
                    FXCollections.observableArrayList();

            while (results.next()) {
                appointments.add(show_appointment(results));
            }

            return appointments;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableArrayList();
    }

    @Override
    public boolean insert(Appointment appmt) {

        try (Connection db_connect = MySQLConnector.open_sql_connection()){

            PreparedStatement prepared_sql_statement =
                    db_connect.prepareStatement(
                            "INSERT INTO client_schedule.appointments VALUE(NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                            Statement.RETURN_GENERATED_KEYS);

            String my_title = appmt.get_appmt_title();
            String my_desc =appmt.get_appmt_text();
            String my_place =appmt.get_appmt_location();
            String my_type =appmt.get_appmt_type();

            Timestamp my_start_time= Timestamp.valueOf(appmt.get_start_hour());
            Timestamp my_end_time= Timestamp.valueOf(appmt.get_end_hour());
            Timestamp my_creation_time= Timestamp.valueOf(appmt.get_creation_time());
            String my_created_by = appmt.get_author_of_appointment();
            Timestamp my_updated_time= Timestamp.valueOf(appmt.get_most_recently_updated_time());

            String my_updated_person = appmt.get_most_recently_updated_person();

            // String.valueOf(8) is "8"
            String my_cust_id = String.valueOf(appmt.get_customer_code());
            String my_user_id = String.valueOf(appmt.get_user_id());
            String my_contact_id  = String.valueOf(appmt.show_contact_id());

            prepared_sql_statement.setString(1, my_title);
            prepared_sql_statement.setString(2, my_desc);
            prepared_sql_statement.setString(3, my_place);
            prepared_sql_statement.setString(4, my_type);

            prepared_sql_statement.setTimestamp(5, my_start_time);
            prepared_sql_statement.setTimestamp(6, my_end_time);
            prepared_sql_statement.setTimestamp(7, my_creation_time);

            prepared_sql_statement.setString(8, my_created_by);

            prepared_sql_statement.setTimestamp(9, my_updated_time);

            prepared_sql_statement.setString(10, my_updated_person);
            prepared_sql_statement.setString(11, my_cust_id);
            prepared_sql_statement.setString(12, my_user_id);
            prepared_sql_statement.setString(13, my_contact_id);

            int flag = prepared_sql_statement.executeUpdate();
            ResultSet query_result = prepared_sql_statement.getGeneratedKeys();

//            if (flag == 1 & query_result.next()){
            if (flag == 1 && query_result.next()){
                appmt.set_appmt_id(query_result.getInt(1));
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    @Override
    public boolean update(Appointment appmt){

        try (Connection db_connect = MySQLConnector.open_sql_connection()) {
            String title_col = "Title=?, ";
            String desc_col = "Description=?, ";
            String loc_col= "Location=?, ";
            String type_col= "Type=?, ";
            String start_col= "Start=?, ";
            String end_col =  "End=?, ";
            String created_col= "Create_Date=?, ";
            String created_by_col= "Created_By=?, ";
            String last_updated_col= "Last_Update=?, ";
            String last_updated_by_col= "Last_Updated_By=?, ";
            String cust_id_col= "Customer_ID=?, ";
            String user_id_col=  "User_ID=?, "; // either above cont_id or below
            String cont_id_col= "Contact_ID=? ";
            String cond =  "Appointment_ID=?";

            PreparedStatement update_statement  =db_connect.prepareStatement("UPDATE client_schedule.appointments SET " + title_col+desc_col+loc_col+type_col+start_col+end_col+created_col+created_by_col+last_updated_col+last_updated_by_col+cust_id_col+user_id_col+cont_id_col+"WHERE " + cond );

            String my_title = appmt.get_appmt_title();
            String my_desc =appmt.get_appmt_text();
            String my_place =appmt.get_appmt_location();
            String my_type =appmt.get_appmt_type();

            Timestamp my_start_time= Timestamp .valueOf(appmt.get_start_hour());
            Timestamp my_end_time= Timestamp.valueOf(appmt.get_end_hour());
            Timestamp my_creation_time= Timestamp.valueOf(appmt.get_creation_time());

            String my_created_by = appmt.get_author_of_appointment();
            Timestamp my_updated_time= Timestamp.valueOf(appmt.get_most_recently_updated_time());

            String my_updated_person = appmt.get_most_recently_updated_person();

            // String.valueOf(8) is "8"
            String my_cust_id = String.valueOf(appmt.get_customer_code());
            String my_contact_id = String.valueOf(appmt.show_contact_id());
            String my_user_id = String.valueOf(appmt.get_user_id());
            String my_appmt_id  = String.valueOf(appmt.get_appmt_id());


            update_statement.setString(1, my_title);
            update_statement.setString(2, my_desc);
            update_statement.setString(3, my_place);
            update_statement.setString(4, my_type);

            update_statement.setTimestamp(5, my_start_time);
            update_statement.setTimestamp(6, my_end_time);
            update_statement.setTimestamp(7, my_creation_time);

            update_statement.setString(8, my_created_by);

            update_statement.setTimestamp(9, my_updated_time);

            update_statement.setString(10, my_updated_person);
            update_statement.setString(11, my_cust_id);
            update_statement.setString(12, my_user_id); // CHECK
            update_statement.setString(13, my_contact_id);
            update_statement.setString(14, my_appmt_id);

            int result = update_statement.executeUpdate();

            // if delete went through
            if (result == 1) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(int id) {

        try (Connection db_connect = MySQLConnector.open_sql_connection()) {

            Statement sql_statement = db_connect.createStatement();

            String table="client_schedule.appointments ";
            String cond="Appointment_ID=";
            // String cond="Appointments_ID=";

            int flag = sql_statement.executeUpdate("DELETE FROM "+ table + "WHERE "+cond+ id);

            if (flag == 1) {
                return true;      // successful delete
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean delete_all_appointments_of_a_customer(int id_of_cust) {

        try (Connection db_connect = MySQLConnector.open_sql_connection()) {

            Statement sql_statement = db_connect.createStatement();

            String table = "client_schedule.appointments ";
            String condition= "Customer_ID=";

            int amt_deleted = sql_statement.executeUpdate("DELETE FROM " + table + "WHERE " +condition +id_of_cust);

            if (amt_deleted > 0) {
                return true;
            }

        }
        catch(SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList group_by_month() {

        try (Connection db_connect = MySQLConnector.open_sql_connection()) {

//            System.out.println("first_mini_table() try");

            Statement sql_statement = db_connect.createStatement();

            String col1,col2,col3, from, group_by;

            col1 = "month(start) ";
            col2="type, ";
            col3= "count(*) ";

            from = "client_schedule.appointments ";
            group_by = "month";

            ResultSet query_results = sql_statement.executeQuery("SELECT "+col1+ "AS month, " +
                    col2+col3+"AS total FROM " +from +  "GROUP BY month");

            ArrayList<Report> first_report_list = new ArrayList<>();

            System.out.println(query_results);
            while (query_results.next()) {


                long date = query_results.getLong("month");
                String type =  query_results.getString("type");
                int tot=  query_results.getInt("total");

                System.out.println("MONTH:  " + date);
                System.out.println(type);
                System.out.println(tot);

                Report current_row = new Report(date,type,tot);
                first_report_list.add(current_row);
            }

            return first_report_list;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }



    public ArrayList group_by_year() {

        try (Connection db_connect = MySQLConnector.open_sql_connection()) {

            Statement statement = db_connect.createStatement();


            // Sum the # of appmts. made for each contact
            String select,select2,select3;

            select ="SELECT year(start) AS year, ";
            select2 = "contacts.Contact_Name AS contact, ";
            select3 = "COUNT(*) AS total ";
            String table="appointments ";

            String cond="appointments.Contact_ID=contacts.Contact_ID ";

            ResultSet results = statement.executeQuery(
                    select+select2+select3+ "FROM "+table+
                            "JOIN contacts ON " + cond+ "GROUP BY year");

            ArrayList<Report> second_report_list = new ArrayList<>();

            while (results.next()) {
                long yr_1=  results.getLong("year");
                String cont_2=  results.getString("contact");
                int tot_3=  results.getInt("total");

                Report row = new Report(yr_1, cont_2, tot_3);

                second_report_list.add(row);
            }

            return second_report_list;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public ArrayList group_by_country(){
        try (Connection db_connect = MySQLConnector.open_sql_connection()) {
            Statement sql_statement = db_connect.createStatement();

            ResultSet results = sql_statement.executeQuery(
                    "SELECT first_level_divisions.Division AS state, " +
                            "Customer_Name, COUNT(*) AS total FROM customers JOIN first_level_divisions ON " +
                            "first_level_divisions.Division_ID=customers.Division_ID GROUP BY state");
            ArrayList<CustomReport> custom_report_list = new ArrayList<>();

            while (results.next()) {
                String state =results.getString("state");
                String name=  results.getString("Customer_Name");
                int amt =  results.getInt("total");

                custom_report_list.add(new CustomReport(state, name, amt));
            }
            return custom_report_list;
        }
        catch (SQLException error){
            error.printStackTrace();
        }
        return new ArrayList<>();
    }
}