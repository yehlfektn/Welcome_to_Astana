package kz.welcometoastana;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.v4.app.NotificationCompat;
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
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.localizationactivity.LocalizationActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.NotificationTarget;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;
import kz.welcometoastana.Entertainment.EntertainmentFragment;
import kz.welcometoastana.Events.EventsDescription;
import kz.welcometoastana.Events.EventsFragment;
import kz.welcometoastana.Events.EventsItemList;
import kz.welcometoastana.Excursion.ExcursionsFragment;
import kz.welcometoastana.GdeOstanovitsya.GdeOstanovitsya;
import kz.welcometoastana.GdeOstanovitsya.GdeOstanovitsyaDescription;
import kz.welcometoastana.GdeOstanovitsya.HotelsListItem;
import kz.welcometoastana.GdePoest.GdePoest;
import kz.welcometoastana.GdePoest.GdePoestDescription;
import kz.welcometoastana.GdePoest.GdePoestListItem;
import kz.welcometoastana.ListView.CustomAdapter;
import kz.welcometoastana.ListView.group;
import kz.welcometoastana.Pamyatka.Expo;
import kz.welcometoastana.Pamyatka.Extrennaya;
import kz.welcometoastana.Pamyatka.Info;
import kz.welcometoastana.Pamyatka.Poleznaya;
import kz.welcometoastana.Pamyatka.Prebyvanie;
import kz.welcometoastana.Pamyatka.Transport;
import kz.welcometoastana.Sightseeings.DescriptionActivity;
import kz.welcometoastana.Sightseeings.SightSeeingsFragment;
import kz.welcometoastana.utility.MyRequest;



public class MainActivity extends LocalizationActivity {

    SimpleDateFormat formatDateTime = new SimpleDateFormat("dd MMM yyyy");
    List<WeakReference<Fragment>> fragList = new ArrayList<WeakReference<Fragment>>();
    private Calendar dateTimeFrom;
    private Calendar dateTimeTo;
    private Boolean mapVisible = false;
    private Handler handler;
    private RequestManager glide;

