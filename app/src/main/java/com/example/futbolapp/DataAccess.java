package com.example.futbolapp;



import com.example.futbolapp.gureKlaseak.Match;
import com.example.futbolapp.gureKlaseak.StandingResponse;
import com.example.futbolapp.ui.home.ApiMap;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DataAccess {

    private static Match[] partidoakOrdenatuta;


    public static CompletableFuture<StandingResponse[]> getStandingsFromAPI(String league) {
        CompletableFuture<StandingResponse[]> future = new CompletableFuture<>();
        System.out.println("REQUEST EGINDA");
        String leagueId = String.valueOf(ApiMap.getValueByName(league));

        OkHttpClient client = new OkHttpClient();
        String url = "https://v3.football.api-sports.io/standings?league=" + leagueId + "&season=2022";

        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-rapidapi-key", "4d1bac668930e231121a2980d1a049e4")
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Error in call");
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                System.out.println("Call done " + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonArr = jsonObject.getJSONArray("response");
                    Gson gson = new Gson();
                    StandingResponse[] standings = gson.fromJson(jsonArr.toString(), StandingResponse[].class);

                    future.complete(standings);
                } catch (JSONException e) {
                    System.out.println("Response ok but JSON ERROR");
                    future.completeExceptionally(e);
                }
            }
        });

        return future;
    }
    public static CompletableFuture<Match[]> getMatchesFromAPI(String league) {
        CompletableFuture<Match[]> future = new CompletableFuture<>();

        String leagueId = String.valueOf(ApiMap.getValueByName(league));
        System.out.println("REQUEST EGINDA");
        OkHttpClient client = new OkHttpClient();
        String url = "https://v3.football.api-sports.io/fixtures?league=" + leagueId + "&season=2022";

        Request request = new Request.Builder()
                .url(url)
                .addHeader("X-rapidapi-key", "4d1bac668930e231121a2980d1a049e4")
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("Error in call");
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                System.out.println("Call done " + json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonArr = jsonObject.getJSONArray("response");
                    Gson gson = new Gson();
                    Match[] partidoak = gson.fromJson(jsonArr.toString(), Match[].class);
                    System.out.println("PARTIDOAK" + partidoak.toString());
                    partidoakOrdenatuta = ordenarPartidosPorFechaYJornada(partidoak);
                    future.complete(partidoakOrdenatuta);
                } catch (JSONException e) {
                    System.out.println("Response ok but JSON ERROR");
                    future.completeExceptionally(e);
                }
            }
        });

        return future;
    }

    public static List<Match> loadMatchesFromJSON() throws  ExecutionException, InterruptedException {
        Callable<List<Match>> callable = new Callable<List<Match>>() {
            @Override
            public List<Match> call() throws Exception {
                URL url = new URL("https://drive.google.com/uc?id=1cE0x9i4gx4HSMV5TTVNQCd3ILTM-LQdP&export=download");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();
                String jsonString = stringBuilder.toString();

                Gson gson = new Gson();

                Match[] matches = gson.fromJson(jsonString, Match[].class);

                return Arrays.asList(ordenarPartidosPorFechaYJornada(matches));
            }
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<Match>> future = executor.submit(callable);
        List<Match> result = future.get();

        executor.shutdown();

        return result;
    }



    public static List<StandingResponse> loadStandingsFromJSON() throws IOException, ExecutionException, InterruptedException {
        Callable<List<StandingResponse>> callable = new Callable<List<StandingResponse>>() {
            @Override
            public List<StandingResponse> call() throws Exception {
                URL url = new URL("https://drive.google.com/uc?id=1w1Oa1GDh5Ns7iVrzihxvTbCmJdY7RiMp&export=download");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();
                String jsonString = stringBuilder.toString();

                Gson gson = new Gson();

                StandingResponse[] allStandings = gson.fromJson(jsonString, StandingResponse[].class);


                return Arrays.asList(allStandings);
            }
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<StandingResponse>> future = executor.submit(callable);
        List<StandingResponse> result = future.get();

        executor.shutdown();

        return result;

    }

    public static Match[] ordenarPartidosPorFechaYJornada(Match[] partidos) {

        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Comparator<Match> comparador = (partido1, partido2) -> {
            try {
                Date fecha1 = formato.parse(partido1.getFixture().getDate());
                Date fecha2 = formato.parse(partido2.getFixture().getDate());
                int matchDay1 = Integer.parseInt(partido1.getLeague().getRound().split(" ")[3]);
                int matchDay2 = Integer.parseInt(partido2.getLeague().getRound().split(" ")[3]);
                if (matchDay1 > matchDay2) {
                    return 1;
                } else if (matchDay1 < matchDay2) {
                    return -1;
                } else {
                    return fecha1.compareTo(fecha2);
                }
            } catch (ParseException e) {

                e.printStackTrace();
            }
            return 0;
        };

        List<Match> listaPartidos = Arrays.asList(partidos);

        Collections.sort(listaPartidos, comparador);

        return listaPartidos.toArray(new Match[0]);
    }
}
