package com.nurdaulet.project;

import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nurdaulet.project.Entertainment.EntertainmentFragment;
import com.nurdaulet.project.Events.EventsFragment;
import com.nurdaulet.project.Events.EventsItemList;
import com.nurdaulet.project.Excursion.ExcursionsFragment;
import com.nurdaulet.project.GdeOstanovitsya.GdeOstanovitsya;
import com.nurdaulet.project.GdePoest.GdePoest;
import com.nurdaulet.project.ListView.CustomAdapter;
import com.nurdaulet.project.ListView.group;
import com.nurdaulet.project.Pamyatka.Expo;
import com.nurdaulet.project.Pamyatka.Extrennaya;
import com.nurdaulet.project.Pamyatka.Poleznaya;
import com.nurdaulet.project.Pamyatka.Prebyvanie;
import com.nurdaulet.project.Pamyatka.Transport;
import com.nurdaulet.project.Sightseeings.SightSeeingsFragment;

import java.util.ArrayList;
import java.util.List;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    String TAG = "MainActivity";
    public static Location  gpsLocation;
    public static List<EventsItemList> eventsItemList;
    private Boolean exit = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //setting navigation drawer code was generated
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MultiDex.install(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //end of generated code

        SmartLocation.with(this).location()
                .oneFix()
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        gpsLocation = location;
                        //Toast.makeText(getApplicationContext(),"location: "+location,Toast.LENGTH_LONG).show();
                    }
                });


        //THE EXPANDABLELIST
        View headerView = navigationView.getHeaderView(0);
        final ExpandableListView elv = (ExpandableListView) headerView.findViewById(R.id.expandableListView1);
        final EditText edt = (EditText) headerView.findViewById(R.id.searchTxt);

        final ArrayList<group> group = getData();

        //layout Parameters
        final LinearLayout linearLayout = (LinearLayout)headerView.findViewById(R.id.layoutLinear);
        final ViewGroup.LayoutParams params = linearLayout.getLayoutParams();
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;



        //CREATE AND BIND TO ADAPTER
        CustomAdapter adapter = new CustomAdapter(this, group);

        elv.setAdapter(adapter);

        elv.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;
            int pixels;

            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousItem )
                    elv.collapseGroup(previousItem);
                previousItem = groupPosition;
                if(groupPosition == 0){
                    pixels = (int) (760 * scale + 0.5f);
                    params.height=pixels;
                    linearLayout.setLayoutParams(params);
                }else if(groupPosition == 1 || groupPosition==2){
                    pixels = (int) (700 * scale + 0.5f);
                    params.height=pixels;
                    linearLayout.setLayoutParams(params);
                }else if(groupPosition == 3){
                    pixels = (int) (750 * scale + 0.5f);
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
                    pixels = (int) (600 * scale + 0.5f);
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

                FragmentManager fm = getSupportFragmentManager();

                for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    if(fm.getBackStackEntryAt(0)!=null){
                    if( getFragmentManager().findFragmentByTag(fm.getBackStackEntryAt(0).getName())!=null){
                        getFragmentManager().findFragmentByTag(fm.getBackStackEntryAt(0).getName()).onDetach();
                        getFragmentManager().findFragmentByTag(fm.getBackStackEntryAt(0).getName()).onDestroy();
                    }
                    }
                    if(fm.getBackStackEntryAt(i)!=null){
                        if( getFragmentManager().findFragmentByTag(fm.getBackStackEntryAt(i).getName())!=null){
                            getFragmentManager().findFragmentByTag(fm.getBackStackEntryAt(i).getName()).onDetach();
                            getFragmentManager().findFragmentByTag(fm.getBackStackEntryAt(i).getName()).onDestroy();
                        }
                    }
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


                //Just for debugging
                //Toast.makeText(getApplicationContext(), group.get(groupPos).items.get(childPos) + "G:" + groupPos + " C:" + childPos, Toast.LENGTH_SHORT).show();

                //if fragment found, make a transaction



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
                        Toast.makeText(getApplicationContext(), "EditTextWasActivated", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }
                return false;
            }
        });


    }

    //ADD AND GET DATA
    private ArrayList<group> getData() {
        group t1 = new group("КУДА СХОДИТЬ");
        t1.items.add("События");
        t1.items.add("Достопримечательности");
        t1.items.add("Экскурсии");
        t1.items.add("Шоппинг и Развлечения");



        group t2 = new group("ГДЕ ПОЕСТЬ");
        t2.items.add("Кафе");
        t2.items.add("Рестораны");
        t2.items.add("Бары");
        group t3 = new group("ГДЕ ОСТАНОВИТЬСЯ");
        t3.items.add("Гостиницы");
        t3.items.add("Хостелы");
        t3.items.add("Апартаменты");
        group t4 = new group("ПАМЯТКА ТУРИСТУ");
        t4.items.add("Пребывание");
        t4.items.add("Транспорт");
        t4.items.add("Полезная информация");
        t4.items.add("Экстренная помощь");
        group t5 = new group("EXPO");

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Xbutton(View v) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    public void goToFacebook(View view) {
        goToUrl("https://www.facebook.com/welcometoastana/");
    }

    public void goToMailru(View view) {
        goToUrl("http://mail.ru/");
    }

    public void goToInsta(View view) {
        goToUrl("https://www.instagram.com/welcometoastana/");
    }

    public void goToVk(View view) {
        goToUrl("https://vk.com/welcometoastana");
    }

    private void goToUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