    private DatePickerDialog.OnDateSetListener from = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateTimeFrom.set(Calendar.YEAR, year);
            dateTimeFrom.set(Calendar.MONTH, monthOfYear);
            dateTimeFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SharedPreferences sharedPref = getSharedPreferences("time", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putLong("dateTimeFrom", dateTimeFrom.getTimeInMillis());
            editor.commit();

            Fragment current = getSupportFragmentManager().findFragmentById(R.id.mainFrame);
            ((TextView) current.getView().findViewById(R.id.txtFrom)).setText(formatDateTime.format(dateTimeFrom.getTime()));
        }
    };
    private DatePickerDialog.OnDateSetListener to = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateTimeTo.set(Calendar.YEAR, year);
            dateTimeTo.set(Calendar.MONTH, monthOfYear);
            dateTimeTo.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SharedPreferences sharedPref = getSharedPreferences("time", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putLong("dateTimeTo", dateTimeTo.getTimeInMillis());
            editor.commit();
            Fragment current = getSupportFragmentManager().findFragmentById(R.id.mainFrame);
            ((TextView) current.getView().findViewById(R.id.txtTo)).setText(formatDateTime.format(dateTimeTo.getTime()));
        }
    };
    private List<GdePoestListItem> gdePoestListItems;
    private List<HotelsListItem> hotelsListItems;
    private List<EventsItemList> eventsItemLists;
    private List<KudaShoditListItem> kudaShoditListItems;
    private Boolean exit = false;
    private ExpandableListView elv;
    private Boolean visible = false;
    private NotificationTarget notificationTarget;
    private HashSet<Integer> ids;
    private boolean firstTime = true;


    @Override
    public void onDestroy() {
        Log.d("MainActivity", "OnDestroy was called");

        formatDateTime = null;
        fragList = null;
        dateTimeTo = null;
        dateTimeFrom = null;
        glide.onDestroy();
        from = null;
        to = null;
        gdePoestListItems = null;
        hotelsListItems = null;
        eventsItemLists = null;
        kudaShoditListItems = null;
        elv = null;
        notificationTarget = null;
        ids = null;


        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        SmartLocation.with(getApplicationContext()).location().stop();


        super.onDestroy();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        //setting navigation drawer code was generated
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (glide == null) {
            glide = Glide.with(this);
        }

        Log.d("MainActivity", "Oncreate");

        SharedPreferences sharedPref = getSharedPreferences("position", Context.MODE_PRIVATE);
        mapVisible = sharedPref.getBoolean("map", false);

        if (gdePoestListItems == null) {
            SharedPreferences prefs = getSharedPreferences("arraylist", Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = prefs.getString("gde", null);
            Type type = new TypeToken<ArrayList<GdePoestListItem>>() {
            }.getType();
            gdePoestListItems = gson.fromJson(json, type);
            if (gdePoestListItems != null) {
                Log.d("MainActivity", "gde loaded: " + gdePoestListItems.size());
            } else {
                gdePoestListItems = new ArrayList<>();
            }
        }
        if (hotelsListItems == null) {
            SharedPreferences prefs = getSharedPreferences("arraylist", Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = prefs.getString("hotel", null);
            Type type = new TypeToken<ArrayList<HotelsListItem>>() {
            }.getType();
            hotelsListItems = gson.fromJson(json, type);
            if (hotelsListItems != null) {
                Log.d("MainActivity", "hotel loaded: " + hotelsListItems.size());
            } else {
                hotelsListItems = new ArrayList<>();
            }
        }
        if (eventsItemLists == null) {

            SharedPreferences prefs = getSharedPreferences("arraylist", Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = prefs.getString("event", null);
            Type type = new TypeToken<ArrayList<EventsItemList>>() {
            }.getType();
            eventsItemLists = gson.fromJson(json, type);
            if (eventsItemLists != null) {
                Log.d("MainActivity", "event loaded: " + eventsItemLists.size());
            } else {
                eventsItemLists = new ArrayList<>();
            }
        }
        if (kudaShoditListItems == null) {
            SharedPreferences prefs = getSharedPreferences("arraylist", Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = prefs.getString("kuda", null);
            Type type = new TypeToken<ArrayList<KudaShoditListItem>>() {
            }.getType();
            kudaShoditListItems = gson.fromJson(json, type);
            if (kudaShoditListItems != null) {
                Log.d("MainActivity", "kuda loaded: " + kudaShoditListItems.size());
            } else {
                kudaShoditListItems = new ArrayList<>();
            }
        }



        //Getting data
        loadRecyclerViewGdeOst();
        loadRecyclerViewGdePoest();
        loadRecyclerViewEvents();
        loadRecyclerViewSight();
        loadRecyclerViewShopping();

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

        LocationParams.Builder builder = new LocationParams.Builder()
                .setAccuracy(LocationAccuracy.HIGH)
                .setDistance(0)
                .setInterval(1800000);

        //Finding location using SmartLocation
        SmartLocation.with(getApplicationContext()).location().config(builder.build()).continuous()
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        SharedPreferences sharedPref = getSharedPreferences("app", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("lat", location.getLatitude() + "");
                        editor.putString("lon", location.getLongitude() + "");
                        editor.commit();

                        Gson gson = new Gson();
                        String json = sharedPref.getString("ids", null);
                        Type type = new TypeToken<HashSet<Integer>>() {
                        }.getType();
                        ids = gson.fromJson(json, type);
                        if (ids != null) {
                            Log.d("MainActivity", "ids loaded: " + ids.size());
                        } else {
                            ids = new HashSet<>();
                        }

                        GdePoestListItem gde = null;
                        Location endPoint = new Location("locationB");


                        Date date = new Date(System.currentTimeMillis());


                        Date myDate = new Date(sharedPref.getLong("time", 0));
                        long difference = date.getTime() - myDate.getTime();
                        Log.d("MainActivity", "difference: " + difference);
                        Date cacheDate = new Date(sharedPref.getLong("cache", 0));
                        long cacheDifference = date.getTime() - cacheDate.getTime();

                        if (cacheDifference > 172800000) {
                            //Log.d("MainActivity",""+cacheDifference);
                            ids.clear();
                            editor.putString("ids", gson.toJson(ids));
                            editor.putLong("time", 0).commit();
                            editor.putLong("cache", date.getTime()).commit();
                            editor.commit();
                        }


                        if (difference > 1800000) {
                            editor.putLong("time", date.getTime()).apply();
                            Log.d("MainActivity", "difference proiden");

                            if (gdePoestListItems.size() != 0) {
                                double min = 1000;
                                for (GdePoestListItem gdePoestListItem : gdePoestListItems) {
                                    if (!gdePoestListItem.getLat().equals("null")) {
                                        endPoint.setLatitude(Double.parseDouble(gdePoestListItem.getLat()));
                                        endPoint.setLongitude(Double.parseDouble(gdePoestListItem.getLon()));
                                        double distanceDouble = location.distanceTo(endPoint);

                                        if (distanceDouble < min) {
                                            if (!ids.contains(gdePoestListItem.getId())) {
                                                gde = gdePoestListItem;
                                                min = distanceDouble;
                                                Log.d("MainActivity", "there is some ids");
                                            }
                                        }
                                    }
                                }

                                if (gde != null) {
                                    ids.add(gde.getId());
                                    final RemoteViews rv = new RemoteViews(getApplicationContext().getPackageName(), R.layout.remoteview_notification);

                                    rv.setImageViewResource(R.id.remoteview_notification_icon, R.drawable.icon_marker);
                                    rv.setTextViewText(R.id.remoteview_notification_headline, getResources().getString(R.string.notification));
                                    rv.setTextViewText(R.id.remoteview_notification_short_message, gde.getName());

                                    //Notification starts
                                    NotificationCompat.Builder mBuilder =
                                            new NotificationCompat.Builder(getApplicationContext())
                                                    .setSmallIcon(R.mipmap.ic_launcher)
                                                    .setContentTitle("Welcome to Astana")
                                                    .setContentText(getResources().getString(R.string.notification) + "\n" + gde.getName())
                                                    .setContent(rv)
                                                    .setCustomBigContentView(rv);
                                    // Creates an explicit intent for an Activity in your app
                                    Intent intent = new Intent(getApplicationContext(), GdePoestDescription.class);
                                    intent.putExtra("name", gde.getName());
                                    intent.putExtra("id", gde.getId());
                                    intent.putExtra("description", gde.getSummary());
                                    intent.putExtra("imageUrl", gde.getImageUrl());
                                    intent.putExtra("category", gde.getCategory());
                                    intent.putExtra("longit", gde.getLon());
                                    intent.putExtra("latit", gde.getLat());
                                    intent.putExtra("address", gde.getAddress());
                                    intent.putExtra("url", "http://89.219.32.107/api/v1/foods?limit=2000&page=1");
                                    intent.putExtra("phone", gde.getPhone());

                                    Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);
                                    backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                                    PendingIntent pendingIntent = PendingIntent.getActivities(getApplicationContext(), 0,
                                            new Intent[]{backIntent, intent}, PendingIntent.FLAG_ONE_SHOT);

                                    mBuilder.setContentIntent(pendingIntent);
                                    NotificationManager mNotificationManager =
                                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    Notification notification = mBuilder.build();
                                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                                    mNotificationManager.notify(1, notification);

                                    notificationTarget = new NotificationTarget(
                                            getApplicationContext(),
                                            rv,
                                            R.id.remoteview_notification_icon,
                                            notification,
                                            1);

                                    glide
                                            .load(gde.getImageUrl())
                                            .asBitmap()
                                            .into(notificationTarget);
                                }

                            }

                            HotelsListItem hotel = null;
                            if (hotelsListItems.size() != 0) {
                                double min = 1000;
                                for (HotelsListItem hotelsListItem : hotelsListItems) {
                                    if (!hotelsListItem.getLat().equals("null")) {
                                        endPoint.setLatitude(Double.parseDouble(hotelsListItem.getLat()));
                                        endPoint.setLongitude(Double.parseDouble(hotelsListItem.getLon()));
                                        double distanceDouble = location.distanceTo(endPoint);

                                        if (distanceDouble < min) {
                                            if (!ids.contains(hotelsListItem.getId())) {
                                                hotel = hotelsListItem;
                                                min = distanceDouble;
                                            }
                                        }
                                    }
                                }
                                if (hotel != null) {
                                    ids.add(hotel.getId());
                                    final RemoteViews rv = new RemoteViews(getApplicationContext().getPackageName(), R.layout.remoteview_notification);

                                    rv.setImageViewResource(R.id.remoteview_notification_icon, R.drawable.icon_marker);
                                    rv.setTextViewText(R.id.remoteview_notification_headline, getResources().getString(R.string.notification));
                                    rv.setTextViewText(R.id.remoteview_notification_short_message, hotel.getName());
                                    //Notification starts
                                    NotificationCompat.Builder mBuilder =
                                            new NotificationCompat.Builder(getApplicationContext())
                                                    .setSmallIcon(R.mipmap.ic_launcher)
                                                    .setContentTitle("Welcome to Astana")
                                                    .setContentText(getResources().getString(R.string.notification) + "\n" + hotel.getName())
                                                    .setContent(rv)
                                                    .setCustomBigContentView(rv);

                                    // Creates an explicit intent for an Activity in your app
                                    Intent intent = new Intent(getApplicationContext(), GdeOstanovitsyaDescription.class);
                                    intent.putExtra("name", hotel.getName());
                                    intent.putExtra("id", hotel.getId());
                                    intent.putExtra("description", hotel.getSummary());
                                    intent.putExtra("imageUrl", hotel.getImageUrl());
                                    intent.putExtra("category", hotel.getCategory());
                                    intent.putExtra("longit", hotel.getLon());
                                    intent.putExtra("latit", hotel.getLat());
                                    intent.putExtra("address", hotel.getAddress());
                                    intent.putExtra("url", "http://89.219.32.107/api/v1/hotels?limit=2000&page=1");
                                    intent.putExtra("phone", hotel.getPhone());
                                    intent.putExtra("website", hotel.getWebsite());
                                    intent.putExtra("stars", hotel.getStars());
                                    intent.putExtra("urlItem", hotel.getBook_url());

                                    Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);

                                    PendingIntent pendingIntent = PendingIntent.getActivities(getApplicationContext(), 1,
                                            new Intent[]{backIntent, intent}, PendingIntent.FLAG_ONE_SHOT);

                                    mBuilder.setContentIntent(pendingIntent);
                                    mBuilder.setAutoCancel(true).setCustomBigContentView(rv);
                                    NotificationManager mNotificationManager =
                                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    Notification notification = mBuilder.build();
                                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                                    mNotificationManager.notify(2, notification);

                                    NotificationTarget notificationTarget = new NotificationTarget(
                                            getApplicationContext(),
                                            rv,
                                            R.id.remoteview_notification_icon,
                                            notification,
                                            2);

                                    glide
                                            .load(hotel.getImageUrl())
                                            .asBitmap()
                                            .into(notificationTarget);

                                }
                            }
                            EventsItemList eventsItemList = null;
                            if (eventsItemLists.size() != 0) {
                                double min = 1000;
                                for (EventsItemList eventsItemList1 : eventsItemLists) {
                                    if (!eventsItemList1.getLat().equals("null")) {
                                        endPoint.setLatitude(Double.parseDouble(eventsItemList1.getLat()));
                                        endPoint.setLongitude(Double.parseDouble(eventsItemList1.getLon()));
                                        double distanceDouble = location.distanceTo(endPoint);
                                        if (distanceDouble < min) {
                                            if (!ids.contains(eventsItemList1.getId())) {
                                                eventsItemList = eventsItemList1;
                                                min = distanceDouble;
                                            }
                                        }
                                    }
                                }
                                if (eventsItemList != null) {
                                    ids.add(eventsItemList.getId());
                                    final RemoteViews rv = new RemoteViews(getApplicationContext().getPackageName(), R.layout.remoteview_notification);

                                    rv.setImageViewResource(R.id.remoteview_notification_icon, R.drawable.icon_marker);
                                    rv.setTextViewText(R.id.remoteview_notification_headline, getResources().getString(R.string.notification));
                                    rv.setTextViewText(R.id.remoteview_notification_short_message, eventsItemList.getName());

                                    //Notification starts
                                    NotificationCompat.Builder mBuilder =
                                            new NotificationCompat.Builder(getApplicationContext())
                                                    .setSmallIcon(R.mipmap.ic_launcher)
                                                    .setContentTitle("Welcome to Astana")
                                                    .setContentText(getResources().getString(R.string.notification) + "\n" + eventsItemList.getName())
                                                    .setContent(rv)
                                                    .setCustomBigContentView(rv);
                                    // Creates an explicit intent for an Activity in your app
                                    Intent intent = new Intent(getApplicationContext(), EventsDescription.class);
                                    intent.putExtra("name", eventsItemList.getName());
                                    intent.putExtra("id", eventsItemList.getId());
                                    intent.putExtra("description", eventsItemList.getSummary());
                                    intent.putExtra("imageUrl", eventsItemList.getImageUrl());
                                    intent.putExtra("category", eventsItemList.getCategory());
                                    intent.putExtra("longit", eventsItemList.getLon());
                                    intent.putExtra("latit", eventsItemList.getLat());
                                    intent.putExtra("url", "http://89.219.32.107/api/v1/places/events?limit=2000&page=1");
                                    intent.putExtra("address", eventsItemList.getAddress());
                                    intent.putExtra("money", eventsItemList.getMoney());
                                    intent.putExtra("date", eventsItemList.getDate());
                                    intent.putExtra("urlItem", eventsItemList.getUrl());

                                    Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);

                                    PendingIntent pendingIntent = PendingIntent.getActivities(getApplicationContext(), 1,
                                            new Intent[]{backIntent, intent}, PendingIntent.FLAG_ONE_SHOT);
                                    mBuilder.setContentIntent(pendingIntent);

                                    NotificationManager mNotificationManager =
                                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    Notification notification = mBuilder.build();
                                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                                    mNotificationManager.notify(3, notification);

                                    NotificationTarget notificationTarget = new NotificationTarget(
                                            getApplicationContext(),
                                            rv,
                                            R.id.remoteview_notification_icon,
                                            notification,
                                            3);

                                    glide
                                            .load(eventsItemList.getImageUrl())
                                            .asBitmap()
                                            .into(notificationTarget);
                                }
                            }

                            KudaShoditListItem kudaShoditListItem = null;
                            if (kudaShoditListItems.size() != 0) {
                                double min = 1000;
                                for (KudaShoditListItem kudaShoditListItem1 : kudaShoditListItems) {
                                    if (!kudaShoditListItem1.getLat().equals("null")) {
                                        endPoint.setLatitude(Double.parseDouble(kudaShoditListItem1.getLat()));
                                        endPoint.setLongitude(Double.parseDouble(kudaShoditListItem1.getLon()));
                                        double distanceDouble = location.distanceTo(endPoint);
                                        if (distanceDouble < min) {
                                            if (!ids.contains(kudaShoditListItem1.getId())) {
                                                kudaShoditListItem = kudaShoditListItem1;
                                                min = distanceDouble;
                                            }
                                        }
                                    }
                                }
                                if (kudaShoditListItem != null) {
                                    ids.add(kudaShoditListItem.getId());
                                    final RemoteViews rv = new RemoteViews(getApplicationContext().getPackageName(), R.layout.remoteview_notification);

                                    rv.setImageViewResource(R.id.remoteview_notification_icon, R.drawable.icon_marker);
                                    rv.setTextViewText(R.id.remoteview_notification_headline, getResources().getString(R.string.notification));
                                    rv.setTextViewText(R.id.remoteview_notification_short_message, kudaShoditListItem.getName());

                                    //Notification starts
                                    NotificationCompat.Builder mBuilder =
                                            new NotificationCompat.Builder(getApplicationContext())
                                                    .setSmallIcon(R.mipmap.ic_launcher)
                                                    .setContentTitle("Welcome to Astana")
                                                    .setContentText(getResources().getString(R.string.notification) + "\n" + kudaShoditListItem.getName())
                                                    .setContent(rv)
                                                    .setCustomBigContentView(rv);
                                    // Creates an explicit intent for an Activity in your app
                                    Intent intent = new Intent(getApplicationContext(), DescriptionActivity.class);
                                    intent.putExtra("name", kudaShoditListItem.getName());
                                    intent.putExtra("id", kudaShoditListItem.getId());
                                    intent.putExtra("description", kudaShoditListItem.getSummary());
                                    intent.putExtra("imageUrl", kudaShoditListItem.getImageUrl());
                                    intent.putExtra("category", kudaShoditListItem.getCategory());
                                    intent.putExtra("longit", kudaShoditListItem.getLon());
                                    intent.putExtra("latit", kudaShoditListItem.getLat());
                                    if (kudaShoditListItem.getUrl() != null) {
                                        intent.putExtra("url", kudaShoditListItem.getUrl());
                                    } else {
                                        intent.putExtra("url", "http://89.219.32.107/api/v1/places/sightseeings?limit=2000&page=1");
                                    }

                                    intent.putExtra("address", kudaShoditListItem.getAddress());

                                    Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);
                                    PendingIntent pendingIntent = PendingIntent.getActivities(getApplicationContext(), 1,
                                            new Intent[]{backIntent, intent}, PendingIntent.FLAG_ONE_SHOT);
                                    mBuilder.setContentIntent(pendingIntent);
                                    NotificationManager mNotificationManager =
                                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    Notification notification = mBuilder.build();
                                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                                    mNotificationManager.notify(4, notification);

                                    NotificationTarget notificationTarget = new NotificationTarget(
                                            getApplicationContext(),
                                            rv,
                                            R.id.remoteview_notification_icon,
                                            notification,
                                            4);

                                    glide
                                            .load(kudaShoditListItem.getImageUrl())
                                            .asBitmap()
                                            .into(notificationTarget);
                                }
                            }
                            editor.putString("ids", gson.toJson(ids));
                            editor.apply();
                        }
                    }
                });

        //Getting access to the header view
        headerView = navigationView.getHeaderView(0);


        //language determination
        String loc = getCurrentLocale().toString();

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
                    findViewById(R.id.mapImage).setVisibility(View.GONE);
                    findViewById(R.id.calendar).setVisibility(View.GONE);
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
                    findViewById(R.id.mapImage).setVisibility(View.GONE);
                    findViewById(R.id.calendar).setVisibility(View.GONE);
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
                deleteFragments();
                for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                    fm.popBackStack();
                }
                deleteFragments();

                Fragment fragment;
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                //finding out which fragment to use
                if (groupPos == 0) {
                    //changing the gradient
                    GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, new int[]{ 0xffFF5800 , 0xffFF8B00 });
                    getSupportActionBar().setBackgroundDrawable(g);
                    if (childPos == 0) {
                        fragment = new EventsFragment();
                        transaction.replace(R.id.mainFrame, fragment,"Events");
                        findViewById(R.id.mapImage).setVisibility(View.VISIBLE);
                    } else {
                        findViewById(R.id.calendar).setVisibility(View.GONE);
                        if (childPos == 1) {
                            fragment = new SightSeeingsFragment();
                        transaction.replace(R.id.mainFrame, fragment,"SightSeeings");
                            findViewById(R.id.mapImage).setVisibility(View.VISIBLE);
                    } else if (childPos == 2) {
                            findViewById(R.id.mapImage).setVisibility(View.GONE);
                            fragment = new ExcursionsFragment();
                        transaction.replace(R.id.mainFrame, fragment,"Excursion");
                    } else if (childPos == 3) {
                            fragment = new EntertainmentFragment();
                            findViewById(R.id.mapImage).setVisibility(View.VISIBLE);
                        transaction.replace(R.id.mainFrame, fragment,"Entertainment");
                    }
                    }
                }else if(groupPos == 1){
                    findViewById(R.id.mapImage).setVisibility(View.VISIBLE);
                    findViewById(R.id.calendar).setVisibility(View.GONE);

                    SharedPreferences sharedPref = getSharedPreferences("position", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();

                    GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, new int[]{ 0xff17A400 , 0xff5ABC05 });
                    getSupportActionBar().setBackgroundDrawable(g);

                    if(childPos == 0){
                        editor.putInt("Pposition", 1);
                        editor.commit();
                        fragment = new GdePoest();
                        transaction.replace(R.id.mainFrame, fragment);
                    }else if(childPos == 1){
                        editor.putInt("Pposition", 2);
                        editor.commit();
                        fragment = new GdePoest();
                        transaction.replace(R.id.mainFrame, fragment);
                    }else if(childPos == 2){
                        editor.putInt("Pposition", 3);
                        editor.commit();
                        fragment = new GdePoest();
                        transaction.replace(R.id.mainFrame, fragment);
                    }
                }else if(groupPos == 2){
                    findViewById(R.id.mapImage).setVisibility(View.VISIBLE);
                    findViewById(R.id.calendar).setVisibility(View.GONE);

                    SharedPreferences sharedPref = getSharedPreferences("position", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();

                    GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, new int[]{ 0xff851AF2, 0xffA64DFF});
                    getSupportActionBar().setBackgroundDrawable(g);
                    if(childPos == 0){
                        editor.putInt("Gposition", 1);
                        editor.commit();

                        fragment = new GdeOstanovitsya();
                        transaction.replace(R.id.mainFrame, fragment);
                    }else if(childPos == 1){
                        editor.putInt("Gposition", 2);
                        editor.commit();
                        fragment = new GdeOstanovitsya();
                        transaction.replace(R.id.mainFrame, fragment);
                    }else if(childPos == 2){
                        editor.putInt("Gposition", 3);
                        editor.commit();
                        fragment = new GdeOstanovitsya();
                        transaction.replace(R.id.mainFrame, fragment);
                    }

                }else if(groupPos == 3) {
                    findViewById(R.id.mapImage).setVisibility(View.GONE);
                    findViewById(R.id.calendar).setVisibility(View.GONE);
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
                handler = new Handler();

                handler.postDelayed(new Runnable() {
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
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
        if (current instanceof EventsFragment) {
            ((EventsFragment) current).load();
        }


        getSupportFragmentManager()
                .beginTransaction()
                .detach(current)
                .attach(current)
                .commit();
        (current.getView().findViewById(R.id.linearEvents)).setVisibility(View.GONE);
        (current.getView().findViewById(R.id.filter)).setVisibility(View.GONE);
    }

    public void resetFilter(View view) {
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.mainFrame);
        ((TextView) current.getView().findViewById(R.id.txtTo)).setText(getResources().getString(R.string.to));
        ((TextView) current.getView().findViewById(R.id.txtFrom)).setText(getResources().getString(R.string.from));
        dateTimeTo = null;
        dateTimeFrom = null;

        SharedPreferences sharedPref = getSharedPreferences("time", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();

        ok(view);
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
        final Fragment current = getSupportFragmentManager().findFragmentById(R.id.mainFrame);
        mapVisible = !mapVisible;
        SharedPreferences sharedPref = getSharedPreferences("position", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("map", mapVisible);
        editor.commit();
        Log.d("MainActivity", "MapVisible: " + mapVisible);


        if (current instanceof EventsFragment) {
            ((EventsFragment) current).close();
        } else if (current instanceof EntertainmentFragment) {
            ((EntertainmentFragment) current).close();
        } else if (current instanceof SightSeeingsFragment) {
            ((SightSeeingsFragment) current).close();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager()
                        .beginTransaction()
                        .detach(current)
                        .attach(current)
                        .commit();
            }
        }, 500);
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

    private void loadRecyclerViewGdePoest() {
        Log.d("MainActivity", "Gde poest Starting loading data");
        if (gdePoestListItems.size() == 0) {
            Log.d("MainActivity", "Gde poest Really loading data");
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://89.219.32.107/api/v1/foods?limit=2000&page=1", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray("places");


                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            String image;
                            if (o.getJSONArray("images").length() > 0) {
                                image = o.getJSONArray("images").get(0).toString();
                            } else {
                                image = "http://imgur.com/a/jkAwJ";
                            }
                            GdePoestListItem item = new GdePoestListItem(
                                    o.getString("name"),
                                    o.getString("description"),
                                    image,
                                    o.getJSONObject("category").getString("name"),
                                    o.optString("lon"),
                                    o.optString("lat"),
                                    o.optString("phone"),
                                    o.optString("address"),
                                    o.getInt("id")
                            );

                            gdePoestListItems.add(item);

                        }

                        SharedPreferences prefs = getSharedPreferences("arraylist", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(gdePoestListItems);
                        editor.putString("gde", json);
                        editor.apply();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    String loc = getCurrentLocale().toString();
                    if (loc.startsWith("en")) {
                        params.put("Accept-Language", "en");
                    } else if (loc.startsWith("kk")) {
                        params.put("Accept-Language", "kz");
                    } else {
                        params.put("Accept-Language", "ru");
                    }
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    private void loadRecyclerViewGdeOst() {
        Log.d("MainAcivity", "Starting loading data");
        if (hotelsListItems.size() == 0) {
            Log.d("MainAcivity", "Really loading data");
            StringRequest stringRequest = new MyRequest(Request.Method.GET, "http://89.219.32.107/api/v1/hotels?limit=2000&page=1", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray("places");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            String image;
                            if (o.getJSONArray("images").length() > 0) {
                                image = o.getJSONArray("images").get(0).toString();
                            } else {
                                image = "http://imgur.com/a/jkAwJ";
                            }
                            HotelsListItem item = new HotelsListItem(
                                    o.getString("name"),
                                    o.getString("description"),
                                    image,
                                    o.getJSONObject("category").getString("name"),
                                    o.optString("lon"),
                                    o.optString("lat"),
                                    o.optString("phone"),
                                    o.optString("address"),
                                    o.getInt("stars"),
                                    o.optString("site"),
                                    o.getInt("id"),
                                    o.optString("book_url")
                            );

                            hotelsListItems.add(item);

                        }
                        SharedPreferences prefs = getSharedPreferences("arraylist", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(hotelsListItems);
                        editor.putString("hotel", json);
                        editor.apply();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    String loc = getCurrentLocale().toString();
                    if (loc.startsWith("en")) {
                        params.put("Accept-Language", "en");
                    } else if (loc.startsWith("kk")) {
                        params.put("Accept-Language", "kz");
                    } else {
                        params.put("Accept-Language", "ru");
                    }
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    public void loadRecyclerViewEvents() {
        if (eventsItemLists.size() == 0) {

            StringRequest stringRequest = new MyRequest(Request.Method.GET, "http://89.219.32.107/api/v1/places/events?limit=2000&page=1", new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray("places");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            String lon;
                            String lat;
                            if (o.getJSONArray("points").length() > 0) {
                                lon = o.getJSONArray("points").getJSONObject(0).optString("lon");
                                lat = o.getJSONArray("points").getJSONObject(0).optString("lat");
                            } else {
                                lon = "null";
                                lat = "null";
                            }
                            String image;
                            if (o.getJSONArray("images").length() != 0) {
                                image = o.getJSONArray("images").get(0).toString();
                            } else {
                                image = "http://imgur.com/a/jkAwJ";
                            }
                            EventsItemList item = new EventsItemList(
                                    o.getString("name"),
                                    o.getString("description"),
                                    image,
                                    o.getJSONObject("category").getString("name"),
                                    lon,
                                    lat,
                                    o.getInt("id"),
                                    o.getString("date"),
                                    o.getString("address"),
                                    "от 5000тг",
                                    o.getString("url_ticketon")
                            );
                            eventsItemLists.add(item);

                        }
                        SharedPreferences prefs = getSharedPreferences("arraylist", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(eventsItemLists);
                        editor.putString("event", json);
                        editor.apply();
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    String loc = getCurrentLocale().toString();
                    if (loc.startsWith("en")) {
                        params.put("Accept-Language", "en");
                    } else if (loc.startsWith("kk")) {
                        params.put("Accept-Language", "kz");
                    } else {
                        params.put("Accept-Language", "ru");
                    }
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

    private void loadRecyclerViewSight() {
        if (kudaShoditListItems.size() == 0) {
            StringRequest stringRequest = new MyRequest(Request.Method.GET, "http://89.219.32.107/api/v1/places/sightseeings?limit=2000&page=1", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray("places");


                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            KudaShoditListItem item = new KudaShoditListItem(
                                    o.getString("name"),
                                    o.getString("description"),
                                    o.getJSONArray("images").get(0).toString(),
                                    o.getJSONObject("category").getString("name"),
                                    o.getString("lon"),
                                    o.getString("lat"),
                                    o.getInt("id"),
                                    o.getString("address")
                            );
                            kudaShoditListItems.add(item);
                        }
                        SharedPreferences prefs = getSharedPreferences("arraylist", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(kudaShoditListItems);
                        editor.putString("kuda", json);
                        editor.apply();

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    String loc = getCurrentLocale().toString();
                    if (loc.startsWith("en")) {
                        params.put("Accept-Language", "en");
                    } else if (loc.startsWith("kk")) {
                        params.put("Accept-Language", "kz");
                    } else {
                        params.put("Accept-Language", "ru");
                    }
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

    private void loadRecyclerViewShopping() {

        SharedPreferences prefs = getSharedPreferences("firstTime", Context.MODE_PRIVATE);
        firstTime = prefs.getBoolean("firstTime", true);
        if (firstTime) {
            Log.d("MainActivity", "loading Really Shopping");
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://89.219.32.107/api/v1/places/shopping?limit=1000&page=1", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray("places");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            KudaShoditListItem item = new KudaShoditListItem(
                                    o.getString("name"),
                                    o.getString("description"),
                                    o.getJSONArray("images").get(0).toString(),
                                    o.getJSONObject("category").getString("name"),
                                    o.getString("lon"),
                                    o.getString("lat"),
                                    o.getInt("id"),
                                    o.getString("address")
                            );

                            item.setUrl("http://89.219.32.107/api/v1/places/shopping?limit=1000&page=1");
                            kudaShoditListItems.add(item);
                        }

                        SharedPreferences prefs = getSharedPreferences("firstTime", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("firstTime", false);
                        editor.apply();

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    String loc = getCurrentLocale().toString();
                    if (loc.startsWith("en")) {
                        params.put("Accept-Language", "en");
                    } else if (loc.startsWith("kk")) {
                        params.put("Accept-Language", "kz");
                    } else {
                        params.put("Accept-Language", "ru");
                    }
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);


        }
    }


    @Override
    public void onAttachFragment(Fragment fragment) {
        fragList.add(new WeakReference(fragment));
    }

    public void deleteFragments() {
        for (WeakReference<Fragment> ref : fragList) {
            Fragment f = ref.get();
            if (f != null) {
                if (f.isVisible()) {
                    f.onDestroy();
                    f.onDetach();
                    Log.d("MainActivity", "Some fragments were deleted");
                }
            }
        }
    }
}
