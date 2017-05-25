package kz.welcometoastana.Entertainment;

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
                return context.getString(R.string.torgoviy_centres);
            case 1:
                return context.getString(R.string.Zona_otdyha);
            case 2:
                return context.getString(R.string.beaches);
            case 3:
                return context.getString(R.string.Nochnye_cluby);
            case 4:
                return context.getString(R.string.cinemas);
            case 5:
                return context.getString(R.string.entertainment);

        }

        return null;
    }
}
