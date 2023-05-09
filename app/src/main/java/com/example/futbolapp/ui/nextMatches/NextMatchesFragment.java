package com.example.futbolapp.ui.nextMatches;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.futbolapp.MainActivity;
import com.example.futbolapp.gureKlaseak.Match;
import com.example.futbolapp.R;
import com.example.futbolapp.ui.home.HomeFragment;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NextMatchesFragment extends Fragment {


    private LinearLayout linearLayout;


    private Spinner spinnerCompetitions;
    private int previousMatchDay = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nextmatches, container, false);



        spinnerCompetitions = rootView.findViewById(R.id.spinnerComp2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.competi_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCompetitions.setAdapter(adapter2);
        linearLayout = rootView.findViewById(R.id.myLinearLayout);



        spinnerCompetitions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                linearLayout = rootView.findViewById(R.id.myLinearLayout);
                String competi = parent.getItemAtPosition(position).toString();

                linearLayout.removeAllViews();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 20, 0, 0);
                List<Match> partidoak = Arrays.stream(HomeFragment.getPartidoak())
                        .filter(m -> {
                            String fechaPartidoStr = m.getFixture().getDate();
                            OffsetDateTime fechaPartido = OffsetDateTime.parse(fechaPartidoStr);
                            OffsetDateTime fechaActual = OffsetDateTime.now();
                            return fechaPartido.isAfter(fechaActual);
                        })
                        .filter(m->m.getLeague().getName().equals(competi))
                        .collect(Collectors.toList());



                for (Match partido: partidoak) {
                    linearLayout = MainActivity.printMatch(linearLayout, getContext(), params, partido,false);
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