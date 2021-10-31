package com.anf.covid_smart;

public class Booking {
    String b_name;
    String b_nric;
    String b_date;
    String b_time;

    public Booking(String b_name, String b_nric, String b_date, String b_time) {
        this.b_name = b_name;
        this.b_nric = b_nric;
        this.b_date = b_date;
        this.b_time = b_time;
    }

    public String getB_name() {
        return b_name;
    }

    public String getB_nric() {
        return b_nric;
    }

    public String getB_date() {
        return b_date;
    }

    public String getB_time() {
        return b_time;
    }

    public void setB_name(String b_name) {
        this.b_name = b_name;
    }

    public void setB_nric(String b_nric) {
        this.b_nric = b_nric;
    }

    public void setB_date(String b_date) {
        this.b_date = b_date;
    }

    public void setB_time(String b_time) {
        this.b_time = b_time;
    }
}
