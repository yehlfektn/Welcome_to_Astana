package kz.welcometoastana.Excursion;

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
                return new GorodFragment();
            case 1:
                return new OtdyhFragment();
            case 2:
                return new ToursFragment();



        }
        return null;
    }

    @Override
    public int getCount() {

        return 3;

    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.city_ways);
            case 1:
                return context.getString(R.string.Otdyh);
            case 2:
                return context.getString(R.string.tours);


        }

        return null;
    }
}
