package com.example.futbolapp;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DataAccess {

    public static List<Match> getMatchesFromJson() throws IOException {
        String filePath ="./laliga";
        String json = null;

        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(filePath));
        Match[] matches = gson.fromJson(reader, Match[].class);
        List<Match> matches2020 = Arrays.stream(matches)
                .filter(m -> m.getFixture().getDate().startsWith("2020-01"))
                .collect(Collectors.toList());

        return matches2020;


    }
}
