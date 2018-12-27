package edu.unipi.students.omadara.musicplayer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;


public class AlbumsFragment extends Fragment {
    private RecyclerView recyclerView;
    private OnAlbumsFragmentInteractionListener mListener;

    public AlbumsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums, container, false);

        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.albums_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        final ProgressBar loading = view.findViewById(R.id.progressBar);
        RestClient.getInstance(this.getContext()).requestAlbums(new RestClient.Callback<List<Album>>() {
            @Override
            public void onCall(List<Album> albums) {
                loading.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(new AlbumAdapter(albums, mListener));
            }
        }, 3, null);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAlbumsFragmentInteractionListener) {
            mListener = (OnAlbumsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAlbumsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnAlbumsFragmentInteractionListener {
        void onAlbumClick(Album album);
    }
}
