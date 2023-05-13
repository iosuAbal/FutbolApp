package com.example.futbolapp;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.futbolapp.databinding.ActivityMainBinding;
import com.example.futbolapp.gureKlaseak.Match;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.example.futbolapp.R;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private static int previousMatchDay = -1;

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
                R.id.nav_home, R.id.nav_seasons, R.id.nav_slideshow, R.id.nav_next)
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
            String score = partido.getScore().getFulltime().getHome()+" - "+ partido.getScore().getFulltime().getAway();
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
                String score = partido.getScore().getFulltime().getHome()+" - "+ partido.getScore().getFulltime().getAway();

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

                //Dataren textView sortu
                TextView dateView = new TextView(context);
                dateView.setGravity(Gravity.CENTER_HORIZONTAL);
                dateView.setTextSize(20);
                dateView.setText(date1);

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




}