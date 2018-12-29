package edu.unipi.students.omadara.musicplayer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {
    private List<Track> trackList;

    public TrackAdapter() {
        this.trackList = new ArrayList<Track>();
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

        //TODO click listeners interface
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public void addTrack(Track track) {
        trackList.add(track);
        notifyItemInserted(trackList.size() - 1);
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
