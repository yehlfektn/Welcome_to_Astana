package com.nurdaulet.project.Sightseeings;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nurdaulet.project.R;
import com.nurdaulet.project.Sightseeings.MyAdapter;

import java.lang.reflect.Field;

/**
 * Created by nurdaulet on 5/3/17.
 */

public class SightSeeingsFragment extends Fragment {

    public TabLayout tabLayout;
    public ViewPager viewPager;

    public SightSeeingsFragment() {
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
    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("SightSeeings","OnDetach()");
        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
            Log.d("SightSeeings","ChildWasCleared");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
