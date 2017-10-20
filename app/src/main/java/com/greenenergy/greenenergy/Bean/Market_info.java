package com.greenenergy.greenenergy.Bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by surine on 2017/9/14.
 */

public class Market_info extends DataSupport implements Serializable {
    private int id;
    private String good_id;
    private String title;
    private String info_url;
    private String picture_url;
    private double price;

    public Market_info() {
    }

    public Market_info(int id, String good_id, String title, String info_url, String picture_url, double price) {
        this.id = id;
        this.good_id = good_id;
        this.title = title;
        this.info_url = info_url;
        this.picture_url = picture_url;
        this.price = price;
    }

    public String getGood_id() {
        return good_id;
    }

    public void setGood_id(String good_id) {
        this.good_id = good_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo_url() {
        return info_url;
    }

    public void setInfo_url(String info_url) {
        this.info_url = info_url;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
