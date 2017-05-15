package com.nurdaulet.project.GdeOstanovitsya;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nurdaulet.project.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GdeOstanovitsya extends Fragment {

    public TabLayout tabLayout;
    public ViewPager viewPager;

    public GdeOstanovitsya() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_gde_ostanovitsya, container, false);

        tabLayout = (TabLayout) v.findViewById(R.id.tabsHotel);
        viewPager = (ViewPager) v.findViewById(R.id.viewpagerHotel);
        //set an adpater

        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
        int position = getArguments().getInt("position");
        viewPager.setCurrentItem(position);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return v;
    }

}
