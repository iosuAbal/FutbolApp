package com.example.futbolapp.gureKlaseak;

import io.realm.RealmObject;

public class Goals extends RealmObject  {
    private int home;
    private int away;

    public int getHome() {
        return home;
    }

    public int getAway() {
        return away;
    }

    // Constructor, getters y setters
}
