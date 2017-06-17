package kz.welcometoastana;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
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
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.localizationactivity.LocalizationActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import kz.welcometoastana.Pamyatka.Info;
import kz.welcometoastana.Pamyatka.Poleznaya;
import kz.welcometoastana.Pamyatka.Prebyvanie;
import kz.welcometoastana.Pamyatka.Transport;
import kz.welcometoastana.Sightseeings.SightSeeingsFragment;


public class MainActivity extends LocalizationActivity {


    public static Location  gpsLocation;
    public static Calendar dateTimeFrom;
    public static Calendar dateTimeTo;
    SimpleDateFormat formatDateTime = new SimpleDateFormat("dd MMM yyyy");
    DatePickerDialog.OnDateSetListener from = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateTimeFrom.set(Calendar.YEAR, year);
            dateTimeFrom.set(Calendar.MONTH, monthOfYear);
            dateTimeFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Fragment current = getSupportFragmentManager().findFragmentById(R.id.mainFrame);
            ((TextView) current.getView().findViewById(R.id.txtFrom)).setText(formatDateTime.format(dateTimeFrom.getTime()));
        }
    };
    DatePickerDialog.OnDateSetListener to = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateTimeTo.set(Calendar.YEAR, year);
            dateTimeTo.set(Calendar.MONTH, monthOfYear);
            dateTimeTo.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            Fragment current = getSupportFragmentManager().findFragmentById(R.id.mainFrame);
            ((TextView) current.getView().findViewById(R.id.txtTo)).setText(formatDateTime.format(dateTimeTo.getTime()));
        }
    };
    private Boolean exit = false;
    private ExpandableListView elv;
    private Boolean visible = false;
    private Boolean mapVisible = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        //setting navigation drawer code was generated
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        View headerView;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
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
                    pixels = (int) (740 * scale + 0.5f);
                    params.height=pixels;
                    linearLayout.setLayoutParams(params);
                }else if(groupPosition == 1 || groupPosition==2){
                    pixels = (int) (710 * scale + 0.5f);
                    params.height=pixels;
                    linearLayout.setLayoutParams(params);
                }else if(groupPosition == 3){
                    pixels = (int) (740 * scale + 0.5f);
                    params.height=pixels;
                    linearLayout.setLayoutParams(params);
                }else if(groupPosition == 4){
                    GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, new int[]{0xff366AFE, 0xff1A44BD});
                    getSupportActionBar().setBackgroundDrawable(g);

                    Fragment fragment = new Expo();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.mainFrame, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();

                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    elv.collapseGroup(4);
                } else if (groupPosition == 5) {
                    GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, new int[]{0xffFF00AA, 0xffFF00AA});
                    getSupportActionBar().setBackgroundDrawable(g);

                    Fragment fragment = new Info();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.mainFrame, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();


                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    elv.collapseGroup(5);

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
        group t6 = new group(getResources().getString(R.string.chinese));

        ArrayList<group> allGroups = new ArrayList<group>();
        allGroups.add(t1);
        allGroups.add(t2);
        allGroups.add(t3);
        allGroups.add(t4);
        allGroups.add(t5);
        allGroups.add(t6);
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

    private void goToUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    public void onChangeToRuClicked(View view) {
        updateViews("ru");
    }

    public void onChangeToEnClicked(View view) {
        updateViews("en");
    }

    public void onChangeToKzClicked(View view) {
        updateViews("kk");
    }

    public void from(View view) {
        dateTimeFrom = Calendar.getInstance();
        new DatePickerDialog(this, from, dateTimeFrom.get(Calendar.YEAR), dateTimeFrom.get(Calendar.MONTH), dateTimeFrom.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void to(View view) {
        dateTimeTo = Calendar.getInstance();
        new DatePickerDialog(this, to, dateTimeTo.get(Calendar.YEAR), dateTimeTo.get(Calendar.MONTH), dateTimeTo.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void ok(View view) {
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.mainFrame);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.detach(current);
        ft.attach(current);
        ft.commit();
        (current.getView().findViewById(R.id.linearEvents)).setVisibility(View.GONE);
        (current.getView().findViewById(R.id.filter)).setVisibility(View.GONE);
    }

    public void resetFilter(View view) {
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.mainFrame);
        ((TextView) current.getView().findViewById(R.id.txtTo)).setText(getResources().getString(R.string.to));
        ((TextView) current.getView().findViewById(R.id.txtFrom)).setText(getResources().getString(R.string.from));
    }

    public void filterButton(View view) {

        Fragment current = getSupportFragmentManager().findFragmentById(R.id.mainFrame);
        if (current instanceof EventsFragment) {
            if (visible) {
                (current.getView().findViewById(R.id.linearEvents)).setVisibility(View.GONE);
                (current.getView().findViewById(R.id.filter)).setVisibility(View.GONE);
                visible = false;
            } else {
                (current.getView().findViewById(R.id.linearEvents)).setVisibility(View.VISIBLE);
                (current.getView().findViewById(R.id.filter)).setVisibility(View.VISIBLE);
                visible = true;
            }
        }
    }


    private void updateViews(String loc) {
        elv.collapseGroup(0);
        elv.collapseGroup(1);
        elv.collapseGroup(2);
        elv.collapseGroup(3);
        setLanguage(loc);
    }

    public void turnMap(View view) {
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.mainFrame);
        if (current instanceof EventsFragment) {
            if (mapVisible) {
                (current.getView().findViewById(R.id.mapView)).setVisibility(View.GONE);
                (current.getView().findViewById(R.id.viewpagerEvent)).setVisibility(View.VISIBLE);
                mapVisible = false;
            } else {
                (current.getView().findViewById(R.id.mapView)).setVisibility(View.VISIBLE);
                (current.getView().findViewById(R.id.viewpagerEvent)).setVisibility(View.GONE);
                mapVisible = true;
            }
        }

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
