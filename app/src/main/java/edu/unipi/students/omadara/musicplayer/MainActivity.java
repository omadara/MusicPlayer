package edu.unipi.students.omadara.musicplayer;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;


public class MainActivity extends AppCompatActivity implements AlbumsFragment.OnAlbumsFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0); //remove actionbar shadow
        initTabLayout();
    }

    private void initTabLayout() {
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager pager = findViewById(R.id.viewPager);
        pager.setOffscreenPageLimit(2);//fortwnei mexri kai 2 geitonika tabs
        pager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(pager);
        tabLayout.getTabAt(0).setText(R.string.tabItemAlbums);
        tabLayout.getTabAt(1).setText(R.string.tabItemArtists);
        tabLayout.getTabAt(2).setText(R.string.tabItemPlaylists);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onAlbumClick(Album album) {
        Snackbar.make(findViewById(R.id.viewPager), "Clicked album "
                + album.getTitle() + ". TODO open_musicplayer(album)", Snackbar.LENGTH_SHORT).show();
    }
}
