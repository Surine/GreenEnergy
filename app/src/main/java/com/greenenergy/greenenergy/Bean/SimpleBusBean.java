package com.greenenergy.greenenergy.Bean;

/**
 * Created by surine on 2017/8/23.
 * id = 3
 */

public class SimpleBusBean {
    private int bus_id;
    private String bus_message;

    public SimpleBusBean(int bus_id, String bus_message) {
        this.bus_id = bus_id;
        this.bus_message = bus_message;
    }

    public int getBus_id() {
        return bus_id;
    }

    public void setBus_id(int bus_id) {
        this.bus_id = bus_id;
    }

    public String getBus_message() {
        return bus_message;
    }

    public void setBus_message(String bus_message) {
        this.bus_message = bus_message;
    }
}
