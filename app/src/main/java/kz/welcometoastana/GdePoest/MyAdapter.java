package kz.welcometoastana.GdePoest;

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
                return new AllPlacesFragment();
            case 1:
                return new CafeFragment();
            case 2:
                return new RestaurantFragment();
            case 3:
                return new BarsFragments();
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
                return "Все заведения";
            case 1:
                return "Кафе";
            case 2:
                return "Рестораны";
            case 3:
                return "Бары";


        }

        return null;
    }
}
