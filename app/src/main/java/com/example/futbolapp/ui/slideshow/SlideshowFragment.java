package com.example.futbolapp.ui.slideshow;

import static com.example.futbolapp.DataAccess.getMatchesFromJson;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.futbolapp.DataAccess;
import com.example.futbolapp.MainActivity;
import com.example.futbolapp.R;
import com.example.futbolapp.databinding.FragmentSlideshowBinding;
import com.example.futbolapp.gureKlaseak.Match;
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
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.TRANSPARENT);  // Color de fondo del LinearLayout
        gd.setStroke(2, Color.BLACK);  // Ancho y color del borde
        gd.setCornerRadius(30);  // Radio de los bordes redondeados



        spinnerYears = rootView.findViewById(R.id.spinnerYears);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.seasons_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYears.setAdapter(adapter);
        spinnerYears.setBackground(gd);
        spinnerYears.setGravity(Gravity.CENTER_HORIZONTAL);

        spinnerCompetitions = rootView.findViewById(R.id.spinnerCompetitions);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.competi_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCompetitions.setAdapter(adapter2);
        spinnerCompetitions.setBackground(gd);
        spinnerCompetitions.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout = rootView.findViewById(R.id.myLinearLayout);
        //TableLayout tableLayout = createTableAndMainRow();

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
                String urtea = selectedItem.split("-")[0];
                String competi = spinnerCompetitions.getSelectedItem().toString();


                try {
                    ranking= DataAccess.getStandingsFromJson(urtea, competi);
                    System.out.println(ranking);
                    for(Standing s :ranking){
                        printStanding(s);
                    }
                } catch (IOException e) {
                    System.out.println("error on standings");
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        return rootView;
    }

    private void printStanding(Standing s) {
        //String result=s.getRank()+" "+s.getTeam().getName()+"    "+s.getPoints();
        //System.out.println(result);

        // Crear el TableLayout
        TableRow tableMainRow = new TableRow(getContext());
        tableMainRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        ));
        tableMainRow.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView teamName = new TextView(getContext());
        teamName.setText("Team");
        //teamName.setPadding(10, 10, 10, 10);
        tableMainRow.addView(teamName);
        TextView playedGames = new TextView(getContext());
        playedGames.setText("Played");
        //playedGames.setPadding(10, 10, 10, 10);
        tableMainRow.addView(playedGames);
        TextView won = new TextView(getContext());
        won.setText("Won");
        //won.setPadding(10, 10, 10, 10);
        tableMainRow.addView(won);
        TextView draw = new TextView(getContext());
        draw.setText("Draw");
        //draw.setPadding(10, 10, 10, 10);
        tableMainRow.addView(draw);
        TextView lost = new TextView(getContext());
        lost.setText("Lost");
        //lost.setPadding(10, 10, 10, 10);
        tableMainRow.addView(lost);
        TextView goalDifference = new TextView(getContext());
        goalDifference.setText("G.Diff");
        //goalDifference.setPadding(10, 10, 10, 10);
        tableMainRow.addView(goalDifference);
        TextView points = new TextView(getContext());
        points.setText("Points");
        //points.setPadding(10, 10, 10, 10);
        tableMainRow.addView(points);
        //linearLayout.addView(tableMainRow);

        TableLayout tableLayout = new TableLayout(getContext());
        tableLayout.addView(tableMainRow);
// Crear las filas y las columnas de la tabla
        for (int i = 0; i < 3; i++) {
            TableRow tableRow = new TableRow(getContext());
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            ));

            for (int j = 0; j < 3; j++) {
                TextView textView = new TextView(getContext());
                textView.setText( + i + "," + j);
                textView.setPadding(10, 10, 10, 10);

                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                );

                tableRow.addView(textView, layoutParams);
            }

            tableLayout.addView(tableRow);
        }
        linearLayout.addView(tableLayout);

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