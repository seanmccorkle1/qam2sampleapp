package Entities;

public class User {

    private int id;
    private String username;


    // defaults are nully
    public User() {
        this.id = -1;
        this.username = null;
    }

    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public void set_user_id(int id) {
        this.id = id;
    }

    public void set_user(String username) {
        this.username = username;
    }

    public int get_user_id() {
        return id;
    }
    public String get_user() {
        return username;
    }

}