package com.greenenergy.greenenergy.Bean;

/**
 * Created by surine on 2017/8/20.
 */

public class UserInfo {
    private String ID;
    private String UserName;
    private String PhoneNum;
    private String Category;
    private ScoreInfo Score;

    public UserInfo(String ID, String userName, String phoneNum, String category, ScoreInfo score) {
        this.ID = ID;
        UserName = userName;
        PhoneNum = phoneNum;
        Category = category;
        Score = score;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPhoneNum() {
        return PhoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        PhoneNum = phoneNum;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public ScoreInfo getScore() {
        return Score;
    }

    public void setScore(ScoreInfo score) {
        Score = score;
    }
}
