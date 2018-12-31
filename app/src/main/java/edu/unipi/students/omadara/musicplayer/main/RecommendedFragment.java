package edu.unipi.students.omadara.musicplayer.main;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import edu.unipi.students.omadara.musicplayer.R;
import edu.unipi.students.omadara.musicplayer.genres.Genre;


public class RecommendedFragment extends Fragment implements View.OnClickListener {
    private Spinner genreDropDown;
    private ViewGroup layoutAddRec;

    public RecommendedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommended, container, false);
        genreDropDown = view.findViewById(R.id.genreDropDown);

        RestClient.getInstance(this.getContext()).requestGenres(new RestClient.Callback<Genre>() {
            private List<String> genreListString = new ArrayList<>();
            @Override
            public void onRequestFinished(Genre genre, boolean isLast) {
                genreListString.add(genre.getName());
                if(isLast) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, genreListString);
                    genreDropDown.setAdapter(adapter);
                }
            }
        }, false, null);

        layoutAddRec = view.findViewById(R.id.layoutAddRecommendation);
        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        layoutAddRec.setVisibility(layoutAddRec.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }
}
