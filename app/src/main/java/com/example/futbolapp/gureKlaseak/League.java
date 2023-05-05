package com.example.futbolapp.gureKlaseak;

import java.util.List;

import io.realm.RealmObject;

public class League {
    private int id;
    private String name;
    private String country;
    private String logo;
    private String flag;
    private int season;
    private String round;

    public Standing[][] getStandings() {
        return standings;
    }

    private Standing[][] standings;







    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogo() {
        return logo;
    }

    public String getFlag() {
        return flag;
    }

    public int getSeason() {
        return season;
    }

    public String getRound() {
        return round;
    }

    public String getCountry() {
        return country;
    }

}
