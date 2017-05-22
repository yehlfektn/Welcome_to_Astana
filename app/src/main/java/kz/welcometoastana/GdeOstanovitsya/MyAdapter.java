package kz.welcometoastana.GdeOstanovitsya;

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
                return new AllHotels();
            case 1:
                return new Hotels();
            case 2:
                return new Hostels();
            case 3:
                return new Apartments();
        }
        return null;
    }

    @Override
    public int getCount() {

        return 4;

    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Все отели";
            case 1:
                return "Гостиницы";
            case 2:
                return "Хостелы";
            case 3:
                return "Апартаменты";


        }

        return null;
    }
}
