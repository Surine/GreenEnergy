package com.greenenergy.greenenergy.Bean;

/**
 * Created by surine on 2017/8/25.
 */

public class MessageBean {
    private int ID;
    private String Title;
    private String Content;
    private String PictureUrl;

    public MessageBean(int ID, String title, String content, String pictureUrl) {
        this.ID = ID;
        Title = title;
        Content = content;
        PictureUrl = pictureUrl;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getPictureUrl() {
        return PictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        PictureUrl = pictureUrl;
    }
}
