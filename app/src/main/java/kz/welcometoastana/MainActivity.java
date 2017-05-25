package kz.welcometoastana;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import kz.welcometoastana.Entertainment.EntertainmentFragment;
import kz.welcometoastana.Events.EventsFragment;
import kz.welcometoastana.Excursion.ExcursionsFragment;
import kz.welcometoastana.GdeOstanovitsya.GdeOstanovitsya;
import kz.welcometoastana.GdePoest.GdePoest;
import kz.welcometoastana.ListView.CustomAdapter;
import kz.welcometoastana.ListView.group;
import kz.welcometoastana.Pamyatka.Expo;
import kz.welcometoastana.Pamyatka.Extrennaya;
import kz.welcometoastana.Pamyatka.Poleznaya;
import kz.welcometoastana.Pamyatka.Prebyvanie;
import kz.welcometoastana.Pamyatka.Transport;
import kz.welcometoastana.Sightseeings.SightSeeingsFragment;
import kz.welcometoastana.utility.LocaleHelper;


public class MainActivity extends AppCompatActivity {


    public static Location  gpsLocation;
    private Boolean exit = false;
    private View headerView;
    private ExpandableListView elv;
    private Boolean changed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //setting navigation drawer code was generated
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                if (changed) {
                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.mainFrame);
                    FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
                    Fragment newFragment = null;
                    if (currentFragment instanceof EntertainmentFragment) {
                        newFragment = new EntertainmentFragment();
                    } else if (currentFragment instanceof EventsFragment) {
                        newFragment = new EventsFragment();
                    } else if (currentFragment instanceof SightSeeingsFragment) {
                        newFragment = new SightSeeingsFragment();
                    } else if (currentFragment instanceof ExcursionsFragment) {
                        newFragment = new ExcursionsFragment();
                    } else if (currentFragment instanceof GdeOstanovitsya) {
                        newFragment = new GdeOstanovitsya();
                        newFragment.setArguments(currentFragment.getArguments());
                    } else if (currentFragment instanceof GdePoest) {
                        newFragment = new GdePoest();
                        newFragment.setArguments(currentFragment.getArguments());
                    } else if (currentFragment instanceof Prebyvanie) {
                        newFragment = new Prebyvanie();
                    } else if (currentFragment instanceof Transport) {
                        newFragment = new Transport();
                    } else if (currentFragment instanceof Poleznaya) {
                        newFragment = new Poleznaya();
                    } else if (currentFragment instanceof Extrennaya) {
                        newFragment = new Extrennaya();
                    } else if (currentFragment instanceof Expo) {
                        newFragment = new Expo();
                    }

