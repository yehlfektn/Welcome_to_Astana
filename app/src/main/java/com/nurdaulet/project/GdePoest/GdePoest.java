package com.nurdaulet.project.GdePoest;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.nurdaulet.project.R;

import java.lang.reflect.Field;

/**
 * A simple {@link Fragment} subclass.
 */
public class GdePoest extends Fragment {


    public TabLayout tabLayout;
    public ViewPager viewPager;

    public GdePoest() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_gde_poest, container, false);

        tabLayout = (TabLayout) v.findViewById(R.id.tabsCafe);
        viewPager = (ViewPager) v.findViewById(R.id.viewpagerCafe);
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

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


}
