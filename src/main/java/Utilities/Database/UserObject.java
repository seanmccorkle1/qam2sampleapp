package Utilities.Database;

import Entities.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserObject implements DataObject<User>{

    private User fetch(ResultSet user_results) throws SQLException {
        return new User(user_results.getInt("User_ID"), user_results.getString("User_Name"));
    }

    // change
    @Override
    public boolean insert(User user) {
        return false;
    }

    // change
    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
    public Optional<User> getUserByUserNameAndPassword(String user, String pass) {

        try(Connection db_connect = MySQLConnector.open_sql_connection()){

            String col1="User_ID, ";
            String col2="User_Name ";

            // THIS IS THE TABLE
            String table = "users ";
            String cond1="User_Name=? ";
            String cond2 ="Password=?";

            PreparedStatement sql_statement = db_connect.prepareStatement(
                    "SELECT " + col1+col2+"FROM " + table+"WHERE "+ cond1+"AND "+ cond2);

            sql_statement.setString(1, user);
            sql_statement.setString(2, pass);

            ResultSet single_result = sql_statement.executeQuery();

            if(single_result.next()) {
                return Optional.of(fetch(single_result));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Optional<User> fetch(int id) {

        try(Connection db_connection = MySQLConnector.open_sql_connection()){

            Statement sql_statement =db_connection.createStatement();

            String col1="User_ID, ";
            String col2="User_Name ";
            String table = "users ";
            String cond = "id=";

            ResultSet user_result = sql_statement.executeQuery(
                    "SELECT " + col1+col2+"FROM "+table+"WHERE "+cond+id);

            if(user_result.next()) {
                return Optional.of(fetch(user_result));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public ObservableList<User> get_every() {
        try(Connection db_connection = MySQLConnector.open_sql_connection()){

            Statement sql_statement = db_connection.createStatement();

            String col1="User_ID, ";
            String col2="User_Name ";
            String table="users";

            ResultSet user_results = sql_statement.executeQuery(
                    "SELECT " + col1+col2+"FROM " + table);

            ObservableList<User> users = FXCollections.observableArrayList();

            while(user_results.next()) {
                User user = fetch(user_results);
                users.add(user);
            }

            return users;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.observableArrayList();
    }
}