                    if (newFragment != null) {
                        fragTransaction.replace(R.id.mainFrame, newFragment);
                        fragTransaction.addToBackStack(null);
                        fragTransaction.commit();
                        changed = false;
                    }
                }
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //end of generated code

        //Finding location using SmartLocation
        SmartLocation.with(this).location()
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        gpsLocation = location;
                        Log.d("MainActivity", "Gsp Location was set: "+location.toString());
                        SmartLocation.with(getApplicationContext()).location().stop();
                        //Toast.makeText(getApplicationContext(),"location: "+location,Toast.LENGTH_LONG).show();
                    }
                });

        //Getting access to the header view
        headerView = navigationView.getHeaderView(0);


        //language determination
        String loc = getCurrentLocale().toString();
        Log.d("MainActivity", "loc: " + loc);
        if (loc.startsWith("en")) {
            TextView en = (TextView) headerView.findViewById(R.id.txtEng);
            en.setTextColor(Color.BLACK);
        } else if (loc.startsWith("kk")) {
            TextView kz = (TextView) headerView.findViewById(R.id.txtKaz);
            kz.setTextColor(Color.BLACK);
        } else {
            TextView ru = (TextView) headerView.findViewById(R.id.txtRus);
            ru.setTextColor(Color.BLACK);
        }

        //THE EXPANDABLELIST
        elv = (ExpandableListView) headerView.findViewById(R.id.expandableListView1);
        final EditText edt = (EditText) headerView.findViewById(R.id.searchTxt);

        //getting the data
        final ArrayList<group> group = getData();

        //layout Parameters
        final LinearLayout linearLayout = (LinearLayout)headerView.findViewById(R.id.layoutLinear);
        final ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;

        //CREATE AND BIND TO ADAPTER
        CustomAdapter adapter = new CustomAdapter(this, group);

        elv.setAdapter(adapter);

        elv.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            int pixels;
            @Override
            public void onGroupCollapse(int groupPosition) {
                pixels = (int) (580 * scale + 0.5f);
                params.height=pixels;
                linearLayout.setLayoutParams(params);
            }
        });

        elv.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;
            int pixels;

            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousItem )
                    elv.collapseGroup(previousItem);

                previousItem = groupPosition;
                if(groupPosition == 0){
                    pixels = (int) (730 * scale + 0.5f);
                    params.height=pixels;
                    linearLayout.setLayoutParams(params);
                }else if(groupPosition == 1 || groupPosition==2){
                    pixels = (int) (700 * scale + 0.5f);
                    params.height=pixels;
                    linearLayout.setLayoutParams(params);
                }else if(groupPosition == 3){
                    pixels = (int) (730 * scale + 0.5f);
                    params.height=pixels;
                    linearLayout.setLayoutParams(params);
                }else if(groupPosition == 4){

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    elv.collapseGroup(4);
                    FragmentManager fm = getSupportFragmentManager();
                    for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                        fm.popBackStack();
                    }
                    pixels = (int) (580 * scale + 0.5f);
                    params.height=pixels;
                    linearLayout.setLayoutParams(params);
                    GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, new int[]{ 0xff366AFE, 0xff1A44BD});
                    getSupportActionBar().setBackgroundDrawable(g);
                    Fragment fragment = new Expo();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                    transaction.replace(R.id.mainFrame, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

                }
            }

        });

        Fragment fragment = new EventsFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.mainFrame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        //SET ONCLICK LISTENER just for debugging
        elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPos,
                                        int childPos, long id) {

                changed=false;
                FragmentManager fm = getSupportFragmentManager();

                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    if (fm.getBackStackEntryAt(0) != null) {
                        if (getSupportFragmentManager().findFragmentByTag(fm.getBackStackEntryAt(0).getName()) != null) {
                            getSupportFragmentManager().findFragmentByTag(fm.getBackStackEntryAt(0).getName()).onDetach();
                            getSupportFragmentManager().findFragmentByTag(fm.getBackStackEntryAt(0).getName()).onDestroy();
                    }
                    }
                    if (fm.getBackStackEntryAt(i) != null) {
                        if (getSupportFragmentManager().findFragmentByTag(fm.getBackStackEntryAt(i).getName()) != null) {
                            getSupportFragmentManager().findFragmentByTag(fm.getBackStackEntryAt(i).getName()).onDetach();
                            getSupportFragmentManager().findFragmentByTag(fm.getBackStackEntryAt(i).getName()).onDestroy();
                        }
                    }
                }
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }


                Fragment fragment;
                Bundle bundle = new Bundle();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //finding out which fragment to use
                if (groupPos == 0) {
                    //changing the gradient
                    GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, new int[]{ 0xffFF5800 , 0xffFF8B00 });
                    getSupportActionBar().setBackgroundDrawable(g);
                    if (childPos == 0) {
                        fragment = new EventsFragment();
                        transaction.replace(R.id.mainFrame, fragment,"Events");
                    } else if (childPos == 1) {
                            fragment = new SightSeeingsFragment();
                        transaction.replace(R.id.mainFrame, fragment,"SightSeeings");
                    } else if (childPos == 2) {
                            fragment = new ExcursionsFragment();
                        transaction.replace(R.id.mainFrame, fragment,"Excursion");
                    } else if (childPos == 3) {
                            fragment = new EntertainmentFragment();
                        transaction.replace(R.id.mainFrame, fragment,"Entertainment");
                    }
                }else if(groupPos == 1){
                    GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, new int[]{ 0xff17A400 , 0xff5ABC05 });
                    getSupportActionBar().setBackgroundDrawable(g);

                    if(childPos == 0){
                        bundle.putInt("position",1);
                        fragment = new GdePoest();
                        fragment.setArguments(bundle);
                        transaction.replace(R.id.mainFrame, fragment);
                    }else if(childPos == 1){
                        bundle.putInt("position",2);
                        fragment = new GdePoest();
                        fragment.setArguments(bundle);
                        transaction.replace(R.id.mainFrame, fragment);
                    }else if(childPos == 2){
                        bundle.putInt("position",3);
                        fragment = new GdePoest();
                        fragment.setArguments(bundle);
                        transaction.replace(R.id.mainFrame, fragment);
                    }
                }else if(groupPos == 2){
                    GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, new int[]{ 0xff851AF2, 0xffA64DFF});
                    getSupportActionBar().setBackgroundDrawable(g);
                    if(childPos == 0){
                        bundle.putInt("position",1);
                        fragment = new GdeOstanovitsya();
                        fragment.setArguments(bundle);
                        transaction.replace(R.id.mainFrame, fragment);
                    }else if(childPos == 1){
                        bundle.putInt("position",2);
                        fragment = new GdeOstanovitsya();
                        fragment.setArguments(bundle);
                        transaction.replace(R.id.mainFrame, fragment);
                    }else if(childPos == 2){
                        bundle.putInt("position",3);
                        fragment = new GdeOstanovitsya();
                        fragment.setArguments(bundle);
                        transaction.replace(R.id.mainFrame, fragment);
                    }

                }else if(groupPos == 3) {
                    GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, new int[]{0xffFFA800, 0xffFFD200});
                    getSupportActionBar().setBackgroundDrawable(g);
                    if (childPos == 0) {
                        fragment = new Prebyvanie();
                        transaction.replace(R.id.mainFrame, fragment,"Trans");
                    } else if (childPos == 1) {
                            fragment = new Transport();
                        transaction.replace(R.id.mainFrame, fragment,"Trans");
                    } else if (childPos == 2) {

                            fragment = new Poleznaya();
                        transaction.replace(R.id.mainFrame, fragment,"Polez");
                    } else if (childPos == 3) {

                            fragment = new Extrennaya();
                        transaction.replace(R.id.mainFrame, fragment,"Extr");
                    }

                }

                    transaction.addToBackStack(null);
                    transaction.commit();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });

        //setting onTouchListener to EditText
        edt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (edt.getRight() - edt.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // close drawer
                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    }
                }
                return false;
            }
        });


    }

    //ADD AND GET DATA
    private ArrayList<group> getData() {
        group t1 = new group(getResources().getString(R.string.kuda_shodit));
        t1.items.add(getResources().getString(R.string.events));
        t1.items.add(getResources().getString(R.string.sightseegins));
        t1.items.add(getResources().getString(R.string.excursions));
        t1.items.add(getResources().getString(R.string.shopping_and));
        group t2 = new group(getString(R.string.gde_poest));
        t2.items.add(getResources().getString(R.string.cafe));
        t2.items.add(getResources().getString(R.string.restourant));
        t2.items.add(getResources().getString(R.string.bar));
        group t3 = new group(getString(R.string.gde_ostanovitsya));
        t3.items.add(getResources().getString(R.string.hotels));
        t3.items.add(getResources().getString(R.string.hostels));
        t3.items.add(getResources().getString(R.string.aparments));
        group t4 = new group(getString(R.string.pamyatka));
        t4.items.add(getResources().getString(R.string.prebyvanie));
        t4.items.add(getResources().getString(R.string.transport));
        t4.items.add(getResources().getString(R.string.poleznaya));
        t4.items.add(getResources().getString(R.string.extrennaya));
        group t5 = new group(getString(R.string.Expo));

        ArrayList<group> allGroups = new ArrayList<group>();
        allGroups.add(t1);
        allGroups.add(t2);
        allGroups.add(t3);
        allGroups.add(t4);
        allGroups.add(t5);
        return allGroups;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (exit) {
                finish(); // finish activity
            } else {
                Toast.makeText(this, R.string.press_Back,
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3000);

            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    public void Xbutton(View v) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void goToFacebook(View view) {
        goToUrl("https://www.facebook.com/welcometoastana/");
    }
    public void goToInsta(View view) {
        goToUrl("https://www.instagram.com/welcometoastana/");
    }
    public void goToVk(View view) {
        goToUrl("https://vk.com/welcometoastana");
    }
    public void goToExpo(View view) {
        goToUrl("https://tickets.expo2017astana.com");
    }

    public void goToIbec(View view){goToUrl("http://www.ibecsystems.com/");}


    private void goToUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    public void onChangeToRuClicked(View view) {
        updateViews("ru_RU");
    }

    public void onChangeToEnClicked(View view) {
        updateViews("en");
    }

    public void onChangeToKzClicked(View view) {
        updateViews("kk_KZ");
    }

    private void updateViews(String loc) {
        changed = true;
        Log.d("MainActivity", "before: " + getCurrentLocale().toString());
        LocaleHelper.setLocale(this, loc);
        Log.d("MainActivity", "after setting: " + getCurrentLocale().toString());
        TextView en = (TextView) headerView.findViewById(R.id.txtEng);
        TextView kz = (TextView) headerView.findViewById(R.id.txtKaz);
        TextView ru = (TextView) headerView.findViewById(R.id.txtRus);
        EditText editText = (EditText) headerView.findViewById(R.id.searchTxt);
        TextView dev = (TextView) headerView.findViewById(R.id.developed);
        if (loc.startsWith("en")) {
            en.setTextColor(Color.BLACK);
            kz.setTextColor(ContextCompat.getColor(this, R.color.txtColor));
            ru.setTextColor(ContextCompat.getColor(this, R.color.txtColor));
        } else if (loc.startsWith("kk")) {
            kz.setTextColor(Color.BLACK);
            en.setTextColor(ContextCompat.getColor(this, R.color.txtColor));
            ru.setTextColor(ContextCompat.getColor(this, R.color.txtColor));
        } else {
            ru.setTextColor(Color.BLACK);
            kz.setTextColor(ContextCompat.getColor(this, R.color.txtColor));
            en.setTextColor(ContextCompat.getColor(this, R.color.txtColor));
        }
        en.setText(getResources().getString(R.string.eng));
        ru.setText(getResources().getString(R.string.rus));
        kz.setText(getResources().getString(R.string.kaz));
        editText.setHint(getResources().getString(R.string.what_are_you_looking_for));
        dev.setText(getResources().getString(R.string.developed_in));
        final ArrayList<group> group = getData();
        CustomAdapter adapter = new CustomAdapter(this, group);
        elv.setAdapter(adapter);

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.mainFrame);
        FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
        fragTransaction.detach(currentFragment);
        fragTransaction.attach(currentFragment);
        fragTransaction.commit();


    }

    @TargetApi(Build.VERSION_CODES.N)
    public Locale getCurrentLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return getResources().getConfiguration().locale;
        }
    }

}
