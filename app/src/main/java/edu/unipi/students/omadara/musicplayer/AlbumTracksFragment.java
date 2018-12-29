package edu.unipi.students.omadara.musicplayer;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class AlbumTracksFragment extends Fragment {
    public interface TrackEventListener {
        void onTrackClick(Track track);
    }
    private TrackAdapter adapter;
    private Album album;
    private TrackEventListener mListener;

    public AlbumTracksFragment() {
        // Required empty public constructor
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_tracks, container, false);

        getActivity().setTitle(album.getTitle());
        ViewGroup albumOnTop = view.findViewById(R.id.album);
        ((TextView)albumOnTop.findViewById(R.id.title)).setText(album.getTitle());
        ((TextView)albumOnTop.findViewById(R.id.artist)).setText(album.getArtist());
        ((TextView)albumOnTop.findViewById(R.id.songs_count)).setText(album.getTrackCount() + " songs");
        ((TextView)albumOnTop.findViewById(R.id.duration)).setText(Utils.durationToString(album.getDuration()));
        ((ImageView)albumOnTop.findViewById(R.id.album_img)).setImageBitmap(album.getThumbnail());

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.tracks_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new TrackAdapter(mListener);
        recyclerView.setAdapter(adapter);

        RestClient.getInstance(context).requestAlbumTracks(album.getId(), new RestClient.Callback<Track>() {
            @Override
            public void onRequestFinished(Track track, boolean isLast) {
                adapter.addTrack(track);
            }
        }, null);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TrackEventListener) {
            mListener = (TrackEventListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement TrackEventListener");
        }
    }

}
