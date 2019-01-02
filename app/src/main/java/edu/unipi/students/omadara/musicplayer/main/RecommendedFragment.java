package edu.unipi.students.omadara.musicplayer.main;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.unipi.students.omadara.musicplayer.R;
import edu.unipi.students.omadara.musicplayer.albums.AlbumTracksFragment;
import edu.unipi.students.omadara.musicplayer.albums.TrackAdapter;
import edu.unipi.students.omadara.musicplayer.models.Genre;
import edu.unipi.students.omadara.musicplayer.models.Track;

import static android.content.Context.LOCATION_SERVICE;


public class RecommendedFragment extends Fragment implements View.OnClickListener {
    private static final int REQUEST_PERMISSIONS_CODE = 12345;
    private static final int ENABLE_GPS_CODE = 1234;
    private static final int MIN_DISTANCE = 100;
    private Spinner genreDropDown;
    private ViewGroup layoutAddRec;
    private EditText latlon;
    private SQLiteDatabase db;
    private TextView prompt, tvGenre;
    private RecyclerView rvTrackList;
    private TrackAdapter trackAdapter;
    private Map<String, String> genresIDs;
    private ProgressBar loading;
    private MapView mapView;
    private Marker clickMarker;
    private Polygon clickMarkerRange;

    public RecommendedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommended, container, false);
        genreDropDown = view.findViewById(R.id.genreDropDown);
        prompt = view.findViewById(R.id.prompt);
        tvGenre = view.findViewById(R.id.textView2);
        FloatingActionButton fab = view.findViewById(R.id.floatingActionButton);
        Button buttonAdd = view.findViewById(R.id.buttonAdd);
        layoutAddRec = view.findViewById(R.id.layoutRecAdd);
        latlon = view.findViewById(R.id.editTextLatLon);
        loading = view.findViewById(R.id.progressBar);
        rvTrackList = view.findViewById(R.id.recyclerView);
        rvTrackList.setLayoutManager(new LinearLayoutManager(getContext()));
        trackAdapter = new TrackAdapter((AlbumTracksFragment.TrackEventListener)getActivity());
        rvTrackList.setAdapter(trackAdapter);
        fab.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);
        genresIDs = new HashMap<>();

        initDropDownAsync();
        initDatabase();

        mapView = (MapView) view.findViewById(R.id.osmdroid);
        mapView.setMultiTouchControls(true);
        mapView.setTilesScaledToDpi(true);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.getController().setZoom(8.0);
        mapView.getController().setCenter(new GeoPoint(37.941649, 23.652894));

        clickMarker = new Marker(mapView);
        clickMarker.setPosition(new GeoPoint(-20.66, -20.66));
        clickMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        clickMarker.setIcon(getResources().getDrawable(R.drawable.ic_menu_mylocation));

        clickMarkerRange = new Polygon();
        clickMarkerRange.setPoints(Polygon.pointsAsCircle(new GeoPoint(-20.66, -20.66), MIN_DISTANCE));
        clickMarkerRange.setFillColor(Color.argb(35, 48, 79, 254));
        clickMarkerRange.setStrokeColor(Color.argb(0,0,0,0));

        mapView.getOverlays().add(clickMarker);
        mapView.getOverlays().add(clickMarkerRange);

        mapView.getOverlays().add(new MapEventsOverlay(new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                latlon.setText(p.toDoubleString());
                clickMarker.setPosition(p);
                clickMarkerRange.setPoints(Polygon.pointsAsCircle(p, MIN_DISTANCE));
                // mapView.getController().setCenter(p); // move to click position
                mapView.invalidate();
                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        }));

        return view;
    }

    private void initDatabase() {
        db = getActivity().openOrCreateDatabase("db", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS pref(genre VARCHAR, lat REAL, lon REAL);");
    }

    private void initDropDownAsync() {
        RestClient.getInstance(this.getContext()).requestGenres(new RestClient.Callback<Genre>() {
            private List<String> genreListString = new ArrayList<>();
            @Override
            public void onRequestFinished(Genre genre, boolean isLast) {
                genresIDs.put(genre.getName(), genre.getId());
                genreListString.add(genre.getName());
                if(isLast) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, genreListString);
                    genreDropDown.setAdapter(adapter);
                    searchForRecommendations();
                }
            }
        }, false, null);
    }

    private void searchForRecommendations() {
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        }else{
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == REQUEST_PERMISSIONS_CODE) {
            if(grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                requestLocation();
            }else{
                Toast.makeText(getActivity(), getString(R.string.gpsPermissionDenied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestLocation() throws SecurityException {
        LocationManager locationManager = (LocationManager)getContext().getSystemService(LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }else{
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    gotLocation(location);
                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) { }
                @Override
                public void onProviderEnabled(String provider) { }
                @Override
                public void onProviderDisabled(String provider) { }
            }, null);
        }
    }

    private void buildAlertMessageNoGps() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.turn_on_gps)
            .setCancelable(false)
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), ENABLE_GPS_CODE);
                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            }).create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ENABLE_GPS_CODE && resultCode == 0) {
            requestLocation();
        }
    }

    private void gotLocation(Location currentLoc) {
        try(Cursor c = db.rawQuery("SELECT genre,lat,lon FROM pref", null)) {
            Location loc = new Location(LocationManager.GPS_PROVIDER);
            String genre = null;
            while(c.moveToNext()) {
                genre = c.getString(0);
                loc.setLatitude(c.getDouble(1));
                loc.setLongitude(c.getDouble(2));
                float d = currentLoc.distanceTo(loc);
                if(d < MIN_DISTANCE) {
                    gotRecommendedGenre(genre);
                    return;
                }
            }
            gotRecommendedGenre(genre);
        }
    }

    private void gotRecommendedGenre(@Nullable String genreName) {
        if(genreName == null){
            prompt.setText(getString(R.string.noRecommendedGenrePrompt));
            tvGenre.setText("");
        }else{
            prompt.setText(R.string.recommended_genre_label);
            tvGenre.setText(genreName);
            final String genreId = genresIDs.get(genreName);
            RestClient.getInstance(getContext()).requestSubGenreTracks(genreId, new RestClient.Callback<Track>() {
                @Override
                public void onRequestFinished(Track track, boolean isLast) {
                    trackAdapter.addTrack(track);
                    if(isLast) loading.setVisibility(View.GONE);
                }
            }, null);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.floatingActionButton){
            layoutAddRec.setVisibility(layoutAddRec.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        }else if(v.getId() == R.id.buttonAdd) {
            String[] tokens = latlon.getText().toString().split("\\s*,\\s*");
            double lat, lon;
            try{
                lat = Double.parseDouble(tokens[0]);
                lon = Double.parseDouble(tokens[1]);
            }catch(NumberFormatException e){
                Toast.makeText(getActivity(), getString(R.string.invalidCoordinates), Toast.LENGTH_SHORT).show();
                return;
            }
            String genre = genreDropDown.getSelectedItem().toString();
            ContentValues contentValues = new ContentValues();
            contentValues.put("lat", lat);
            contentValues.put("lon", lon);
            contentValues.put("genre", genre);
            db.insert("pref",null, contentValues);
            Toast.makeText(getActivity(), "Genre '" + genre + "' added for the location.", Toast.LENGTH_SHORT).show();
        }
    }
}
