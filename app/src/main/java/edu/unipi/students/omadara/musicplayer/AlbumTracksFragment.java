package edu.unipi.students.omadara.musicplayer;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class AlbumTracksFragment extends Fragment {
    private Album album;
    private RecyclerView recyclerView;

    //TODO track click interface

    public AlbumTracksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_tracks, container, false);

        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.tracks_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        Bundle args = getArguments();
        album = new Album();
        album.setId(args.getString("id"));
        album.setTitle(args.getString("title"));
        album.setArtist(args.getString("artist"));
        getActivity().setTitle(album.getTitle());

        // Bind album to top_album_item using AlbumAdapter
        List<Album> albumlist = new ArrayList<Album>();
        AlbumAdapter albumadapter = new AlbumAdapter(albumlist, null);
        AlbumAdapter.ViewHolder top_album_item = albumadapter.new ViewHolder(view.findViewById(R.id.album));
        albumlist.add(album);
        albumadapter.onBindViewHolder(top_album_item, 0);

        List<Track> tracks = new ArrayList<Track>();
        recyclerView.setAdapter(new TrackAdapter(tracks));

        RestClient.getInstance(context).requestAlbumTracks(album.getId(), new RestClient.Callback<List<Track>>() {
            @Override
            public void onAllRequestsFinished(List<Track> tracks) {
                //
            }

            @Override
            public void onRequestFinished(List<Track> tracks) {
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        }, tracks, null);

        return view;
    }

}
