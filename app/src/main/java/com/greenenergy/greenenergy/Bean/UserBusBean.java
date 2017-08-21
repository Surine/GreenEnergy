package com.greenenergy.greenenergy.Bean;

/**
 * Created by surine on 2017/8/20.
 * bus_id = 1
 */

public class UserBusBean {
    private int bus_id;
    private UserInfo mUserInfo;

    public UserBusBean(int bus_id, UserInfo userInfo) {
        this.bus_id = bus_id;
        mUserInfo = userInfo;
    }

    public int getBus_id() {
        return bus_id;
    }

    public void setBus_id(int bus_id) {
        this.bus_id = bus_id;
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
    }
}
