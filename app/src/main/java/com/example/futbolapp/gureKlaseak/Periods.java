package com.example.futbolapp.gureKlaseak;

import io.realm.RealmObject;

public class Periods extends RealmObject {
    private Integer first;
    private Integer second;

    public Integer getFirst() {
        return first;
    }

    public Integer getSecond() {
        return second;
    }

    // Constructor, getters y setters
}
