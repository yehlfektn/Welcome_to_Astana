package kz.welcometoastana.Events;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kz.welcometoastana.R;
import kz.welcometoastana.utility.FixedSpeedScroller;
import kz.welcometoastana.utility.MyRequest;

/**
 * Created by nurdaulet on 5/3/17.
 */

public class EventsFragment extends Fragment {
    static final float COORDINATE_OFFSET = 0.0009f;
    public TabLayout tabLayout;
    public ViewPager viewPager;
    SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd");
    MapView mMapView;
    HashMap<String, String> markerLocation = new HashMap<>();
    List<Marker> markerList = new ArrayList<>();
    Map<Marker, EventsItemList> markerMap;
    private GoogleMap googleMap;
    private String Url;
    private List<EventsItemList> eventsItemLists;
    private BottomSheetBehavior mBottomSheetBehavior;
    private Boolean map = false;
    private RequestManager glide;


    private static String makeFragmentName(int viewId, int position) {
        return "android:switcher:" + viewId + ":" + position;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.events_layout, container, false);

        View bottomSheet = v.findViewById(R.id.nested);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        if (glide == null) {
            glide = Glide.with(this);
        }

        SharedPreferences sharedPref = getActivity().getSharedPreferences("position", Context.MODE_PRIVATE);
        map = sharedPref.getBoolean("map", false);

        if (map) {
                v.findViewById(R.id.viewpagerEvent).setVisibility(View.GONE);
                v.findViewById(R.id.mapView).setVisibility(View.VISIBLE);
            } else {
                v.findViewById(R.id.viewpagerEvent).setVisibility(View.VISIBLE);
                v.findViewById(R.id.viewpagerEvent).bringToFront();
                v.findViewById(R.id.mapView).setVisibility(View.GONE);
            }

        tabLayout = (TabLayout) v.findViewById(R.id.tabsEvent);
        viewPager = (ViewPager) v.findViewById(R.id.viewpagerEvent);
        getActivity().findViewById(R.id.calendar).setVisibility(View.VISIBLE);

