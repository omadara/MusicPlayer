package edu.unipi.students.omadara.musicplayer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class AlbumsFragment extends Fragment {
    private RecyclerView recyclerView;

    public AlbumsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums, container, false);

        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.albums_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        //TODO earbits requests
        List<Album> albumList = new ArrayList<Album>();
        albumList.add(new Album("Test Title", "Test Artist", "33:33", "3 songs", R.drawable.ic_launcher_foreground));
        albumList.add(new Album("New Album", "ArrrrTttttsssst", "133:33", "33 songs", R.drawable.ic_launcher_foreground));
        albumList.add(new Album("Title Title", "5 Artist", "33:33", "6 songs", R.drawable.ic_launcher_foreground));
        albumList.add(new Album("TTT Title", "Earbits", "13:11", "13 songs", R.drawable.ic_launcher_foreground));
        albumList.add(new Album("RRR Title", "Test", "11:35", "23 songs", R.drawable.ic_launcher_foreground));

        recyclerView.setAdapter(new AlbumAdapter(albumList));

        return view;
    }
}
