package kz.welcometoastana.Excursion;

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
                return "Городские маршруты";
            case 1:
                return "Отдых за городом";
            case 2:
                return "Туры по казахстану";


        }

        return null;
    }
}
