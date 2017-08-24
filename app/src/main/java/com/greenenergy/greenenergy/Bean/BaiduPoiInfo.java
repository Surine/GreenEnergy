package com.greenenergy.greenenergy.Bean;

import java.util.List;

/**
 * Created by surine on 2017/8/22.
 */

public class BaiduPoiInfo {
    private String status;
    private String total;
    private String size;
    private List<Can_Poi> contents;

    public BaiduPoiInfo(String status, String total, String size, List<Can_Poi> contents) {
        this.status = status;
        this.total = total;
        this.size = size;
        this.contents = contents;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public List<Can_Poi> getContents() {
        return contents;
    }

    public void setContents(List<Can_Poi> contents) {
        this.contents = contents;
    }
}
