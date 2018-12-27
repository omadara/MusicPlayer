package edu.unipi.students.omadara.musicplayer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.unipi.students.omadara.musicplayer.AlbumsFragment.OnAlbumsFragmentInteractionListener;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private List<Album> albumList;
    private OnAlbumsFragmentInteractionListener mListener;

    public AlbumAdapter(List<Album> albumList, OnAlbumsFragmentInteractionListener mListener) {
        this.albumList = albumList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int layoutId) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.album_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        Album a = albumList.get(position);
        viewHolder.album = a;
        viewHolder.title.setText(a.getTitle());
        viewHolder.artist.setText(a.getArtist());
        viewHolder.image.setImageBitmap(a.getThumbnail());
        viewHolder.songs_count.setText(a.getTrackCount() + " songs");
        int duration = a.getDuration() / 1000;
        viewHolder.duration.setText(String.format("%d:%02d", duration / 60, duration % 60));

        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onAlbumClick(viewHolder.album);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Album album;
        public final View mView;
        public final TextView title;
        public final TextView artist;
        public final TextView songs_count;
        public final TextView duration;
        public final ImageView image;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            title = (TextView) view.findViewById(R.id.title);
            artist = (TextView) view.findViewById(R.id.artist);
            songs_count = (TextView) view.findViewById(R.id.songs_count);
            duration = (TextView) view.findViewById(R.id.duration);
            image = (ImageView) view.findViewById(R.id.album_img);
        }
    }

}
