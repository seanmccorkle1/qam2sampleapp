package Entities;
import java.time.LocalDateTime;

public class Customer {

    public Customer() {customer_id = -1;}

    private int customer_id;
    private int division_code;
    private int country_code;

    private LocalDateTime time_created;
    private LocalDateTime time_updated;

    private String name;
    private String home_address;
    private String zip_code;
    private String phone_num;
    private String person_who_created;
    private String person_who_updated;
    private String country;
    private String division;

    public Customer(int customer_id, int division_code, int country_code, LocalDateTime time_created, LocalDateTime time_updated,
                    String name, String phone_num, String home_address, String zip_code, String division, String country,
                    String person_who_created, String person_who_updated) {

        this.country = country;
        this.country_code = country_code;
        this.time_created = time_created;
        this.person_who_created = person_who_created;
        this.division= division;
        this.division_code = division_code;
        this.customer_id = customer_id;
        this.time_updated = time_updated;
        this.person_who_updated = person_who_updated;
        this.name = name;
        this.phone_num = phone_num;
        this.zip_code = zip_code;
        this.home_address = home_address;
    }

    public void set_country_code(int country_code) {this.country_code = country_code;}
    public void set_division_code(int division_code) {this.division_code = division_code;}
    public void set_customer_id(int customer_id) {this.customer_id = customer_id;}

    public void set_created_time(LocalDateTime time_created) {this.time_created = time_created;}
    public void set_updated_time(LocalDateTime time_updated) {this.time_updated = time_updated;}

    public void set_customer_name(String name) {this.name = name;}
    public void set_phone(String phone_num) {this.phone_num = phone_num;}
    public void set_zip_code(String zip_code) {this.zip_code = zip_code;}
    public void set_address(String home_address) {this.home_address = home_address;}
    public void set_country(String country) {this.country = country;}
    public void set_created_person(String person_who_created) {this.person_who_created = person_who_created;}
    public void set_division(String division) {this.division = division;}
    public void set_updated_person(String person_who_updated) {this.person_who_updated = person_who_updated;}

    // GETTERS
    public LocalDateTime get_updated_time() {return time_updated;}
    public LocalDateTime get_created_time() {return time_created;}

    public int get_country_code() {return country_code;}
    public int get_division_code() {return division_code;}
    public int get_customer_id() {return customer_id;}

    public String get_address() {return home_address;}
    public String get_country() {return country;}
    public String get_division() {return division;}

    public String get_created_person() {return person_who_created;}
    public String get_updated_person() {return person_who_updated;}
    public String get_customer_name() {return name;}
    public String get_phone() {return phone_num;}
    public String get_zip_code() {return zip_code;}
}