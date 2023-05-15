package com.example.futbolapp.ui.slideshow;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.futbolapp.DataAccess;
import com.example.futbolapp.MainActivity;
import com.example.futbolapp.R;
import com.example.futbolapp.databinding.FragmentSlideshowBinding;
import com.example.futbolapp.gureKlaseak.League;
import com.example.futbolapp.gureKlaseak.Proba;
import com.example.futbolapp.gureKlaseak.Standing;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private List<Standing> ranking;
    private Spinner spinnerYears;

    private Spinner spinnerCompetitions;
    private LinearLayout linearLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_slideshow, container, false);






        spinnerYears = rootView.findViewById(R.id.spinnerYears);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.seasons_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYears.setAdapter(adapter);
        spinnerCompetitions = rootView.findViewById(R.id.spinnerCompetitions);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.competi_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCompetitions.setAdapter(adapter2);
        spinnerCompetitions.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout = rootView.findViewById(R.id.myLinearLayout);


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

    private void tratatuSpinner( View rootView) {
        linearLayout = rootView.findViewById(R.id.myLinearLayout);


        linearLayout.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 20, 0, 0);
        String urtea= spinnerYears.getSelectedItem().toString().split("-")[0];
        String competi = spinnerCompetitions.getSelectedItem().toString();


        ranking= filterJSONStandings(MainActivity.getGetAllStandings(),urtea,competi);
        linearLayout.addView(MainActivity.printStanding(getContext(),ranking));
    }

    public List<Standing> filterJSONStandings(List<Proba> allStandings, String urtea, String competi){
        League league = allStandings.stream()
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
        return st;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /*private TableLayout createTableAndMainRow(){

        return tableLayout;
    }*/
}