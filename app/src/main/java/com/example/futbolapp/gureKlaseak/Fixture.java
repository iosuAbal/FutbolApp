package com.example.futbolapp.gureKlaseak;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Fixture{
    @PrimaryKey
    private int id;
    private String referee;
    private String timezone;
    private String date;
    private int timestamp;
    private Periods periods;
    private Venue venue;
    private Status status;

    public int getId() {
        return id;
    }

    public String getReferee() {
        return referee;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getDate() {
        return date;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public Periods getPeriods() {
        return periods;
    }

    public Venue getVenue() {
        return venue;
    }

    public Status getStatus() {
        return status;
    }

    // Constructor, getters y setters
}
