package kz.welcometoastana.GdePoest;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import kz.welcometoastana.R;

/**
 * Created by Nurdaulet on 3/1/2017.
 */

public class MyAdapter extends FragmentPagerAdapter {

    private Context context;

    public MyAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
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
                return context.getString(R.string.all_places_cafe);
            case 1:
                return context.getString(R.string.cafe);
            case 2:
                return context.getString(R.string.restourant);
            case 3:
                return context.getString(R.string.bar);
        }

        return null;
    }
}
