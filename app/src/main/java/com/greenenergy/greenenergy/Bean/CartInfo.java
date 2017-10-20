package com.greenenergy.greenenergy.Bean;

import org.litepal.crud.DataSupport;

/**
 * Created by surine on 2017/9/28.
 */

public class CartInfo extends DataSupport{
    private int id;
    private boolean isChecked;
    private String good_id;

    public CartInfo() {
    }

    public CartInfo(int id, boolean isChecked, String good_id) {
        this.id = id;
        this.isChecked = isChecked;
        this.good_id = good_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public String getGood_id() {
        return good_id;
    }

    public void setGood_id(String good_id) {
        this.good_id = good_id;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
