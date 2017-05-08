package com.nurdaulet.project;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import static com.nurdaulet.project.EntertainmentFragment.int_items;

/**
 * Created by Nurdaulet on 3/1/2017.
 */

public class MyAdapter extends FragmentPagerAdapter {


    public MyAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AllEntertainment();
            case 1:
                return new CultureFragment();
            case 2:
                return new HistoryFragment();


        }
        return null;
    }

    @Override
    public int getCount() {


        return int_items;
    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Все объекты";
            case 1:
                return "Культура";
            case 2:
                return "История";


        }

        return null;
    }
}
