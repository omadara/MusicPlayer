package edu.unipi.students.omadara.musicplayer;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements AlbumsFragment.OnAlbumsFragmentInteractionListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private ViewGroup playerContainer;
    private TextView tvTrackName, tvCurrentTime, tvDuration;
    private Button btnPlayPause, btnStop;
    private SeekBar seekBar;
    private boolean playing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0); //remove actionbar shadow
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragmentContainer, new TabsFragment(), "TAG_TABS").commit();
        tvTrackName = findViewById(R.id.tvTrackName);
        tvCurrentTime = findViewById(R.id.tvCurrentTime);
        tvDuration = findViewById(R.id.tvDuration);
        playerContainer = findViewById(R.id.playerContainer);
        btnPlayPause = findViewById(R.id.buttonPlayPause);
        btnStop = findViewById(R.id.buttonStop);
        seekBar = findViewById(R.id.seekBar);
        btnPlayPause.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment f = getSupportFragmentManager().findFragmentByTag("TAG_TABS");
        if(f != null && f.isVisible()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            setTitle(R.string.app_name);
            setActionBarShadow(0);
        }else{
            setActionBarShadow(10);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onAlbumClick(Album album) {
        Fragment fragment = new AlbumTracksFragment();
        Bundle args = new Bundle();
        args.putString("id", album.getId());
        args.putString("title", album.getTitle());
        args.putString("artist", album.getArtist());
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragmentContainer, fragment)
                .addToBackStack(null).commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setActionBarShadow(10);
    }

    private void setActionBarShadow(int dp) {
        getSupportActionBar().setElevation(dp * getResources().getDisplayMetrics().density);
    }

    public void onTrackClick(Track track) {
        playerContainer.setVisibility(View.VISIBLE);
        btnPlayPause.setBackgroundResource(R.drawable.ic_pause);
        playerContainer.setBackgroundResource(R.color.playerBackgroundPlayingColor);
        seekBar.setProgress(0);
        tvTrackName.setText(track.getName());
        tvCurrentTime.setText("0:00");
        tvDuration.setText(String.format("%d:%02d", track.getDuration()));
        playing = true;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonPlayPause) {
            playing = !playing;
            if (playing) {
                btnPlayPause.setBackgroundResource(R.drawable.ic_pause);
                playerContainer.setBackgroundResource(R.color.playerBackgroundPlayingColor);
            } else {
                btnPlayPause.setBackgroundResource(R.drawable.ic_play);
                playerContainer.setBackgroundResource(R.color.playerBackgroundPausedColor);
            }
        }else if(v.getId() == R.id.buttonStop){
            playerContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
