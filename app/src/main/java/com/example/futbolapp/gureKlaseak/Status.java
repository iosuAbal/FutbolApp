package com.example.futbolapp.gureKlaseak;

import io.realm.RealmObject;

public class Status extends RealmObject {
    private String longStatus;
    private String shortStatus;
    private Integer elapsed;

    public String getLongStatus() {
        return longStatus;
    }

    public String getShortStatus() {
        return shortStatus;
    }

    public Integer getElapsed() {
        return elapsed;
    }

    // Constructor, getters y setters
}
