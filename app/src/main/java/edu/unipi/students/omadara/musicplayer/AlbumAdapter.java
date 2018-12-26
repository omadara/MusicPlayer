package edu.unipi.students.omadara.musicplayer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private List<Album> albumList;

    public AlbumAdapter(List<Album> albumList) {
        this.albumList = albumList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int layoutId) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.album_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Album a = albumList.get(position);
        viewHolder.album = a;
        viewHolder.title.setText(a.getTitle());
        viewHolder.artist.setText(a.getArtist());
        viewHolder.duration.setText(a.getDuration());
        viewHolder.songs_count.setText(a.getSongs_cnt());
        viewHolder.image.setImageResource(R.drawable.pink_floyd_album_cover); //TODO download bitmap from earbits
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Album album;
        public final TextView title;
        public final TextView artist;
        public final TextView songs_count;
        public final TextView duration;
        public final ImageView image;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            artist = (TextView) view.findViewById(R.id.artist);
            songs_count = (TextView) view.findViewById(R.id.songs_count);
            duration = (TextView) view.findViewById(R.id.duration);
            image = (ImageView) view.findViewById(R.id.album_img);
        }
    }

}
