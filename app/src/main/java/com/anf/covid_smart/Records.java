package com.anf.covid_smart;

public class Records {
    String r_addr;
    String r_date;
    String r_time;

    public Records(String r_addr, String r_date, String r_time) {
        this.r_addr = r_addr;
        this.r_date = r_date;
        this.r_time = r_time;
    }

    public String getR_addr() {
        return r_addr;
    }

    public void setR_addr(String r_addr) {
        this.r_addr = r_addr;
    }

    public String getR_date() {
        return r_date;
    }

    public void setR_date(String r_date) {
        this.r_date = r_date;
    }

    public String getR_time() {
        return r_time;
    }

    public void setR_time(String r_time) {
        this.r_time = r_time;
    }
}
