package Entities;

public class Contact {

    private int id;
    private String contact_name;
    private String email;

    public Contact() {
        this.email = null;
        this.id = -1;
        this.contact_name = null;
    }

    // new Contact(2, 'sean', seanspy7@gmail.com)
    public Contact(int id, String name, String email) {
        this.id = id;
        this.contact_name = name;
        this.email = email;
    }

    // setters
    public void set_contact_name(String name) {this.contact_name = name;}
    public void set_contact_id(int id) {this.id = id;}
    public void set_contact_email(String email) {this.email = email;}

    // getters
    public int get_contact_id() {return id;}
    public String get_contact_name() {return contact_name;}
    public String get_contact_email() {return email;}
}