package com.example.futbolapp;

import java.util.List;

public class MyObject {
    public Filters filters;
    public Match[] matches;

    public static class Filters {
        public String season;
        public String competitions;
        public String permission;
        public int limit;
    }

    public static class Match {
        public Area area;
        public Team awayTeam;
        public Competition competition;
        public String group;
        public Team homeTeam;
        public int id;
        public String lastUpdated;
        public int matchday;
        public Odds odds;
        public List<Referee> referees;
        public Score score;
        public String winner;
        public Season season;
        public String stage;
        public String status;
        public String utcDate;
    }

    public static class Area {
        public int id;
        public String name;
        public String code;
        public String flag;
    }

    public static class Team {
        public int id;
        public String name;
        public String shortName;
        public String tla;
        public String crest;
    }

    public static class Competition {
        public int id;
        public String name;
        public String code;
        public String type;
        public String emblem;
    }

    public static class Odds {
        public String msg;
    }

    public static class Referee {
    }

    public static class Score {
        public String duration;
        public FullTime fullTime;
        public HalfTime halfTime;
        public String winner;
    }

    public static class FullTime {
        public int home;
        public int away;
    }

    public static class HalfTime {
        public Integer home;
        public Integer away;
    }

    public static class Season {
        public int id;
        public String startDate;
        public String endDate;
        public Integer currentMatchday;
        public String winner;
    }
}
