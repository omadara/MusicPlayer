package edu.unipi.students.omadara.musicplayer.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import edu.unipi.students.omadara.musicplayer.albums.AlbumsFragment;
import edu.unipi.students.omadara.musicplayer.genres.GenresFragment;


public class TabsFragmentPagerAdapter extends FragmentPagerAdapter {
    private final Fragment[] fragments = new Fragment[3];
    public TabsFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(fragments[position] == null) {
            switch (position) {
                case 0: fragments[position] = new AlbumsFragment(); break;
                case 1: fragments[position] = new GenresFragment(); break;
                case 2: fragments[position] = new RecommendedFragment(); break;
            }
        }
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
