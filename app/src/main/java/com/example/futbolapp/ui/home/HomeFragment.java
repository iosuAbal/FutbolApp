package com.example.futbolapp.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.futbolapp.DataAccess;
import com.example.futbolapp.R;
import com.example.futbolapp.databinding.FragmentHomeBinding;
import com.example.futbolapp.gureKlaseak.Match;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private LinearLayout linearLayout;
    private Match partidoak[];
    final CountDownLatch latch = new CountDownLatch(1);

// Realizar la llamada a la API


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();


        linearLayout = rootView.findViewById(R.id.myLinearLayout);
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        //-----
        partidoak = null;
        while (partidoak == null) {
            partidoak = DataAccess.getMatchesFromAPI();
            if (partidoak == null) {
                // Si el valor es nulo, esperamos 500 milisegundos antes de volver a intentarlo
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("ha salido ..");



        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        String month = dateFormat.format(cal.getTime());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 20, 0, 0);

        System.out.println("AA"+partidoak);
        List<Match> unekoPartidoak = Arrays.stream(partidoak)
                .filter(m -> m.getFixture().getDate().
                        startsWith(String.valueOf(year)+"-"+ (month)))
                .filter(m->m.getFixture().getStatus().getElapsed()!=null)
                .collect(Collectors.toList());
        System.out.println("UNEKOAK "+unekoPartidoak.toString());
        for (Match m: unekoPartidoak){
            printMatch(params,m);
            System.out.println(m);
        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void printMatch(LinearLayout.LayoutParams params, Match partido) {
        /*String result= partido.getTeams().getHome().getName()+" "+ partido.getScore().getFulltime().getHome()
                +" - "+
                partido.getScore().getFulltime().getAway()+"  "+ partido.getTeams().getAway().getName();*/
        String homeName = partido.getTeams().getHome().getName();
        String score = partido.getScore().getFulltime().getHome()+" - "+ partido.getScore().getFulltime().getAway();
        String awayName = partido.getTeams().getAway().getName();
        TextView liveView = null;
        if (true){
            String live ="\n  LIVE min("+partido.getFixture().getStatus().getElapsed()+")";
            liveView = new TextView(getContext());
            liveView.setText(live);
        }

        TextView homeNameView = new TextView(getContext());
        homeNameView.setText(homeName);
        TextView scoreView = new TextView(getContext());
        scoreView.setText(score);
        TextView awayNameView = new TextView(getContext());
        awayNameView.setText(awayName);
        ImageView homePic = new ImageView(getContext());
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(100,100);
        //params.gravity= Gravity.CENTER;
        homePic.setLayoutParams(imageParams);
        String homeURL = partido.getTeams().getHome().getLogo();
        Picasso.get().load(homeURL).into(homePic);

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



        LinearLayout layoutHorizontal = new LinearLayout(getContext());
        layoutHorizontal.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        layoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.GREEN);  // Color de fondo del LinearLayout
        gd.setStroke(2, Color.BLACK);  // Ancho y color del borde
        gd.setCornerRadius(10);  // Radio de los bordes redondeados

        layoutHorizontal.setBackground(gd);

        layoutHorizontal.addView(homePic);
        layoutHorizontal.addView(homeNameView);
        layoutHorizontal.addView(scoreView);
        layoutHorizontal.addView(awayNameView);
        layoutHorizontal.addView(awayPic);
        //linearLayout.addView(layoutHorizontal);

        DisplayMetrics displayMetrics = new DisplayMetrics();

        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels-100;


// Establecer el ancho de cada View en función del ancho de la pantalla
        for (int i = 0; i < layoutHorizontal.getChildCount(); i++) {
            View child = layoutHorizontal.getChildAt(i);
            LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) child.getLayoutParams();
            params2.width = screenWidth / layoutHorizontal.getChildCount();
            child.setLayoutParams(params2);

        }

        LinearLayout layoutVertical = new LinearLayout(getContext());
        layoutVertical.setGravity(Gravity.CENTER_HORIZONTAL);
        layoutVertical.addView(layoutHorizontal);

            layoutVertical.addView(liveView);

        linearLayout.addView(layoutVertical);
    }

}