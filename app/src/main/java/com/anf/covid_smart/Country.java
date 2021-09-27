package com.anf.covid_smart;

public class Country {
    String c_name;
    String c_new;
    String c_active;

    public Country(String c_name, String c_new, String c_active) {
        this.c_name = c_name;
        this.c_new = c_new;
        this.c_active = c_active;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getC_new() {
        return c_new;
    }

    public void setC_new(String c_new) {
        this.c_new = c_new;
    }

    public String getC_active() {
        return c_active;
    }

    public void setC_active(String c_active) {
        this.c_active = c_active;
    }
}
