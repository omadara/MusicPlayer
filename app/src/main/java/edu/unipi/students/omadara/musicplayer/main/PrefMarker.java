package edu.unipi.students.omadara.musicplayer.main;

import android.view.MotionEvent;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polygon;

public class PrefMarker extends Marker {
    protected ClickListener mOnPrefMarkerClickListener;
    private Overlay circle;

    public PrefMarker(MapView mapView) {
        super(mapView);
        mOnPrefMarkerClickListener = null;
    }

    public void setCircle(Overlay circle) {
        this.circle = circle;
    }

    public Overlay getCircle() {
        return circle;
    }

    public interface ClickListener {
        boolean onPrefMarkerLongClick(PrefMarker marker, MapView mapView);
        boolean onPrefMarkerClick(PrefMarker marker, MapView mapView);
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

    public void setOnPrefMarkerClickListener(final ClickListener listener) {
        mOnPrefMarkerClickListener = listener;
        if (listener != null) {
            this.setOnMarkerClickListener(new OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker, MapView mapView) {
                    return listener.onPrefMarkerClick(PrefMarker.this, mapView);
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