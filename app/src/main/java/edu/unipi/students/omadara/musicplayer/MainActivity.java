package edu.unipi.students.omadara.musicplayer;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity implements AlbumsFragment.OnAlbumsFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0); //remove actionbar shadow
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFragmentContainer, new TabsFragment(), "TAG_TABS").commit();
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

}
