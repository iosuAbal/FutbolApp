package com.example.futbolapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.futbolapp.databinding.ActivityMainBinding;
import com.example.futbolapp.gureKlaseak.Match;
import com.example.futbolapp.gureKlaseak.Proba;
import com.example.futbolapp.gureKlaseak.Standing;
import com.google.android.material.navigation.NavigationView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private static int previousMatchDay = -1;


    private static List<Proba> externalStandingsLaLiga;
    private static List<Proba> externalStandingsPremier;

    private static List<Match> allJSONMatches;
    private static List<Match> partidoakLaLiga;


    private static List<Match>  partidoakPremier;

    public static List<Match>  getPartidoakLaLiga() {
        return partidoakLaLiga;
    }

    public static List<Match>  getPartidoakPremier() {
        return partidoakPremier;
    }

    public static List<Match> getAllJSONMatches() {
        return allJSONMatches;
    }
    public static List<Proba> getExternalStandingsLaLiga() {
        return externalStandingsLaLiga;
    }

    public static List<Proba> getExternalStandingsPremier() {
        return externalStandingsPremier;
    }



    private static List<Proba> allJSONStandings;
    public static List<Proba> getGetAllStandings() {
        return allJSONStandings;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 20, 0, 0);
        try {
            allJSONMatches=DataAccess.loadMatchesFromJSON();
            allJSONStandings=DataAccess.loadStandingsFromJSON();
            partidoakLaLiga=getMatchesFromAPI( params,"La Liga");
            partidoakPremier=getMatchesFromAPI( params,"Premier League");
            externalStandingsLaLiga=getStandingsFromAPI(params,"La Liga");
            externalStandingsPremier =getStandingsFromAPI(params,"Premier League");
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_seasons, R.id.nav_slideshow, R.id.nav_next, R.id.nav_currentStandings)
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

    private List<Match> getMatchesFromAPI(LinearLayout.LayoutParams params, String competi) {
        CompletableFuture<Match[]> futurePartidoak = DataAccess.getMatchesFromAPI(competi);
        futurePartidoak.join();
        Match[] allMatches = futurePartidoak.getNow(null);
        if (allMatches != null) {
            return Arrays.asList(allMatches);
        } else {
            return Arrays.asList(new Match[0]); // errore kasua
        }
    }
    private List<Proba> getStandingsFromAPI(LinearLayout.LayoutParams params, String competi) {
        CompletableFuture<Proba[]> futureStandings = DataAccess.getStandingsFromAPI(competi);
        futureStandings.join();
        Proba[] allStandings = futureStandings.getNow(null);
        if (allStandings != null) {
            System.out.println("All standings of "+competi+allStandings);
            return Arrays.asList(allStandings);
        } else {
            return Arrays.asList(new Proba[0]); // errore kasua
        }
    }
    public static LinearLayout printMatch(LinearLayout linearLayout, Context context, LinearLayout.LayoutParams params, Match partido, Boolean finished) {
        //Pantailaren zabalera lortu
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels-100;

        //Estilo bat definitu
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.TRANSPARENT);  // Fondo kolorea
        gd.setStroke(2, Color.BLACK);  // Bordearen kolorea eta lodiera
        gd.setCornerRadius(30);  // Bordeak borobilak

        //Jornada lortu
        int currentMatchDay = Integer.parseInt(partido.getLeague().getRound().split(" ")[3]);

        //Etxekoaren izena lortu
        String homeName = partido.getTeams().getHome().getName();

        //Kanpokoaren izena lortu
        String awayName = partido.getTeams().getAway().getName();

        //Etxekoaren izenaren textView sortu
        TextView homeNameView = new TextView(context);
        homeNameView.setGravity(Gravity.CENTER_HORIZONTAL);
        homeNameView.setTextSize(16);
        homeNameView.setText(homeName);

        //Etxekoaren logoa lortu
        ImageView homePic = new ImageView(context);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(100,100);
        //params.gravity= Gravity.CENTER;
        homePic.setLayoutParams(imageParams);
        String homeURL = partido.getTeams().getHome().getLogo();
        Picasso.get().load(homeURL).into(homePic);

        //Etxekoaren layout sortu
        LinearLayout homeLayout = new LinearLayout(context);
        homeLayout.setOrientation(LinearLayout.VERTICAL);
        homeLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        homeLayout.addView(homePic);
        homeLayout.addView(homeNameView);

        //Kanpokoaren izenaren textView sortu
        TextView awayNameView = new TextView(context);
        awayNameView.setGravity(Gravity.CENTER_HORIZONTAL);
        awayNameView.setTextSize(16);
        awayNameView.setText(awayName);

        //Kanpokoaren logoa lortu
        ImageView awayPic = new ImageView(context);
        awayPic.setLayoutParams(imageParams);
        String awayURL = partido.getTeams().getAway().getLogo();
        Picasso.get().load(awayURL).into(awayPic);

        //Kanpokoaren layout sortu
        LinearLayout awayLayout = new LinearLayout(context);
        awayLayout.setOrientation(LinearLayout.VERTICAL);
        awayLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        awayLayout.addView(awayPic);
        awayLayout.addView(awayNameView);

        //Partidoaren oinarrizko layout sortu
        LinearLayout layoutHorizontal = new LinearLayout(context);
        layoutHorizontal.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        layoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);

        //Partidoaren layout osoa sortu
        LinearLayout layoutVertical = new LinearLayout(context);
        layoutVertical.setOrientation(LinearLayout.VERTICAL);
        layoutVertical.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        layoutVertical.setBackground(gd);
        LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) layoutVertical.getLayoutParams();
        params3.bottomMargin=25;
        layoutVertical.setLayoutParams(params3);

        if (finished){
            //Emaitza lortu
            String score = partido.getGoals().getHome()+" - "+ partido.getGoals().getAway();
            //Emaitzaren textView sortu
            TextView scoreView = new TextView(context);
            scoreView.setTextSize(50);
            scoreView.setGravity(Gravity.CENTER_HORIZONTAL);
            scoreView.setText(score);

            //Partidoaren data lortu
            String date1 = partido.getFixture().getDate().split("T")[0];

            //Dataren textView sortu
            TextView dateView = new TextView(context);
            dateView.setGravity(Gravity.CENTER_HORIZONTAL);
            dateView.setTextSize(30);
            dateView.setText(date1);

            //Finished texView sortu
            TextView finishedView = new TextView(context);
            finishedView.setGravity(Gravity.CENTER_HORIZONTAL);
            finishedView.setTextSize(20);
            finishedView.setText("Finished");

            //Partidoaren oinarrizko layout bete
            layoutHorizontal.addView(homeLayout);
            layoutHorizontal.addView(scoreView);
            layoutHorizontal.addView(awayLayout);

            // Pantailaren zabaleraren arabera view bakoitzaren zabalera ajustatu
            for (int i = 0; i < layoutHorizontal.getChildCount(); i++) {
                View child = layoutHorizontal.getChildAt(i);
                LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) child.getLayoutParams();
                params2.width = screenWidth / layoutHorizontal.getChildCount();
                params2.topMargin=15;
                child.setLayoutParams(params2);
            }

            //Partidoaren layout osoa bete
            layoutVertical.addView(dateView);
            layoutVertical.addView(layoutHorizontal);
            layoutVertical.addView(finishedView);
        }
        else {

            //Live-rako elementuak sortu
            TextView liveText = null;
            TextView liveView = null;
            ImageView livePic = null;
            LinearLayout liveLayout = null;

            boolean liveDago = partido.getFixture().getStatus().getElapsed() !=null &&
                    partido.getFixture().getStatus().getElapsed()<90;
            //Live badago partidoa (probetarako true)
            if (liveDago){

                //Emaitza lortu
                String score = partido.getGoals().getHome()+" - "+ partido.getGoals().getAway();

                //Emaitzaren textView sortu
                TextView scoreView = new TextView(context);
                scoreView.setTextSize(50);
                scoreView.setGravity(Gravity.CENTER_HORIZONTAL);
                scoreView.setText(score);

                //Live layout sortu
                livePic = new ImageView(context);
                LinearLayout.LayoutParams liveImageParams = new LinearLayout.LayoutParams(150,150);
                params.gravity= Gravity.CENTER_HORIZONTAL;
                livePic.setLayoutParams(liveImageParams);
                String liveURL = "https://static.vecteezy.com/system/resources/previews/016/314/808/original/transparent-live-transparent-live-icon-free-png.png";
                Picasso.get().load(liveURL).into(livePic);
                //linearLayout.addView(livePic);
                liveLayout = new LinearLayout(context);
                liveLayout.setOrientation(LinearLayout.HORIZONTAL);
                liveLayout.setGravity(Gravity.CENTER_HORIZONTAL);
                liveLayout.addView(livePic);

                //Minutua lortu
                String live ="min "+partido.getFixture().getStatus().getElapsed();

                //Minutuaren textView sortu
                liveText = new TextView(context);
                liveText.setGravity(Gravity.CENTER_HORIZONTAL);
                liveText.setText(live);
                liveText.setTextSize(16);
                liveText.setTextColor(Color.GREEN);

                //Partidoaren oinarrizko layout bete
                layoutHorizontal.addView(homeLayout);
                layoutHorizontal.addView(scoreView);
                layoutHorizontal.addView(awayLayout);

                // Pantailaren zabaleraren arabera view bakoitzaren zabalera ajustatu
                for (int i = 0; i < layoutHorizontal.getChildCount(); i++) {
                    View child = layoutHorizontal.getChildAt(i);
                    LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) child.getLayoutParams();
                    params2.width = screenWidth / layoutHorizontal.getChildCount();
                    params2.topMargin=15;
                    child.setLayoutParams(params2);

                }

                //Partidoaren layout osoa bete
                layoutVertical.addView(liveLayout);
                layoutVertical.addView(layoutHorizontal);
                layoutVertical.addView(liveText);

            }
            else {
                //Partidoaren data lortu
                String date1 = partido.getFixture().getDate().split("T")[0];
                String date2 = partido.getFixture().getDate().split("T")[1];
                String date3 = date2.split("\\+" )[0];
                int hour = Integer.parseInt(date3.split(":")[0]);
                hour = hour+2;
                date3 = hour+":"+date3.split(":")[1];

                //Dataren textView sortu
                TextView dateView = new TextView(context);
                dateView.setGravity(Gravity.CENTER_HORIZONTAL);
                dateView.setTextSize(20);
                dateView.setText(date1 + "\n" + date3);

                //Partidoaren oinarrizko layout bete
                layoutHorizontal.addView(homeLayout);
                layoutHorizontal.addView(dateView);
                layoutHorizontal.addView(awayLayout);

                // Pantailaren zabaleraren arabera view bakoitzaren zabalera ajustatu
                for (int i = 0; i < layoutHorizontal.getChildCount(); i++) {
                    View child = layoutHorizontal.getChildAt(i);
                    LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) child.getLayoutParams();
                    params2.width = screenWidth / layoutHorizontal.getChildCount();
                    params2.topMargin=15;
                    child.setLayoutParams(params2);

                }

                //Partidoaren layout osoa bete
                layoutVertical.addView(layoutHorizontal);


            }

        }








        /*TextView textView = new TextView(getContext());
        GradientDrawable shape = new GradientDrawable();
        shape.setColor(Color.LTGRAY);
        shape.setCornerRadius(18);
        textView.setBackground(shape);
        textView.setTextColor(Color.BLACK);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textView.setLayoutParams(params);
        textView.setWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250,
                getResources().getDisplayMetrics()));
        textView.setText(result);*/







        //layoutHorizontal.setBackground(gd);

        //layoutHorizontal.addView(homePic);
        //layoutHorizontal.addView(homeNameView);

        //layoutHorizontal.addView(awayNameView);
        //layoutHorizontal.addView(awayPic);
        //linearLayout.addView(layoutHorizontal);
        //linearLayout.addView(liveView);






        //layoutVertical.setGravity(Gravity.CENTER_HORIZONTAL);




        //Zenbatgarren jornadakoak diren jarri
        if (previousMatchDay!=currentMatchDay || previousMatchDay==-1) {
            TextView matchDayView = new TextView(context);
            matchDayView.setGravity(Gravity.CENTER_HORIZONTAL);
            matchDayView.setText("Match day "+currentMatchDay);
            matchDayView.setTextSize(20);
            linearLayout.addView(matchDayView);
            previousMatchDay = currentMatchDay;
            //if ()
        }
        linearLayout.addView(layoutVertical);

        return linearLayout;
    }

    public static void loadImageInUI(Context context, String imageUrl, ImageView imageView) {
        if (context != null) {
            // Ejecutar código en el hilo de la interfaz de usuario
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Cargar la imagen en el ImageView utilizando Picasso
                    Picasso.get().load(imageUrl).into(imageView);
                }
            });
        }
    }

    public static TableLayout printStanding(Context context, List<Standing> ranking){

        TableRow tableMainRow = new TableRow(context);
        tableMainRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        ));
        tableMainRow.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView empty = new TextView(context);
        empty.setText("");
        empty.setPadding(10, 0, 10, 0);
        tableMainRow.addView(empty);
        TextView empty2 = new TextView(context);
        empty2.setText("");
        empty2.setPadding(10, 0, 10, 0);
        tableMainRow.addView(empty2);
        TextView teamName = new TextView(context);
        teamName.setText("Team");
        teamName.setPadding(10, 0, 10, 0);
        teamName.setTextSize(15);
        tableMainRow.addView(teamName);
        TextView playedGames = new TextView(context);
        playedGames.setText("Games");
        playedGames.setPadding(10, 0, 10, 0);
        playedGames.setTextSize(15);
        //playedGames.setPadding(10, 10, 10, 10);
        tableMainRow.addView(playedGames);
        TextView won = new TextView(context);
        won.setText("W");
        won.setPadding(10, 0, 10, 0);
        won.setTextSize(15);
        //won.setPadding(10, 10, 10, 10);
        tableMainRow.addView(won);
        TextView draw = new TextView(context);
        draw.setText("D");
        draw.setPadding(10, 0, 10, 0);
        draw.setTextSize(15);
        //draw.setPadding(10, 10, 10, 10);
        tableMainRow.addView(draw);
        TextView lost = new TextView(context);
        lost.setText("L");
        lost.setPadding(10, 0, 10, 0);
        lost.setTextSize(15);
        //lost.setPadding(10, 10, 10, 10);
        tableMainRow.addView(lost);
        TextView goalDifference = new TextView(context);
        goalDifference.setText("Diff");
        goalDifference.setPadding(10, 0, 10, 0);
        goalDifference.setTextSize(15);
        //goalDifference.setPadding(10, 10, 10, 10);
        tableMainRow.addView(goalDifference);
        TextView points = new TextView(context);
        points.setText("P");
        points.setPadding(10, 0, 10, 0);
        points.setTextSize(15);
        points.setTypeface(null, Typeface.BOLD);
        //points.setPadding(10, 10, 10, 10);
        tableMainRow.addView(points);
        //linearLayout.addView(tableMainRow);

        TableLayout tableLayout = new TableLayout(context);
        tableLayout.addView(tableMainRow);
