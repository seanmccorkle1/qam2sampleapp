package Entities;
import java.time.LocalDateTime;

// this is where 'Appointment' in eg:
// Appointment controller = loader.getController();
// comes from

public class Appointment {

    private int id;
    private String title;
    private String desc;
    private String location;
    private String type;

    //    private SimpleObjectProperty<LocalDateTime> start;
//    private SimpleObjectProperty<LocalDateTime> end;
    private LocalDateTime created_time;
    private LocalDateTime last_updated_time;

    private LocalDateTime start;
    private LocalDateTime end;

    private String created_by_person;
    private String last_updated_person;

    private int customer_id;
    private int contact_id;
    private int user_id;

    private String customer;
    private String contact;
    private String user;


    public Appointment() {

        this(-1, null, null, null, null,
                null, null, null, null,
                null,null,  -1,
                null, -1, null, -1, null);
    }

    //CHECK ORDERING
    public Appointment(int id, String title, String desc, String location,
                       String type, LocalDateTime start, LocalDateTime end,
                       LocalDateTime created_time, String created_by_person,LocalDateTime last_updated_time,
                       String last_updated_person, int customer_id, String customer, int contact_id, String contact,
                       int user_id, String user) {

        this.id = id;
        this.title = title;
        this.desc = desc;
        this.location = location;
        this.type = type;

//        this.start = start;
//        this.start = new SimpleObjectProperty<>(start);
//        this.end = new SimpleObjectProperty<>(end);

        this.start=start;
        this.end = end;

        this.created_time = created_time;
        this.created_by_person = created_by_person;

        this.last_updated_time = last_updated_time;
        this.last_updated_person = last_updated_person;

        this.customer_id = customer_id;
        this.customer = customer;

        this.contact_id = contact_id;
        this.contact = contact;

        this.user_id = user_id;
        this.user = user;
    }



    public LocalDateTime get_start_hour() {return start;}
    public LocalDateTime get_end_hour() {return end;}

    //    public LocalDateTime get_start_hour() {return start.get();}
//    public LocalDateTime get_end_hour() {return end.get();}
    public LocalDateTime get_creation_time() {return created_time;}
    public LocalDateTime get_most_recently_updated_time() {return last_updated_time;}

    public String get_appmt_title() {return title;}
    public String get_appmt_text() {return desc;}
    public String get_appmt_location() {return location;}
    public String get_appmt_type() {return type;}
    public String get_author_of_appointment() {return created_by_person;}

    public String get_most_recently_updated_person() {return last_updated_person;}
    public String get_customer_name() {return customer;}

    public String get_contact_name() {return contact;}
    // public String getContact() {return appmt_contact;}

    public String get_appmt_user() {return user;}
    public int get_appmt_id() {return id;}
    public int show_contact_id() {return contact_id;}
    public int get_user_id() {return user_id;}

    public int get_customer_code() {return customer_id;}
    // public int getCustomerID() {return appmt_customer_id;}

    public void set_customer_code(int customer_id) {this.customer_id = customer_id;}
    public void set_appmt_id(int id) {this.id = id;}
    public void set_contact_code(int contact_id) {this.contact_id = contact_id;}

    public void set_appmt_user_id(int user_id) {this.user_id = user_id;}

    public void set_appmt_title(String title) {this.title = title;}
    public void set_appmt_text(String desc) {this.desc=desc;}
    public void set_appmt_location(String location) {this.location=location;}
    public void set_appmt_type(String type) {this.type = type;}

    public void set_author_of_appointment(String created_by_person) {
        this.created_by_person = created_by_person;
    }

//    public void set_start_hour(LocalDateTime start) {this.start.set(start);}

    public void set_start_hour(LocalDateTime start) {this.start = start;}
    public void set_end_hour(LocalDateTime end) {this.end = end;}

//    public void set_end_hour(LocalDateTime end) {this.end.set(end);}

    public void set_creation_time(LocalDateTime created_time) {this.created_time = created_time;}

    public void set_most_recently_updated_time(LocalDateTime last_updated_time) {
        this.last_updated_time =  last_updated_time;
    }

    /**
     * Sets the MRU (most recently used) user, the most recent {@code appmt_user} who updated the appointment
     */
    public void set_most_recently_updated_person(String last_updated_person) {
        this.last_updated_person=last_updated_person;
    }

    public void set_customer_name(String customer) {
        this.customer = customer;
    }
    public void set_contact(String contact) {
        this.contact = contact;
    }
    public void set_appmt_user(String user) {this.user = user;}
}