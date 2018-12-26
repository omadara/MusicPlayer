package edu.unipi.students.omadara.musicplayer;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0); //remove actionbar shadow
        initTabLayout();

        RestClient.getInstance(this).getAlbums(new RestClient.Callback<List<Album>>() {
            @Override
            public void onCall(List<Album> albums) {
                for(Album album : albums) {
                    Log.i("musicplayer test", album.toString());
                }
            }
        });
    }

    private void initTabLayout() {
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager pager = findViewById(R.id.viewPager);
        pager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(pager);
        tabLayout.getTabAt(0).setText(R.string.tabItemAlbums);
        tabLayout.getTabAt(1).setText(R.string.tabItemArtists);
        tabLayout.getTabAt(2).setText(R.string.tabItemPlaylists);
    }

}
