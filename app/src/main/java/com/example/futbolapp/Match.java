package com.example.futbolapp;

public class Match {
    private Fixture fixture;
    private League league;
    private Teams teams;
    private Goals goals;
    private Score score;

    public Fixture getFixture() {
        return fixture;
    }

    public void setFixture(Fixture fixture) {
        this.fixture = fixture;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }

    public Teams getTeams() {
        return teams;
    }

    public void setTeams(Teams teams) {
        this.teams = teams;
    }

    public Goals getGoals() {
        return goals;
    }

    public void setGoals(Goals goals) {
        this.goals = goals;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    // Constructor, getters y setters
}

 class Fixture {
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

 class Periods {
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

 class Venue {
    private Integer id;
    private String name;
    private String city;

    // Constructor, getters y setters
}

 class Status {
    private String longStatus;
    private String shortStatus;
    private Integer elapsed;

    // Constructor, getters y setters
}

 class League {
    private int id;
    private String name;
    private String country;
    private String logo;
    private String flag;
    private int season;
    private String round;

    // Constructor, getters y setters
}

 class Teams {
    private Home home;
    private Away away;

    // Constructor, getters y setters
}

 class Home {
    private int id;
    private String name;
    private String logo;
    private Boolean winner;

    // Constructor, getters y setters
}

 class Away {
    private int id;
    private String name;
    private String logo;
    private Boolean winner;

    // Constructor, getters y setters
}

 class Goals {
    private int home;
    private int away;

    // Constructor, getters y setters
}

 class Score {
    private HalfTime halftime;
    private FullTime fulltime;
    private ExtraTime extratime;
    private Penalty penalty;

    // Constructor, getters y setters
}

 class HalfTime {
    private int home;
    private int away;

    // Constructor, getters y setters
}

class FullTime {
    private int home;
    private int away;

    // Constructor, getters y setters
}

 class ExtraTime {
    private Integer home;
    private Integer away;

    // Constructor, getters y setters
}

 class Penalty {
    private Integer home;
    private Integer away;

    // Constructor, getters y setters
}
