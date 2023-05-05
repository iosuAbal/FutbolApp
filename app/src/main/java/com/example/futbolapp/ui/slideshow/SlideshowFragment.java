package com.example.futbolapp.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.futbolapp.DataAccess;
import com.example.futbolapp.R;
import com.example.futbolapp.databinding.FragmentSlideshowBinding;
import com.example.futbolapp.gureKlaseak.Standing;

import java.io.IOException;
import java.util.List;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private List<Standing> ranking;
    private Spinner spinnerYears;
    private Spinner spinnerCompetitions;
    private LinearLayout linearLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
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

        try {
            ranking= DataAccess.getStandingsFromJson("2020", "La Liga");
            System.out.println(ranking);
            for(Standing s :ranking){
                printStanding(s);
            }
        } catch (IOException e) {
            System.out.println("error on standings");
            throw new RuntimeException(e);
        }


        return rootView;
    }

    private void printStanding(Standing s) {
        String result=s.getRank()+" "+s.getTeam().getName()+"    "+s.getPoints();
        System.out.println(result);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}