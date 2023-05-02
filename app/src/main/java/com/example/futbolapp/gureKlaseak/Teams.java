package com.example.futbolapp.gureKlaseak;

import io.realm.RealmObject;

public class Teams extends RealmObject {
    private Home home;
    private Away away;

    public Home getHome() {
        return home;
    }

    public Away getAway() {
        return away;
    }

    // Constructor, getters y setters
}
