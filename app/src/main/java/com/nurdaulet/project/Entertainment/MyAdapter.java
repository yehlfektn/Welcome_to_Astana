package com.nurdaulet.project.Entertainment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nurdaulet.project.Sightseeings.AllSightseeings;
import com.nurdaulet.project.Sightseeings.Architecture_Fragment;
import com.nurdaulet.project.Sightseeings.CultureFragment;
import com.nurdaulet.project.Sightseeings.DosugFragment;
import com.nurdaulet.project.Sightseeings.DuhovnostFragment;
import com.nurdaulet.project.Sightseeings.HistoryFragment;
import com.nurdaulet.project.Sightseeings.InterestingPlacesFragment;

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
                return new AllSightseeings();
            case 1:
                return new CultureFragment();
            case 2:
                return new HistoryFragment();
            case 3:
                return new DuhovnostFragment();
            case 4:
                return new DosugFragment();
            case 5:
                return new InterestingPlacesFragment();
            case 6:
                return new Architecture_Fragment();


        }
        return null;
    }

    @Override
    public int getCount() {

        return 7;

    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Все объекты";
            case 1:
                return "Культура";
            case 2:
                return "История";
            case 3:
                return "Духовность";
            case 4:
                return "Досуг";
            case 5:
                return "Интересные Места";
            case 6:
                return "Архитектура";

        }

        return null;
    }
}
