package com.nurdaulet.project;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    String TAG = "MainActivity";
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ImageView imgX;
        //setting navigation drawer code was generated
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //end of generated code

        //THE EXPANDABLE
        View headerView = navigationView.getHeaderView(0);
        ExpandableListView elv = (ExpandableListView) headerView.findViewById(R.id.expandableListView1);
        final EditText edt = (EditText) headerView.findViewById(R.id.searchTxt);

        final ArrayList<group> group = getData();
        //CREATE AND BIND TO ADAPTER
        CustomAdapter adapter = new CustomAdapter(this, group);

        elv.setAdapter(adapter);

        //SET ONCLICK LISTENER just for debugging
        elv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPos,
                                        int childPos, long id) {
                Fragment fragment = null;

                //finding out which fragment to use
                if (groupPos == 0) {
                    if (childPos == 0) {
                        fragment = new SightSeeingsFragment();
                    } else if (childPos == 1) {
                        fragment = new ShoppingFragment();
                    } else if (childPos == 2) {
                        fragment = new EntertainmentFragment();
                    } else if (childPos == 3) {
                        fragment = new ExcursionsFragment();
                    }
                }


                //Just for debugging
                Toast.makeText(getApplicationContext(), group.get(groupPos).items.get(childPos) + "G:" + groupPos + " C:" + childPos, Toast.LENGTH_SHORT).show();

                //if fragment found, make a transaction
                if (fragment != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                    transaction.replace(R.id.mainFrame, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

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
        t1.items.add("Достопримечательности");
        t1.items.add("Шоппинг");
        t1.items.add("Развлечения");
        t1.items.add("Экскурсии");
        t1.items.add("События");

        group t2 = new group("ГДЕ ПОЕСТЬ");
        group t3 = new group("ГДЕ ОСТАНОВИТЬСЯ");
        group t4 = new group("ПАМЯТКА ТУРИСТУ");
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
            super.onBackPressed();
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
