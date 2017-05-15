package com.nurdaulet.project.Events;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.nurdaulet.project.R;

/**
 * Created by nurdaulet on 5/3/17.
 */

public class EventsFragment extends Fragment {
    public TabLayout tabLayout;
    public ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.events_layout, null);

        tabLayout = (TabLayout) v.findViewById(R.id.tabsEvent);
        viewPager = (ViewPager) v.findViewById(R.id.viewpagerEvent);
        //set an adpater

        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });


        return v;
    }

}
