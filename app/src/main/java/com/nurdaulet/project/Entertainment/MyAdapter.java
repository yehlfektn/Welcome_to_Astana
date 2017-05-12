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
                return new TorgovyiFragment();
            case 1:
                return new ZonyOtdyhaFragment();
            case 2:
                return new BeachFragment();
            case 3:
                return new ClubsFragment();
            case 4:
                return new CinemaFragment();
            case 5:
                return new EnterDosug();
        }
        return null;
    }

    @Override
    public int getCount() {

        return 6;

    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Торговые центры";
            case 1:
                return "Зоны отдыха";
            case 2:
                return "Пляжи";
            case 3:
                return "Ночные клубы";
            case 4:
                return "Кинотеатры";
            case 5:
                return "Развлечения";


        }

        return null;
    }
}
