package com.example.futbolapp;

import android.provider.ContactsContract;

import com.example.futbolapp.gureKlaseak.League;
import com.example.futbolapp.gureKlaseak.Match;
import com.example.futbolapp.gureKlaseak.Proba;
import com.example.futbolapp.gureKlaseak.Standing;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DataAccess {

    private static Match[] partidoak;
    private static Match[] partidoakOrdenatuta;

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
                    partidoakOrdenatuta = ordenarPartidosPorFecha(partidoak);



                } catch (JSONException e) {
                    System.out.println("Response ok but JSON ERROR ");

                }

            }
        });
        return partidoakOrdenatuta;
    }

    public static List<Match> getMatchesFromJson(String urtea,String competi) throws IOException {



        String filePath ="/data/data/com.example.futbolapp/files/matches.json";
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(filePath));
        Match[] matches = gson.fromJson(reader, Match[].class);
        List<Match> matches2020 = Arrays.stream(matches)
                .filter(m -> m.getLeague().getSeason()==(Integer.parseInt(urtea)))
                .filter(m->m.getLeague().getName().equals(competi))
                .collect(Collectors.toList());

        return Arrays.asList(ordenarPartidosPorFecha(matches2020.toArray(new Match[0])));


    }
    public static List<Standing> getStandingsFromJson(String urtea,String competi) throws IOException {
        String filePath ="/data/data/com.example.futbolapp/files/proba.json";
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(filePath));
        Proba[] leagues = gson.fromJson(reader, Proba[].class);
        League league = Arrays.stream(leagues)
                .filter(m -> m.getLeague().getSeason() == Integer.parseInt(urtea))
                .filter(m->m.getLeague().getName().equals(competi))
                .findFirst()
                .map(Proba::getLeague)
                .orElse(null);

        List<Standing> st = new ArrayList<>();

        for (Standing[] standingArr : league.getStandings()) {
            if (standingArr != null && standingArr.length > 0) {
                for (Standing standing : standingArr) {
                    st.add(standing);
                }
            }
        }
        System.out.println("standings are "+st.toString());


        return st;





    }
    public static Match[] ordenarPartidosPorFecha(Match[] partidos) {
        // Crea un nuevo formato de fecha para analizar las cadenas de fecha
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        // Crea un nuevo comparador para ordenar los partidos de más reciente a más antiguo
        Comparator<Match> comparador = (partido1, partido2) -> {
            try {
                //Date fecha1 = formato.parse(partido1.getFixture().getDate());
                Date fecha2 = formato.parse(partido2.getFixture().getDate());
                int matchDay1 = Integer.parseInt(partido1.getLeague().getRound().split(" ")[3]);
                int matchDay2 = Integer.parseInt(partido2.getLeague().getRound().split(" ")[3]);
                if (matchDay1 > matchDay2) {
                    return 1;
                } else if (matchDay1 < matchDay2) {
                    return -1;
                } else {
                    return 0;
                }
            } catch (ParseException e) {
                // Manejar la excepción en caso de que la cadena no se pueda analizar
                e.printStackTrace();
            }
            return 0;
        };

        // Convierte el arreglo en una lista para poder ordenarlo
        List<Match> listaPartidos = Arrays.asList(partidos);

        // Ordena la lista de partidos utilizando el comparador personalizado
        Collections.sort(listaPartidos, comparador);

        // Convierte la lista ordenada de vuelta a un arreglo y devuélvela
        return listaPartidos.toArray(new Match[0]);
    }
}
