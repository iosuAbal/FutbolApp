package com.example.futbolapp.gureKlaseak;

import io.realm.RealmObject;

public class Score extends RealmObject {
    private HalfTime halftime;
    private FullTime fulltime;
    private ExtraTime extratime;
    private Penalty penalty;

    public HalfTime getHalftime() {
        return halftime;
    }

    public FullTime getFulltime() {
        return fulltime;
    }

    public ExtraTime getExtratime() {
        return extratime;
    }

    public Penalty getPenalty() {
        return penalty;
    }

    // Constructor, getters y setters
}
