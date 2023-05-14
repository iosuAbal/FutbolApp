package com.example.futbolapp.ui.standings;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CurrentStandingsFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private List<Standing> ranking;


    private Spinner spinnerCompetitions;
    private LinearLayout linearLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_currentstandings, container, false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 20, 0, 0);



        spinnerCompetitions = rootView.findViewById(R.id.spinnerCompetitions);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.competi_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCompetitions.setAdapter(adapter2);
        spinnerCompetitions.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout = rootView.findViewById(R.id.myLinearLayout);
        //TableLayout tableLayout = createTableAndMainRow();


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
        printStanding(ranking);
    }


    public List<Standing> filterCurrentJSONStandings(List<Proba> allStandings){
        League league = allStandings.stream()
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


    private void printStanding(List<Standing> ranking) {



        TableRow tableMainRow = new TableRow(getContext());

        tableMainRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        ));
        tableMainRow.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView empty = new TextView(getContext());
        empty.setText("");
        empty.setPadding(10, 0, 10, 0);
        tableMainRow.addView(empty);
        TextView empty2 = new TextView(getContext());
        empty2.setText("");
        empty2.setPadding(10, 0, 10, 0);
        tableMainRow.addView(empty2);
        TextView teamName = new TextView(getContext());
        teamName.setText("     Team");
        teamName.setPadding(10, 0, 10, 0);
        teamName.setTextSize(15);
        tableMainRow.addView(teamName);
        TextView playedGames = new TextView(getContext());
        playedGames.setText("      Games");
        playedGames.setPadding(10, 0, 10, 0);
        playedGames.setTextSize(15);
        //playedGames.setPadding(10, 10, 10, 10);
        tableMainRow.addView(playedGames);
        TextView won = new TextView(getContext());
        won.setText("     W");
        won.setPadding(10, 0, 10, 0);
        won.setTextSize(15);
        //won.setPadding(10, 10, 10, 10);
        tableMainRow.addView(won);
        TextView draw = new TextView(getContext());
        draw.setText("     D");
        draw.setPadding(10, 0, 10, 0);
        draw.setTextSize(15);
        //draw.setPadding(10, 10, 10, 10);
        tableMainRow.addView(draw);
        TextView lost = new TextView(getContext());
        lost.setText("L");
        lost.setPadding(10, 0, 10, 0);
        lost.setTextSize(15);
        //lost.setPadding(10, 10, 10, 10);
        tableMainRow.addView(lost);
        TextView goalDifference = new TextView(getContext());
        goalDifference.setText("     Diff");
        goalDifference.setPadding(10, 0, 10, 0);
        goalDifference.setTextSize(15);
        //goalDifference.setPadding(10, 10, 10, 10);
        tableMainRow.addView(goalDifference);
        TextView points = new TextView(getContext());
        points.setText("     P");
        points.setPadding(10, 0, 10, 0);
        points.setTextSize(15);
        points.setTypeface(null, Typeface.BOLD);
        //points.setPadding(10, 10, 10, 10);
        tableMainRow.addView(points);
        //linearLayout.addView(tableMainRow);

        TableLayout tableLayout = new TableLayout(getContext());
        tableLayout.addView(tableMainRow);
// Crear las filas y las columnas de la tabla
        for (Standing s : ranking) {
            TableRow tableRow = new TableRow(getContext());
            tableRow.setGravity(Gravity.CENTER_HORIZONTAL);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
            ));
            TextView rank = new TextView(getContext());
            rank.setText(String.valueOf(s.getRank()));
            rank.setPadding(10, 0, 10, 0);
            rank.setTextSize(15);
            tableRow.addView(rank);
            ImageView homePic = new ImageView(getContext());
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(100,100);
            //params.gravity= Gravity.CENTER;
            homePic.setLayoutParams(imageParams);
            String homeURL = s.getTeam().getLogo();
            Picasso.get().load(homeURL).into(homePic);
            LinearLayout homeLayout = new LinearLayout(getContext());
            homeLayout.setOrientation(LinearLayout.VERTICAL);
            homeLayout.setGravity(Gravity.CENTER_HORIZONTAL);
            homeLayout.setPadding(10, 5, 10, 5);
            homeLayout.addView(homePic);
            tableRow.addView(homeLayout);
            String teamNameString = s.getTeam().getName();
            String[] teamNameSplit = teamNameString.split(" ");
            switch (teamNameSplit.length){
                case 1:
                    teamNameString = teamNameSplit[0];
                    break;
                case 2:
                    teamNameString = teamNameSplit[0]+"\n"+teamNameSplit[1];
                    break;
                case 3:
                    teamNameString = teamNameSplit[0]+"\n"+teamNameSplit[1]+ " " +teamNameSplit[2];
                    break;
            }
            TextView name = new TextView(getContext());
            name.setText(teamNameString);
            name.setPadding(10, 0, 10, 0);
            name.setTextSize(15);
            tableRow.addView(name);
            int gamesPlayed = s.getAll().getPlayed();
            int gamesWon = s.getAll().getWin();
            int gamesDraw = s.getAll().getDraw();
            int gamesLost = s.getAll().getLose();
            int goalDiff = s.getGoalsDiff();
            int pointsEarned = s.getPoints();
            List<Integer> list = new ArrayList<Integer>();
            list.add(gamesPlayed);
            list.add(gamesWon);
            list.add(gamesDraw);
            list.add(gamesLost);
            list.add(goalDiff);
            list.add(pointsEarned);

            for (int j = 0; j < tableMainRow.getChildCount()-3; j++) {
                TextView textView = new TextView(getContext());
                textView.setText(list.get(j).toString());
                textView.setTextSize(15);
                if (j==tableMainRow.getChildCount()-4){
                    textView.setTypeface(null, Typeface.BOLD);
                    textView.setPadding(10, 0, 10, 0);
                }
                else {
                    textView.setPadding(10, 0, 10, 0);
                }


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