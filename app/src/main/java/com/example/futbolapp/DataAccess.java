package com.example.futbolapp;

import com.example.futbolapp.gureKlaseak.Match;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DataAccess {

    private static Match[] partidoak;

    public static Match [] getMatchesFromAPI(){

        OkHttpClient client = new OkHttpClient();
        String url = "https://v3.football.api-sports.io/fixtures?league=140&season=2022";

        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-rapidapi-key", "4d1bac668930e231121a2980d1a049e4")
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Error in call");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json=response.body().string();
                System.out.println("Call done "+json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonArr=jsonObject.getJSONArray("response");
                    Gson gson = new Gson();
                    partidoak = gson.fromJson(jsonArr.toString(), Match[].class);
                    System.out.println("PARTIDOAK"+partidoak.toString());




                } catch (JSONException e) {
                    System.out.println("Response ok but JSON ERROR ");

                }

            }
        });
        return partidoak;
    }

    public static List<Match> getMatchesFromJson(String urtea,String competi) throws IOException {



        String filePath ="/data/data/com.example.futbolapp/files/"+competi+".json";
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(filePath));
        Match[] matches = gson.fromJson(reader, Match[].class);
        List<Match> matches2020 = Arrays.stream(matches)
                .filter(m -> m.getFixture().getDate().startsWith(urtea))
                .collect(Collectors.toList());

        return matches2020;


    }
}
