package com.nurdaulet.project.Events;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

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
                return new AllEvents();
            case 1:
                return new Concerts();
            case 2:
                return new Vystavki();
            case 3:
                return new Theatre();
            case 4:
                return new Sports();
            case 5:
                return new ExpoEvent();

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
                return "Все события";
            case 1:
                return "Концерты";
            case 2:
                return "Выставки";
            case 3:
                return "Театральная постановка";
            case 4:
                return "Спортивные мероприятия";
            case 5:
                return "Мероприятия Expo";


        }

        return null;
    }
}