        //set an adapter
        viewPager.setAdapter(new MyAdapter(getChildFragmentManager(), getActivity()));

        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext());
            mScroller.set(viewPager, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        if (tabLayout.getTabAt(tabLayout.getSelectedTabPosition()) != null) {
                            tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).select();
                        }
                    }
                }, 500);

        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        eventsItemLists = new ArrayList<>();
        markerMap = new HashMap<>();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                if (map) {
                mMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap mMap) {
                        googleMap = mMap;
                        for (Marker marker : markerList) {
                            try {
                                marker.remove();
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            }
                        }
                        eventsItemLists.clear();
                        markerLocation.clear();
                        markerMap.clear();
                        markerList.clear();
                        // For dropping a marker at a point on the Map

                        int a = tabLayout.getSelectedTabPosition();
                        if (a == 0) {
                            Url = "http://89.219.32.107/api/v1/places/events?limit=2000&page=1";
                        } else if (a == 1) {
                            Url = "http://89.219.32.107/api/v1/places/events?limit=200&page=1&category=76";
                        } else if (a == 2) {
                            Url = "http://89.219.32.107/api/v1/places/events?limit=200&page=1&category=12";
                        } else if (a == 3) {
                            Url = "http://89.219.32.107/api/v1/places/events?limit=200&page=1&category=13";
                        } else if (a == 4) {
                            Url = "http://89.219.32.107/api/v1/places/events?limit=200&page=1&category=43";
                        } else if (a == 5) {
                            Url = "http://89.219.32.107/api/v1/places/events?limit=200&page=1&category=68";
                        }


                        SharedPreferences sharedPref = getContext().getSharedPreferences("time", Context.MODE_PRIVATE);
                        long timefrom = sharedPref.getLong("dateTimeFrom", 0);
                        long timeTo = sharedPref.getLong("dateTimeTo", 0);

                        if (timefrom != 0 && timeTo != 0) {
                            Calendar dateFrom = new GregorianCalendar();
                            Calendar dateTo = new GregorianCalendar();
                            dateFrom.setTimeInMillis(timefrom);
                            dateTo.setTimeInMillis(timeTo);
                            Url = Url + "&from=" + formatDateTime.format(dateFrom.getTime()) + "&to=" + formatDateTime.format(dateTo.getTime());
                        }


                        StringRequest stringRequest = new MyRequest(Request.Method.GET, Url, new Response.Listener<String>() {

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
                                                o.getString("url")
                                        );
                                        eventsItemLists.add(item);
                                    }

                                    for (int i = 0; i < eventsItemLists.size(); i++) {
                                        final EventsItemList eventsItemList = eventsItemLists.get(i);
                                        if (!eventsItemList.getLat().equals("null") && !eventsItemList.getImageUrl().startsWith("http://imgur.com")) {
                                            float lat = Float.parseFloat(eventsItemList.getLat());
                                            float lng = Float.parseFloat(eventsItemList.getLon());
                                            final Marker marker = googleMap.addMarker(new MarkerOptions().position(coordinateForMarker(lat, lng)).title(eventsItemList.getName()));
                                            marker.setVisible(false);
                                            markerList.add(marker);
                                            markerMap.put(marker, eventsItemList);

                                            glide.
                                                    load(eventsItemList.getImageUrl())
                                                    .asBitmap()
                                                    .override(75, 75)
                                                    .centerCrop()
                                                    .into(new SimpleTarget<Bitmap>() {
                                                        @Override
                                                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                                            Bitmap bitmap1 = getCircularBitmap(bitmap);
                                                            try {
                                                                marker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap1));
                                                                marker.setVisible(true);
                                                            } catch (IllegalArgumentException e) {
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                    if (markerList.size() != 0) {
                                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                        for (Marker marker : markerList) {
                                            builder.include(marker.getPosition());
                                        }
                                        LatLngBounds bounds = builder.build();
                                        DisplayMetrics metrics = new DisplayMetrics();
                                        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
                                        int padding;
                                        if (metrics.heightPixels >= 1280) {
                                            padding = 200;
                                        } else {
                                            padding = 100;
                                        }
                                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, metrics.widthPixels, metrics.heightPixels, padding);
                                        googleMap.animateCamera(cu);
                                    }

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
                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                        requestQueue.add(stringRequest);


                        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {

                                final EventsItemList eventsItemList = markerMap.get(marker);

                                if (eventsItemList != null) {
                                    ((TextView) v.findViewById(R.id.name)).setText(eventsItemList.getName());
                                    ((TextView) v.findViewById(R.id.date)).setText(eventsItemList.getDate());
                                    ((TextView) v.findViewById(R.id.category)).setText(eventsItemList.getCategory());
                                    if (eventsItemList.getAddress().length() < 2) {
                                        v.findViewById(R.id.address).setVisibility(View.GONE);
                                    } else {
                                        ((TextView) v.findViewById(R.id.address)).setText(eventsItemList.getAddress());
                                    }
                                    v.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                                        }
                                    });
                                    v.findViewById(R.id.fromMap).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getActivity(), EventsDescription.class);
                                            intent.putExtra("name", eventsItemList.getName());
                                            intent.putExtra("id", eventsItemList.getId());
                                            intent.putExtra("description", eventsItemList.getSummary());
                                            intent.putExtra("imageUrl", eventsItemList.getImageUrl());
                                            intent.putExtra("category", eventsItemList.getCategory());
                                            intent.putExtra("longit", eventsItemList.getLon());
                                            intent.putExtra("latit", eventsItemList.getLat());
                                            intent.putExtra("url", Url);
                                            intent.putExtra("address", eventsItemList.getAddress());
                                            intent.putExtra("money", eventsItemList.getMoney());
                                            intent.putExtra("date", eventsItemList.getDate());
                                            intent.putExtra("urlItem", eventsItemList.getUrl());
                                            startActivityForResult(intent, 0);
                                            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                        }
                                    });

                                }
                                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                                return false;
                            }
                        });
                    }
                });

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDestroy() {
        if (mMapView != null) {
            try {
                mMapView.onDestroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d("EventsFragment", "OnDestroy was activated");
        if (viewPager != null) {
            FragmentPagerAdapter fragmentPagerAdapter = (FragmentPagerAdapter) viewPager.getAdapter();
            Log.d("EventsFragment", "Fragment count" + fragmentPagerAdapter.getCount());
            for (int i = 0; i < fragmentPagerAdapter.getCount(); i++) {
                String name = makeFragmentName(viewPager.getId(), i);
                Fragment viewPagerFragment = getChildFragmentManager().findFragmentByTag(name);
                if (viewPagerFragment != null) {
                    // Interact with any views/data that must be alive
                    Log.d("EventsFragment", "" + viewPagerFragment.getTag() + "is ondestroy");
                    if (viewPagerFragment instanceof AllEvents) {
                        Log.d("EventsFragment", "All events is on destroy");
                    }
                    viewPagerFragment.onDestroy();
                }
            }
        }

        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }


        super.onDestroy();
    }

    @Override
    public void onResume() {

        if (mMapView != null) {
            try {
                mMapView.onResume();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onResume();
    }

    @Override
    public void onPause() {

        if (mMapView != null) {
            try {
                mMapView.onPause();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onPause();
    }

    @Override
    public void onLowMemory() {

        if (mMapView != null) {
            try {
                mMapView.onLowMemory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onLowMemory();
    }

    public Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        float r = 0;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        } else {
            r = bitmap.getWidth() / 2;
        }

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private Locale getCurrentLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return getResources().getConfiguration().locale;
        }
    }

    private LatLng coordinateForMarker(float latitude, float longitude) {

        String[] location = new String[2];

        for (int i = 0; i <= 100; i++) {

            if (mapAlreadyHasMarkerForLocation((latitude + i * COORDINATE_OFFSET)
                    + "," + (longitude + i * COORDINATE_OFFSET))) {
                // If i = 0 then below if condition is same as upper one. Hence, no need to execute below if condition.
                if (i == 0)
                    continue;

                if (mapAlreadyHasMarkerForLocation((latitude - i * COORDINATE_OFFSET)
                        + "," + (longitude - i * COORDINATE_OFFSET))) {

                    continue;

                } else {
                    location[0] = latitude - (i * COORDINATE_OFFSET) + "";
                    location[1] = longitude - (i * COORDINATE_OFFSET) + "";
                    break;
                }

            } else {
                location[0] = latitude + (i * COORDINATE_OFFSET) + "";
                location[1] = longitude + (i * COORDINATE_OFFSET) + "";
                break;
            }
        }

        if (location[0] != null) {
            return new LatLng(Float.parseFloat(location[0]), Float.parseFloat(location[1]));
        } else {
            return new LatLng(latitude, longitude);
        }
    }

    // Return whether marker with same location is already on map
    private boolean mapAlreadyHasMarkerForLocation(String location) {
        if (markerLocation.containsValue(location)) {
            return true;
        } else {
            markerLocation.put(location, location);
            return false;
        }
    }

    public void load() {

        FragmentPagerAdapter fragmentPagerAdapter = (FragmentPagerAdapter) viewPager.getAdapter();
        for (int i = 0; i < fragmentPagerAdapter.getCount(); i++) {
            String name = makeFragmentName(viewPager.getId(), i);
            Fragment viewPagerFragment = getChildFragmentManager().findFragmentByTag(name);
            if (viewPagerFragment != null) {
                // Interact with any views/data that must be alive
                if (viewPagerFragment instanceof AllEvents) {
                    ((AllEvents) viewPagerFragment).loadRecyclerView();
                } else if (viewPagerFragment instanceof ExpoEvent) {
                    ((ExpoEvent) viewPagerFragment).loadRecyclerView();
                } else if (viewPagerFragment instanceof Concerts) {
                    ((Concerts) viewPagerFragment).loadRecyclerView();
                } else if (viewPagerFragment instanceof Vystavki) {
                    ((Vystavki) viewPagerFragment).loadRecyclerView();
                } else if (viewPagerFragment instanceof Theatre) {
                    ((Theatre) viewPagerFragment).loadRecyclerView();
                } else if (viewPagerFragment instanceof Sports) {
                    ((Sports) viewPagerFragment).loadRecyclerView();
                }
            }
        }
    }

    public void close() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }
}
