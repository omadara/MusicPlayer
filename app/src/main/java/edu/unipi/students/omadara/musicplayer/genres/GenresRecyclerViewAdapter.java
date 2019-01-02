package edu.unipi.students.omadara.musicplayer.genres;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.unipi.students.omadara.musicplayer.R;
import edu.unipi.students.omadara.musicplayer.models.Genre;

public class GenresRecyclerViewAdapter extends RecyclerView.Adapter<GenresRecyclerViewAdapter.ViewHolder> {
    private List<Genre> genreList = new ArrayList<>();
    private GenresFragment.GenreEventListener mListener;
    private View selected;

    public GenresRecyclerViewAdapter(GenresFragment.GenreEventListener mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public GenresRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int layoutId) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.genres_grid_cell, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GenresRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        Genre genre = genreList.get(position);
        viewHolder.genre = genre;
        viewHolder.image.setImageBitmap(genre.getThumbnail());
        viewHolder.name.setText(genre.getName());
        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null) mListener.onGenreClick(viewHolder.genre);
                if(selected != null) selected.setBackgroundResource(android.R.color.white);
                selected = viewHolder.mView;
                selected.setBackgroundResource(R.color.selectedTrackColor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    public void addGenre(Genre genre) {
        genreList.add(genre);
        notifyItemInserted(genreList.size() - 1);
    }

    public List<Genre> getGenreList() {
        return genreList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView image;
        public final TextView name;
        public Genre genre;

        public ViewHolder(@NonNull View view) {
            super(view);
            mView = view;
            image = view.findViewById(R.id.genre_img);
            name = view.findViewById(R.id.genre_name);
        }
    }
}
