package com.example.futbolapp.ui.seasons;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;





import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.futbolapp.MainActivity;
import com.example.futbolapp.MyObject;
import com.example.futbolapp.R;
import com.example.futbolapp.databinding.FragmentSlideshowBinding;
import com.example.futbolapp.ui.slideshow.SeasonsViewModel;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpEntity;

import java.io.IOException;

public class SeasonsFragment extends Fragment {



    private Spinner spinnerYears;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_seasons, container, false);

        spinnerYears = rootView.findViewById(R.id.spinnerYears);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.seasons_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYears.setAdapter(adapter);
        spinnerYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                Log.d("SeasonsFragment", "Selected item: " + selectedItem);
                try {
                    ((MainActivity) getActivity()).getFromAPI("https://api.football-data.org/v4/teams/86/matches?season=2010");
                    System.out.println("Deia eginda");
                } catch (IOException e) {
                    System.out.print("Error when calling api");
                    throw new RuntimeException(e);
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
