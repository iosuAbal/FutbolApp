package com.example.futbolapp.gureKlaseak;

import io.realm.RealmObject;

public class ExtraTime  extends RealmObject {
    private Integer home;
    private Integer away;

    public Integer getHome() {
        return home;
    }

    public Integer getAway() {
        return away;
    }

    // Constructor, getters y setters
}