// Crear las filas y las columnas de la tabla
        for (Standing s : ranking) {
            TableRow tableRow = new TableRow(context);
            tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            ));
            TextView rank = new TextView(context);
            rank.setText(String.valueOf(s.getRank()));
            rank.setPadding(10, 0, 10, 0);
            rank.setTextSize(15);
            tableRow.addView(rank);
            ImageView homePic = new ImageView(context);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(100,115);
            //params.gravity= Gravity.CENTER;
            homePic.setLayoutParams(imageParams);
            String homeURL = s.getTeam().getLogo();
            Picasso.get().load(homeURL).into(homePic);
            LinearLayout homeLayout = new LinearLayout(context);
            homeLayout.setOrientation(LinearLayout.VERTICAL);
            homeLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            homeLayout.setPadding(10, 5, 10, 5);
            homeLayout.addView(homePic);
            tableRow.addView(homeLayout);
            String teamNameString = s.getTeam().getName();
            String[] teamNameSplit = teamNameString.split(" ");
            switch (teamNameSplit.length){
                case 1:
                    teamNameString = teamNameSplit[0];
                    break;
                case 2:
                    teamNameString = teamNameSplit[0]+"\n"+teamNameSplit[1];
                    break;
                case 3:
                    teamNameString = teamNameSplit[0]+"\n"+teamNameSplit[1]+ " " +teamNameSplit[2];
                    break;
            }
            TextView name = new TextView(context);
            name.setText(teamNameString);
            name.setPadding(10, 0, 10, 0);
            name.setTextSize(15);
            tableRow.addView(name);
            int gamesPlayed = s.getAll().getPlayed();
            int gamesWon = s.getAll().getWin();
            int gamesDraw = s.getAll().getDraw();
            int gamesLost = s.getAll().getLose();
            int goalDiff = s.getGoalsDiff();
            int pointsEarned = s.getPoints();
            List<Integer> list = new ArrayList<Integer>();
            list.add(gamesPlayed);
            list.add(gamesWon);
            list.add(gamesDraw);
            list.add(gamesLost);
            list.add(goalDiff);
            list.add(pointsEarned);

            for (int j = 0; j < tableMainRow.getChildCount()-3; j++) {
                TextView textView = new TextView(context);
                textView.setText(list.get(j).toString());
                textView.setTextSize(15);
                if (j==tableMainRow.getChildCount()-4){
                    textView.setTypeface(null, Typeface.BOLD);
                    textView.setPadding(10, 0, 10, 0);
                }
                else {
                    textView.setPadding(10, 0, 10, 0);
                }


                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                );

                tableRow.addView(textView, layoutParams);
            }

            tableLayout.addView(tableRow);
        }
        return tableLayout;
    }




}