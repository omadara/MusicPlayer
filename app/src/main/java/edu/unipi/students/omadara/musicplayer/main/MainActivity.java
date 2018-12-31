package edu.unipi.students.omadara.musicplayer.main;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import edu.unipi.students.omadara.musicplayer.R;
import edu.unipi.students.omadara.musicplayer.albums.AlbumTracksFragment;
import edu.unipi.students.omadara.musicplayer.albums.AlbumsFragment;
import edu.unipi.students.omadara.musicplayer.genres.Genre;
import edu.unipi.students.omadara.musicplayer.genres.GenresFragment;
import edu.unipi.students.omadara.musicplayer.models.Album;
import edu.unipi.students.omadara.musicplayer.models.Track;


public class MainActivity extends AppCompatActivity implements AlbumsFragment.AlbumEventListener, AlbumTracksFragment.TrackEventListener,
        View.OnClickListener, SeekBar.OnSeekBarChangeListener, GenresFragment.GenreEventListener {
    private TabsFragment tabsFragment;
    private ViewGroup playerContainer;
    private TextView tvTrackName, tvCurrentTime, tvDuration;
    private Button btnPlayPause, btnStop;

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private Handler seekBarUpdateHandler = new Handler();
    private Runnable seekBarUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            float completionRatio = (float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
            seekBar.setProgress((int)(completionRatio * seekBar.getMax()));
            seekBarUpdateHandler.postDelayed(this, 1000);
            tvCurrentTime.setText(Utils.durationToString(mediaPlayer.getCurrentPosition()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0); //remove actionbar shadow
        setContentView(R.layout.activity_main);
        tabsFragment = new TabsFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragmentContainer, tabsFragment, "TAG_TABS").commit();
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
        if (f != null && f.isVisible()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            setTitle(R.string.app_name);
            setActionBarShadow(0);
        } else {
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
        AlbumTracksFragment fragment = new AlbumTracksFragment();
        fragment.setAlbum(album);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragmentContainer, fragment)
                .addToBackStack(null).commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setActionBarShadow(10);
    }

    private void setActionBarShadow(int dp) {
        getSupportActionBar().setElevation(dp * getResources().getDisplayMetrics().density);
    }

    @Override
    public void onTrackClick(Track track) {
        playerContainer.setVisibility(View.VISIBLE);
        seekBar.setProgress(0);
        tvTrackName.setText(track.getName());
        tvCurrentTime.setText(Utils.durationToString(0));
        tvDuration.setText(Utils.durationToString(track.getDuration()));
        if(mediaPlayer == null) {
            initMediaPlayer();
        }else{
            seekBarUpdateHandler.removeCallbacks(seekBarUpdateRunnable);
            mediaPlayer.reset();
        }
        preparePlayer(track.getMediaUrl());
    }

    @Override
    public void onGenreClick(Genre genre) {
        RestClient.getInstance(this).requestGenreTrack(genre.getId(), new RestClient.Callback<Track>() {
            @Override
            public void onRequestFinished(Track track, boolean isLast) {
                onTrackClick(track);
            }
        }, null);
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        AudioAttributes.Builder builder = new AudioAttributes.Builder();
        builder.setContentType(AudioAttributes.CONTENT_TYPE_MUSIC);
        builder.setUsage(AudioAttributes.USAGE_MEDIA);
        mediaPlayer.setAudioAttributes(builder.build());
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                resumePlayer();
            }
        });
    }

    private void preparePlayer(String mediaUrl) {
        try {
            mediaPlayer.setDataSource(mediaUrl);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e("musicplayer", "IOException while attempting to play from media url.", e);
        }
    }

    private void resumePlayer() {
        btnPlayPause.setBackgroundResource(R.drawable.ic_pause);
        playerContainer.setBackgroundResource(R.color.playerBackgroundPlayingColor);
        mediaPlayer.start();
        seekBarUpdateHandler.post(seekBarUpdateRunnable);
    }

    private void pausePlayer() {
        btnPlayPause.setBackgroundResource(R.drawable.ic_play);
        playerContainer.setBackgroundResource(R.color.playerBackgroundPausedColor);
        mediaPlayer.pause();
        seekBarUpdateHandler.removeCallbacks(seekBarUpdateRunnable);
    }

    private void closePlayer() {
        playerContainer.setVisibility(View.GONE);
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        seekBarUpdateHandler.removeCallbacks(seekBarUpdateRunnable);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonPlayPause) {
            if (!mediaPlayer.isPlaying()) {
                resumePlayer();
            } else {
                pausePlayer();
            }
        } else if (v.getId() == R.id.buttonStop) {
            closePlayer();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(!fromUser) return;
        float completionRatio = (float) progress / seekBar.getMax();
        mediaPlayer.seekTo((int)(completionRatio * mediaPlayer.getDuration()));
        tvCurrentTime.setText(Utils.durationToString(mediaPlayer.getCurrentPosition()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onPause() {
        super.onPause();
        if(mediaPlayer != null) pausePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mediaPlayer != null) resumePlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null) closePlayer();
    }

}