package com.example.futbolapp.ui.standings;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.futbolapp.MainActivity;
import com.example.futbolapp.R;
import com.example.futbolapp.databinding.FragmentSlideshowBinding;
import com.example.futbolapp.gureKlaseak.League;
import com.example.futbolapp.gureKlaseak.Standing;
import com.example.futbolapp.gureKlaseak.StandingResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CurrentStandingsFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private List<Standing> ranking;


    private Spinner spinnerCompetitions;
    private LinearLayout linearLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_currentstandings, container, false);

        spinnerCompetitions = rootView.findViewById(R.id.spinnerCompetitions);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.competi_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCompetitions.setAdapter(adapter2);
        spinnerCompetitions.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout = rootView.findViewById(R.id.myLinearLayout);



        spinnerCompetitions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tratatuSpinner(rootView);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    private void tratatuSpinner( View rootView) {
        linearLayout = rootView.findViewById(R.id.myLinearLayout);


        linearLayout.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 20, 0, 0);
        String competi = spinnerCompetitions.getSelectedItem().toString();


        switch (competi){
            case("La Liga"):
                ranking=filterCurrentJSONStandings(MainActivity.getExternalStandingsLaLiga());
                break;
            case("Premier League"):
                ranking=filterCurrentJSONStandings(MainActivity.getExternalStandingsPremier());
                break;
        }
        linearLayout.addView(MainActivity.printStanding(getContext(),ranking));
    }

    public List<Standing> filterCurrentJSONStandings(List<StandingResponse> allStandings){
        League league = allStandings.stream()
                .findFirst()
                .map(StandingResponse::getLeague)
                .orElse(null);

        List<Standing> st = new ArrayList<>();
        for (Standing[] standingArr : league.getStandings()) {
            if (standingArr != null && standingArr.length > 0) {
                Collections.addAll(st, standingArr);
            }
        }
        return st;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}