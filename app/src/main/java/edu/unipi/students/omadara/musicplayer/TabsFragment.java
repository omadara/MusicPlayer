package edu.unipi.students.omadara.musicplayer;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class TabsFragment extends Fragment {

    public TabsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabs, container, false);
        initTabLayout(view);
        return view;
    }

    private void initTabLayout(View view) {
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager pager = view.findViewById(R.id.viewPager);
        pager.setOffscreenPageLimit(2);//fortwnei mexri kai 2 geitonika tabs
        pager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(pager);
        tabLayout.getTabAt(0).setText(R.string.tabItemAlbums);
        tabLayout.getTabAt(1).setText(R.string.tabItemArtists);
        tabLayout.getTabAt(2).setText(R.string.tabItemPlaylists);
    }

}
