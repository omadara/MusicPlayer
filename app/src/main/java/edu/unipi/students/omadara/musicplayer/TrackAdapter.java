package edu.unipi.students.omadara.musicplayer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {
    private List<Track> trackList;

    public TrackAdapter(List<Track> trackList) {
        this.trackList = trackList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int layoutId) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.track_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Track t = trackList.get(position);
        viewHolder.track = t;
        viewHolder.name.setText(t.getName());
        viewHolder.duration.setText(Utils.durationToString(t.getDuration()));
        //viewHolder.image.....

        //TODO click listeners interface
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Track track;
        public final View mView;
        public final TextView name;
        public final TextView duration;
        public final ImageView image;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            name = (TextView) view.findViewById(R.id.name);
            duration = (TextView) view.findViewById(R.id.duration);
            image = (ImageView) view.findViewById(R.id.track_img);
        }
    }
}
