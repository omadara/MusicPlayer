package edu.unipi.students.omadara.musicplayer.albums;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.unipi.students.omadara.musicplayer.R;
import edu.unipi.students.omadara.musicplayer.albums.AlbumsFragment.AlbumEventListener;
import edu.unipi.students.omadara.musicplayer.main.Utils;
import edu.unipi.students.omadara.musicplayer.models.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private List<Album> albumList;
    private AlbumEventListener mListener;

    public AlbumAdapter(AlbumEventListener mListener) {
        this.mListener = mListener;
        this.albumList = new ArrayList<Album>();
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
        viewHolder.duration.setText(Utils.durationToString(a.getDuration()));

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

    public void addAlbum(Album album) {
        albumList.add(album);
        notifyItemInserted(albumList.size() - 1);
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
