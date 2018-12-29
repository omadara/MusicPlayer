package edu.unipi.students.omadara.musicplayer.genres;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.unipi.students.omadara.musicplayer.R;


public class GenresFragment extends Fragment {

    public GenresFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_genres, container, false);
    }

}
