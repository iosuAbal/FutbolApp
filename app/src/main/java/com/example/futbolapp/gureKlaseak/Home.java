package com.example.futbolapp.gureKlaseak;

import io.realm.RealmObject;

public class Home extends RealmObject {
    private int id;
    private String name;
    private String logo;
    private Boolean winner;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogo() {
        return logo;
    }

    public Boolean getWinner() {
        return winner;
    }

    // Constructor, getters y setters
}
