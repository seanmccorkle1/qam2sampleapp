package Utilities.Database;

import Entities.Division;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import Entities.Report;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class DivisionObject implements DataObject<Division>{

    @Override
    public boolean insert(Division division) {
        return false;
    }

    @Override
    public boolean update(Division division) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    private Division show_division(ResultSet div_results) throws SQLException {

        int division_code= div_results.getInt("Division_ID");
        String division_name = div_results.getString("Division");
        int country_code = div_results.getInt("Country_ID");

        return new Division(division_code, division_name, country_code);
    }

    @Override
    public Optional<Division> fetch(int id) {

        try (Connection connect = MySQLConnector.open_sql_connection()) {

            Statement my_statement = connect.createStatement();

            String col1="Division_ID, ";
            String col2="Division, ";
            String col3="Country_ID, ";
            String col4="Country ";

            String table =  "first_level_divisions ";
            String join =  "countries.Country_ID=first_level_divisions.Country_ID ";
            String cond =  "Division_ID=";

            ResultSet sql_results = my_statement.executeQuery("SELECT " + col1+col2+col3+col4+"FROM " +table
                    +"JOIN countries ON " + join+ "WHERE "+cond+ id);

            if(sql_results.next()) {
                return Optional.of(show_division(sql_results));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public ObservableList<Division> get_every() {

        try (Connection connect = MySQLConnector.open_sql_connection()){

            Statement my_sql_statement = connect.createStatement();

            String col1 = "first_level_divisions.*, ";
            String col2="countries.Country ";
            String table ="first_level_divisions ";
            String cond = "countries.Country_ID=first_level_divisions.Country_ID";

            ResultSet sql_results = my_sql_statement.executeQuery("SELECT " + col1+col2+"FROM " + table +
                    "JOIN countries ON " + cond);

            System.out.println(sql_results);

            ObservableList<Division> divisions_list = FXCollections.observableArrayList();
            while(sql_results.next()) {
                Division div = show_division(sql_results);
                divisions_list.add(div);
            }

            // a List of <Divisions>
            return divisions_list;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableArrayList();
    }
}