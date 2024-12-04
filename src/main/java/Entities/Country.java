package Entities;

public class Country {

    private int id;
    private String country_name;

    public Country() {
        this.id = -1;
        this.country_name = null;
    }

    //    public Country(int id, String country_name) {
    public Country(int id, String country_name) {
        this.id = id;
        this.country_name = country_name;
    }

    public int get_country_code() {return id;}
    public String get_country_name() {return country_name;}

    public void set_country_name(String country_name) {
        this.country_name = country_name;
    }
    public void set_iso_code(int id) {this.id = id;}
}