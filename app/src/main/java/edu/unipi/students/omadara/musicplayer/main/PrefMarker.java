package edu.unipi.students.omadara.musicplayer.main;

import android.view.MotionEvent;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class PrefMarker extends Marker {
    protected OnPrefMarkerClickListener mOnPrefMarkerClickListener;

    public PrefMarker(MapView mapView) {
        super(mapView);
        mOnPrefMarkerClickListener = null;
    }

    public interface OnPrefMarkerClickListener {
        abstract boolean onPrefMarkerLongClick(Marker marker, MapView mapView);
        abstract boolean onPrefMarkerClick(Marker marker, MapView mapView);
    }

    @Override
    public boolean onLongPress(final MotionEvent event, final MapView mapView) {
        boolean touched = hitTest(event, mapView);
        if (touched) {
            if (mOnPrefMarkerClickListener != null)
                return mOnPrefMarkerClickListener.onPrefMarkerLongClick(this, mapView);
        }
        return touched;
    }

    public void setOnPrefMarkerClickListener(final OnPrefMarkerClickListener listener) {
        mOnPrefMarkerClickListener = listener;
        if (listener != null) {
            this.setOnMarkerClickListener(new OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    return listener.onPrefMarkerClick(marker, mapView);
                }
            });
        }
    }

    @Override
    public void onDetach(MapView mapView) {
        this.mOnPrefMarkerClickListener = null;
        super.onDetach(mapView);
    }
}