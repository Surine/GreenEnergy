package com.greenenergy.greenenergy.Bean;

/**
 * Created by surine on 2017/8/22.
 */

public class Can_Poi {
    private String uid;
    private String province;
    private String geotable_id;
    private String district;
    private String icon_style_id;
    private String city;
    private String[] location;
    private String address;
    private String title;
    private String coord_type;
    private String direction;
    private String type;
    private String distance;
    private String weight;
    private int is_full;

    public Can_Poi(String uid, String province, String geotable_id, String district, String icon_style_id, String city, String[] location, String address, String title, String coord_type, String direction, String type, String distance, String weight, int is_full) {
        this.uid = uid;
        this.province = province;
        this.geotable_id = geotable_id;
        this.district = district;
        this.icon_style_id = icon_style_id;
        this.city = city;
        this.location = location;
        this.address = address;
        this.title = title;
        this.coord_type = coord_type;
        this.direction = direction;
        this.type = type;
        this.distance = distance;
        this.weight = weight;
        this.is_full = is_full;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getIs_full() {
        return is_full;
    }

    public void setIs_full(int is_full) {
        this.is_full = is_full;
    }

    public String getGeotable_id() {
        return geotable_id;
    }

    public void setGeotable_id(String geotable_id) {
        this.geotable_id = geotable_id;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getIcon_style_id() {
        return icon_style_id;
    }

    public void setIcon_style_id(String icon_style_id) {
        this.icon_style_id = icon_style_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String[] getLocation() {
        return location;
    }

    public void setLocation(String[] location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoord_type() {
        return coord_type;
    }

    public void setCoord_type(String coord_type) {
        this.coord_type = coord_type;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
