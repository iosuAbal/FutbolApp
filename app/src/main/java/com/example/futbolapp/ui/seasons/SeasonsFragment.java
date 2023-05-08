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
import com.example.futbolapp.MainActivity;
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

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.TRANSPARENT);  // Color de fondo del LinearLayout
        gd.setStroke(2, Color.BLACK);  // Ancho y color del borde
        gd.setCornerRadius(30);

        spinnerYears = rootView.findViewById(R.id.spinnerYears);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.seasons_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYears.setAdapter(adapter);
        spinnerYears.setBackground(gd);

        spinnerCompetitions = rootView.findViewById(R.id.spinnerCompetitions);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.competi_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCompetitions.setAdapter(adapter2);
        linearLayout = rootView.findViewById(R.id.myLinearLayout);
        linearLayout.setBackground(gd);



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
                    linearLayout = MainActivity.printMatch(linearLayout, getContext(), params, partido,true);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se hace nada si no se selecciona nada
            }
        });
        return rootView;
    }




}
