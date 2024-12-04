package Entities;

public class Division {

    private int id;
    private int country_code;
    private String subdivision_name;

    // each division has an an ID, as well as each country
    public Division(int id, String name, int country_code) {
        this.id = id;
        this.country_code = country_code;
        this.subdivision_name = name;
    }

    public String get_division() {return subdivision_name;}
    public int get_division_code() {return id;}
    public int get_country_code() {return country_code;}

    public void set_country_name(String country_name) {
        this.subdivision_name =subdivision_name;
    }

    // public void set_division(int id) {this.id = id;}

    // eg. US is 1 , china is 86,pakistan is 92
    public void set_division_code(int division_code) {this.id = division_code;}

}