package edu.unipi.students.omadara.musicplayer.genres;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import edu.unipi.students.omadara.musicplayer.R;
import edu.unipi.students.omadara.musicplayer.albums.AlbumAdapter;
import edu.unipi.students.omadara.musicplayer.albums.AlbumsFragment;
import edu.unipi.students.omadara.musicplayer.main.RestClient;


public class GenresFragment extends Fragment {
    public interface GenreEventListener {
        void onGenreClick(Genre genre);
    }
    private GenresRecyclerViewAdapter adapter;
    private GenreEventListener mListener;

    public GenresFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_progressbar, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        adapter = new GenresRecyclerViewAdapter(mListener);
        recyclerView.setAdapter(adapter);

        final ProgressBar loading = view.findViewById(R.id.progressBar);
        RestClient.getInstance(this.getContext()).requestGenres(new RestClient.Callback<Genre>() {
            @Override
            public void onRequestFinished(Genre genre, boolean isLast) {
                adapter.addGenre(genre);
                if(isLast) loading.setVisibility(View.INVISIBLE);
            }
        }, null);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GenreEventListener) {
            mListener = (GenreEventListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement " + GenreEventListener.class.getCanonicalName());
        }
    }

}
