package Entities;

public class Report {
    long date;
    String type;
    int total;

    public Report(long date, String type, int total) {
        this.date = date;
        this.type = type;
        this.total = total;
    }

    public String get_type() {return type;}
    public long get_date() {return date;}
    public int get_total_num_of_appmts() {return total;}

    public void set_type(String type) {this.type = type;}
    public void set_date(long date) {this.date = date;}
    public void set_total_num_of_appmts(int total) {this.total = total;}
}