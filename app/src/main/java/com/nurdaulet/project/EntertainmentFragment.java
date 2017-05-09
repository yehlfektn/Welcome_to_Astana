package com.nurdaulet.project;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nurdaulet.project.R;
import com.nurdaulet.project.Sightseeings.MyAdapter;

/**
 * Created by nurdaulet on 5/3/17.
 */

public class EntertainmentFragment extends Fragment {

    public static int int_items = 3;
    public TabLayout tabLayout;
    public ViewPager viewPager;

    public EntertainmentFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.sightseeings_layout, container, false);

        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
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
