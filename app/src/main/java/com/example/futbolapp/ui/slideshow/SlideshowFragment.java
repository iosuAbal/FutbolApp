package com.example.futbolapp.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.futbolapp.DataAccess;
import com.example.futbolapp.databinding.FragmentSlideshowBinding;
import com.example.futbolapp.gureKlaseak.Standing;

import java.io.IOException;
import java.util.List;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private List<Standing> ranking;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SeasonsViewModel seasonsViewModel =
                new ViewModelProvider(this).get(SeasonsViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

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