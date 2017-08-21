package com.greenenergy.greenenergy.Bean;

/**
 * Created by surine on 2017/8/20.
 */

public class ScoreInfo {
    private String Score;
    private String Energy;
    private String Other;

    public ScoreInfo(String score, String energy, String other) {
        Score = score;
        Energy = energy;
        Other = other;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public String getEnergy() {
        return Energy;
    }

    public void setEnergy(String energy) {
        Energy = energy;
    }

    public String getOther() {
        return Other;
    }

    public void setOther(String other) {
        Other = other;
    }
}
