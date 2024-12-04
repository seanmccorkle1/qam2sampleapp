package Entities;

public class CustomReport {
    String state;
    String name;
    int total;
    public CustomReport(String state, String name, int total) {
        this.state = state;
        this.name=name;
        this.total = total;
    }

    public String get_state() {return state;}
    public String get_person() {return name;}
    public int get_total() {return total;}

    public void set_state(String state) {this.state =state;}
    public void set_person(String name) {this.name = name;}
    public void set_total (int total) {this.total = total;}

}