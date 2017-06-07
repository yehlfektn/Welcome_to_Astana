package kz.welcometoastana.Sightseeings;

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
                return context.getString(R.string.all_objects);
            case 1:
                return context.getString(R.string.Culture);
            case 2:
                return context.getString(R.string.History);
            case 3:
                return context.getString(R.string.Dohovnost);
            case 4:
                return context.getString(R.string.Dosug);
            case 5:
                return context.getString(R.string.Interesting_places);
            case 6:
                return context.getString(R.string.architecture);

        }

        return null;
    }
}
