package edu.unipi.students.omadara.musicplayer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class AlbumTracksFragment extends Fragment {
    private Album album;

    public AlbumTracksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        album = new Album();
        album.setId(args.getString("id"));
        album.setTitle(args.getString("title"));
        album.setArtist(args.getString("artist"));
        // TODO create xml to show list of album's songs
        return inflater.inflate(/*replace me*/ R.layout.fragment_artists, container, false);
    }

}
