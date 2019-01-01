package edu.unipi.students.omadara.musicplayer.main;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.unipi.students.omadara.musicplayer.R;
import edu.unipi.students.omadara.musicplayer.albums.AlbumTracksFragment;
import edu.unipi.students.omadara.musicplayer.albums.TrackAdapter;
import edu.unipi.students.omadara.musicplayer.genres.Genre;
import edu.unipi.students.omadara.musicplayer.models.Track;

import static android.content.Context.LOCATION_SERVICE;


public class RecommendedFragment extends Fragment implements View.OnClickListener {
    private static final int REQUEST_PERMISSION_CODE_GPS = 12345;
    private static final int MIN_DISTANCE = 100;
    private Spinner genreDropDown;
    private ViewGroup layoutAddRec;
    private EditText latlon;
    private SQLiteDatabase db; 
    private TextView prompt, tvGenre;
    private RecyclerView rvTrackList;
    private TrackAdapter trackAdapter;
    private Map<String, String> genresIDs;

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
        rvTrackList = view.findViewById(R.id.recyclerView);
        rvTrackList.setLayoutManager(new LinearLayoutManager(getContext()));
        trackAdapter = new TrackAdapter((AlbumTracksFragment.TrackEventListener)getActivity());
        rvTrackList.setAdapter(trackAdapter);
        fab.setOnClickListener(this);
        buttonAdd.setOnClickListener(this);
        genresIDs = new HashMap<>();

        initDropDownAsync();
        initDatabase();

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
        //elenxos an exei e3ousiodoth8ei me to location permission
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        }else{ //an oxi rwtaei to xrhsth (to apotelesma to pairnei apo th me8odo onRequestPermissionsResult())
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_CODE_GPS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode == REQUEST_PERMISSION_CODE_GPS) {
            //apodexthke to aithma
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocation();
            }else{ //aperripse to aithma
                Toast.makeText(getActivity(), getString(R.string.gpsPermissionDenied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestLocation() throws SecurityException {
        LocationManager locationManager = (LocationManager)getContext().getSystemService(LOCATION_SERVICE);
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
            tvGenre.setText("");
            prompt.setText(getString(R.string.noRecommendedGenrePrompt));
        }else{
            tvGenre.setText(genreName);
            String genreId = genresIDs.get(genreName);
            for(int i = 0; i < 10; i++) {
                addTrackToRecyclerView(genreId);
            }
        }
    }

    private void addTrackToRecyclerView(String genreId) {
        RestClient.getInstance(getContext()).requestGenreTrack(genreId, new RestClient.Callback<Track>() {
            @Override
            public void onRequestFinished(Track track, boolean isLast) {
                trackAdapter.addTrack(track);
            }
        }, null);
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
