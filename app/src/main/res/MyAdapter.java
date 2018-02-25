package kz.welcometoastana.Events;

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
                return new AllEvents();
            case 1:
                return new Concerts();
            case 2:
                return new Vystavki();
            case 3:
                return new Theatre();
            case 4:
                return new Sports();
        }
        return null;
    }

    @Override
    public int getCount() {

        return 5;

    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.all_events);
            case 1:
                return context.getString(R.string.Concerts);
            case 2:
                return context.getString(R.string.vystavky);
            case 3:
                return context.getString(R.string.theatre);
            case 4:
                return context.getString(R.string.sport_events);
        }

        return null;
    }
}
