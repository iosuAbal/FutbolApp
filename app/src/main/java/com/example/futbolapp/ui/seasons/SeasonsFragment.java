package com.example.futbolapp.ui.seasons;


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
import com.example.futbolapp.R;
import com.example.futbolapp.gureKlaseak.Match;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class SeasonsFragment extends Fragment {


    private LinearLayout linearLayout;

    private Spinner spinnerYears;
    private Spinner spinnerCompetitions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_seasons, container, false);


        linearLayout = rootView.findViewById(R.id.myLinearLayout);

        spinnerYears = rootView.findViewById(R.id.spinnerYears);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.seasons_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYears.setAdapter(adapter);


        spinnerCompetitions = rootView.findViewById(R.id.spinnerCompetitions);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.competi_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCompetitions.setAdapter(adapter2);





        spinnerYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tratatuSpinner(rootView);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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

    public void tratatuSpinner(View rootView){
        linearLayout = rootView.findViewById(R.id.myLinearLayout);
        String selectedItem = spinnerYears.getSelectedItem().toString();

        linearLayout.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 20, 0, 0);
        String urtea= spinnerYears.getSelectedItem().toString().split("-")[0];
        String competi= spinnerCompetitions.getSelectedItem().toString();
        List<Match> partidoak;
        try {
            partidoak= filterPastMatches(MainActivity.getAllJSONMatches(),urtea,competi);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (Match partido: partidoak) {
            linearLayout = MainActivity.printMatch(linearLayout, getContext(), params, partido,true);
        }

    }
    public static List<Match> filterPastMatches(List<Match> allMatches, String urtea, String competi) throws IOException, ExecutionException, InterruptedException {
        List<Match> filteredMatches = allMatches.stream()
                .filter(m -> m.getLeague().getSeason() == (Integer.parseInt(urtea)))
                .filter(m -> m.getLeague().getName().equals(competi))
                .collect(Collectors.toList());
        return filteredMatches;
    }




}
