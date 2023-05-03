package com.example.futbolapp;

import com.example.futbolapp.gureKlaseak.Match;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DataAccess {

    public static List<Match> getMatchesFromJson(String urtea,String competi) throws IOException {




        String filePath ="/data/user/0/com.example.futbolapp/files/"+competi+".json";

        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(filePath));
        Match[] matches = gson.fromJson(reader, Match[].class);
        List<Match> matches2020 = Arrays.stream(matches)
                .filter(m -> m.getFixture().getDate().startsWith(urtea))
                .collect(Collectors.toList());

        return matches2020;


    }
}
