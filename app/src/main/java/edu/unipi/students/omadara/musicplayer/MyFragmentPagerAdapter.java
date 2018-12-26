package edu.unipi.students.omadara.musicplayer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private final Fragment[] fragments = new Fragment[3];
    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if(fragments[position] == null) {
            switch (position) {
                case 0: fragments[position] = new AlbumsFragment(); break;
                case 1: fragments[position] = new ArtistsFragment(); break;
                case 2: fragments[position] = new PlaylistsFragment(); break;
            }
        }
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
