package edu.unipi.students.omadara.musicplayer;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;


public class MainActivity extends AppCompatActivity implements AlbumsFragment.OnAlbumsFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0); //remove actionbar shadow
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragmentContainer, new TabsFragment()).commit();
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
    }
}
