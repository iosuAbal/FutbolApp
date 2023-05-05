package com.example.futbolapp.ui.seasons;

import static com.example.futbolapp.DataAccess.getMatchesFromJson;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.futbolapp.DataAccess;
import com.example.futbolapp.gureKlaseak.Match;
import com.example.futbolapp.R;
import com.example.futbolapp.ui.home.HomeFragment;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class SeasonsFragment extends Fragment {


    private LinearLayout linearLayout;

    private Spinner spinnerYears;
    private Spinner spinnerCompetitions;
    private int previousMatchDay = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_seasons, container, false);

        spinnerYears = rootView.findViewById(R.id.spinnerYears);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.seasons_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYears.setAdapter(adapter);

        spinnerCompetitions = rootView.findViewById(R.id.spinnerCompetitions);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.competi_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCompetitions.setAdapter(adapter2);
        linearLayout = rootView.findViewById(R.id.myLinearLayout);



        spinnerYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                linearLayout = rootView.findViewById(R.id.myLinearLayout);
                String selectedItem = parent.getItemAtPosition(position).toString();

                linearLayout.removeAllViews();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 20, 0, 0);
                String urtea= selectedItem.split("-")[0];
                String competi= spinnerCompetitions.getSelectedItem().toString();
                List<Match> partidoak;

                try {
                    partidoak=getMatchesFromJson(urtea,competi);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                for (Match partido: partidoak) {
                    printMatch(params, partido);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se hace nada si no se selecciona nada
            }
        });
        return rootView;
    }

    private void printMatch(LinearLayout.LayoutParams params, Match partido) {
        /*String result= partido.getTeams().getHome().getName()+" "+ partido.getScore().getFulltime().getHome()
                +" - "+
                partido.getScore().getFulltime().getAway()+"  "+ partido.getTeams().getAway().getName();*/
        int currentMatchDay = Integer.parseInt(partido.getLeague().getRound().split(" ")[3]);
        String homeName = partido.getTeams().getHome().getName();
        String score = partido.getScore().getFulltime().getHome()+" - "+ partido.getScore().getFulltime().getAway();
        String awayName = partido.getTeams().getAway().getName();
        TextView liveText = null;
        TextView liveView = null;
        ImageView livePic = null;
        LinearLayout liveLayout = null;
        if (true){
            /*String live ="LIVE min("+partido.getFixture().getStatus().getElapsed()+")";
            liveView = new TextView(getContext());
            liveView.setGravity(Gravity.CENTER_HORIZONTAL);
            liveView.setText(live);*/
            livePic = new ImageView(getContext());
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(150,150);
            params.gravity= Gravity.CENTER_HORIZONTAL;
            livePic.setLayoutParams(imageParams);
            String liveURL = "https://static.vecteezy.com/system/resources/previews/016/314/808/original/transparent-live-transparent-live-icon-free-png.png";
            Picasso.get().load(liveURL).into(livePic);
            //linearLayout.addView(livePic);
            liveLayout = new LinearLayout(getContext());
            liveLayout.setOrientation(LinearLayout.HORIZONTAL);
            liveLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            liveLayout.addView(livePic);

            String live ="min "+partido.getFixture().getStatus().getElapsed();
            liveText = new TextView(getContext());
            liveText.setGravity(Gravity.CENTER_HORIZONTAL);
            liveText.setText(live);
            liveText.setTextSize(16);
            liveText.setTextColor(Color.GREEN);
        }

        TextView homeNameView = new TextView(getContext());
        homeNameView.setGravity(Gravity.CENTER_HORIZONTAL);
        homeNameView.setTextSize(16);
        homeNameView.setText(homeName);
        TextView scoreView = new TextView(getContext());
        scoreView.setTextSize(50);
        scoreView.setGravity(Gravity.CENTER_HORIZONTAL);
        scoreView.setText(score);
        TextView awayNameView = new TextView(getContext());
        awayNameView.setGravity(Gravity.CENTER_HORIZONTAL);
        awayNameView.setTextSize(16);
        awayNameView.setText(awayName);
        ImageView homePic = new ImageView(getContext());
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(100,100);
        //params.gravity= Gravity.CENTER;
        homePic.setLayoutParams(imageParams);
        String homeURL = partido.getTeams().getHome().getLogo();
        Picasso.get().load(homeURL).into(homePic);
        LinearLayout homeLayout = new LinearLayout(getContext());
        homeLayout.setOrientation(LinearLayout.VERTICAL);
        homeLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        homeLayout.addView(homePic);
        homeLayout.addView(homeNameView);
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
        ImageView awayPic = new ImageView(getContext());
        awayPic.setLayoutParams(imageParams);
        String awayURL = partido.getTeams().getAway().getLogo();
        Picasso.get().load(awayURL).into(awayPic);
        LinearLayout awayLayout = new LinearLayout(getContext());
        awayLayout.setOrientation(LinearLayout.VERTICAL);
        awayLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        awayLayout.addView(awayPic);
        awayLayout.addView(awayNameView);


        LinearLayout layoutHorizontal = new LinearLayout(getContext());
        layoutHorizontal.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        layoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.TRANSPARENT);  // Color de fondo del LinearLayout
        gd.setStroke(2, Color.BLACK);  // Ancho y color del borde
        gd.setCornerRadius(30);  // Radio de los bordes redondeados

        //layoutHorizontal.setBackground(gd);

        //layoutHorizontal.addView(homePic);
        //layoutHorizontal.addView(homeNameView);
        layoutHorizontal.addView(homeLayout);
        layoutHorizontal.addView(scoreView);
        layoutHorizontal.addView(awayLayout);
        //layoutHorizontal.addView(awayNameView);
        //layoutHorizontal.addView(awayPic);
        //linearLayout.addView(layoutHorizontal);
        //linearLayout.addView(liveView);
        DisplayMetrics displayMetrics = new DisplayMetrics();

        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels-100;


        // Establecer el ancho de cada View en función del ancho de la pantalla
        for (int i = 0; i < layoutHorizontal.getChildCount(); i++) {
            View child = layoutHorizontal.getChildAt(i);
            LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) child.getLayoutParams();
            params2.width = screenWidth / layoutHorizontal.getChildCount();
            params2.topMargin=15;
            child.setLayoutParams(params2);

        }

        LinearLayout layoutVertical = new LinearLayout(getContext());
        layoutVertical.setOrientation(LinearLayout.VERTICAL);
        layoutVertical.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        //layoutVertical.setGravity(Gravity.CENTER_HORIZONTAL);
        layoutVertical.addView(liveLayout);
        layoutVertical.addView(layoutHorizontal);
        layoutVertical.addView(liveText);

        layoutVertical.setBackground(gd);
        //bottom margin to layoutVertical
        LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) layoutVertical.getLayoutParams();
        params3.bottomMargin=25;

        layoutVertical.setLayoutParams(params3);


        if (previousMatchDay!=currentMatchDay || previousMatchDay==-1) {
            TextView matchDayView = new TextView(getContext());
            matchDayView.setGravity(Gravity.CENTER_HORIZONTAL);
            matchDayView.setText("Match day "+currentMatchDay);
            matchDayView.setTextSize(20);
            linearLayout.addView(matchDayView);
            previousMatchDay = currentMatchDay;
            //if ()
        }
        linearLayout.addView(layoutVertical);
    }


}
