package com.example.futbolapp.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.futbolapp.DataAccess;
import com.example.futbolapp.MainActivity;
import com.example.futbolapp.R;
import com.example.futbolapp.databinding.FragmentHomeBinding;
import com.example.futbolapp.gureKlaseak.Match;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FragmentHomeBinding binding;
    private LinearLayout linearLayout;
    private Button buttonLoad;




    private Spinner spinnerCompetitions;
    private View  rootView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        linearLayout = rootView.findViewById(R.id.myLinearLayout);
        spinnerCompetitions = rootView.findViewById(R.id.spinnerCompetitions);
        buttonLoad=rootView.findViewById(R.id.buttonLoad);
        buttonLoad.setOnClickListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.competi_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCompetitions.setAdapter(adapter);






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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onClick(View v) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        String competi= spinnerCompetitions.getSelectedItem().toString();
        List<Match> filtratuta;
        switch (competi){
            case "La Liga" :
                filtratuta=filterPastMatches(MainActivity.getPartidoakLaLiga());
                Collections.reverse(filtratuta);
                printMatches(params, filtratuta);
                break;
            case "Premier League":
                filtratuta=filterPastMatches(MainActivity.getPartidoakPremier());
                Collections.reverse(filtratuta);
                printMatches(params,filtratuta);
                break;

        }
        buttonLoad.setEnabled(false);
    }

    public void tratatuSpinner(View rootView){
        buttonLoad.setEnabled(true);
        linearLayout = rootView.findViewById(R.id.myLinearLayout);
        linearLayout.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 20, 0, 0);
        String competi= spinnerCompetitions.getSelectedItem().toString();
        List<Match> filtratuta;
        switch ( competi){
            case "La Liga" :
                filtratuta = filterMatches(MainActivity.getPartidoakLaLiga());
                Collections.reverse(filtratuta);
                printMatches(params, filtratuta);
                break;
            case "Premier League":
                filtratuta = filterMatches(MainActivity.getPartidoakPremier());
                Collections.reverse(filtratuta);
                printMatches(params, filtratuta);
                break;

        }
    }


    @NonNull
    private static List<Match> filterMatches(List<Match>  allMatches) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        String month = dateFormat.format(cal.getTime());
        List<Match> unekoPartidoak = allMatches.stream()
                .filter(m -> m.getFixture().getDate().startsWith(String.valueOf(year) + "-" + (month)))
                .filter(m -> m.getFixture().getStatus().getElapsed() != null)
                .collect(Collectors.toList());
        return unekoPartidoak;
    }
    private static List<Match> filterPastMatches(List<Match>  allMatches) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1); // Restar 1 mes a la fecha actual
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1; // Sumar 1 porque los meses en Calendar van de 0 a 11

        List<Match> unekoPartidoak = allMatches.stream()
                .filter(m -> {
                    int matchYear = Integer.parseInt(m.getFixture().getDate().substring(0, 4));
                    int matchMonth = Integer.parseInt(m.getFixture().getDate().substring(5, 7));
                    return (matchYear < year || (matchYear == year && matchMonth <= month));
                })
                .filter(m -> m.getFixture().getStatus().getElapsed() != null)
                .collect(Collectors.toList());

        return unekoPartidoak;
    }
    private void printMatches(LinearLayout.LayoutParams params, List<Match> matches) {
        // on the main thread
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (Match m : matches) {
                    boolean finished=false;
                    if(m.getFixture().getStatus().getElapsed()!=null && m.getFixture().getStatus().getElapsed()==90){
                        finished=true;
                    }
                    MainActivity.printMatch(linearLayout, getContext(), params, m, finished);
                }
            }
        });
    }



}