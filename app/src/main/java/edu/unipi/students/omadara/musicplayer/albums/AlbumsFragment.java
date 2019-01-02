package edu.unipi.students.omadara.musicplayer.albums;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import edu.unipi.students.omadara.musicplayer.R;
import edu.unipi.students.omadara.musicplayer.main.RestClient;
import edu.unipi.students.omadara.musicplayer.models.Album;


public class AlbumsFragment extends Fragment {
    public interface AlbumEventListener {
        void onAlbumClick(Album album);
    }
    private AlbumAdapter adapter;
    private AlbumEventListener mListener;

    public AlbumsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_progressbar, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new AlbumAdapter(mListener);
        recyclerView.setAdapter(adapter);

        final ProgressBar loading = view.findViewById(R.id.progressBar);
        RestClient.getInstance(this.getContext()).requestAlbums(new RestClient.Callback<Album>() {
            @Override
            public void onRequestFinished(Album album, boolean isLast) {
                adapter.addAlbum(album);
                if(isLast) loading.setVisibility(View.INVISIBLE);
            }
        }, 30, null);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AlbumEventListener) {
            mListener = (AlbumEventListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AlbumEventListener");
        }
    }

}
