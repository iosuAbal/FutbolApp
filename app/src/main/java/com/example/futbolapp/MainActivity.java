package com.example.futbolapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;


import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.futbolapp.databinding.ActivityMainBinding;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_seasons, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public class MyAsyncTask extends AsyncTask<Void, Void, String> {
        private String pUrl;
        public MyAsyncTask(String url) {
            pUrl = url;
        }
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = null;
                try {
                    url = new URL(pUrl);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Authorization", "Bearer bba9aba98f4a4a979f0000dfb827ec55");
                conn.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                conn.disconnect();
                String emaitzaString = response.toString();

                // Parsear la respuesta JSON con GSON
                Gson gson = new Gson();
                MyObject myObject = gson.fromJson(emaitzaString, MyObject.class);


                return myObject.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            Gson gson = new Gson();/*
            MyObject myObject = gson.fromJson(result, MyObject.class);
            String homeTeamName = MyObject.matches(0);
            String awayTeamName = match.getAwayTeam().getName();
            String homeScore = String.valueOf(match.getScore().getFullTime().getHome());
            String awayScore = String.valueOf(match.getScore().getFullTime().getAway());

            String resultado = homeTeamName + " " + homeScore + " : " + awayScore + " " + awayTeamName;
*/
        }
    }


    public void getFromAPI(String pUrl) throws IOException {
        MyAsyncTask asyncTask = new MyAsyncTask(pUrl);
        asyncTask.execute();


    }

}