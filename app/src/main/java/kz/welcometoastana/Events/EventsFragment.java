package kz.welcometoastana.Events;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
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
    static final float COORDINATE_OFFSET = 0.0002f;
    public TabLayout tabLayout;
    public ViewPager viewPager;
    MapView mMapView;
    LatLng astana = new LatLng(51.149202, 71.439285);
    HashMap<String, String> markerLocation = new HashMap<>();
    List<Marker> markerList = new ArrayList<>();
    private GoogleMap googleMap;
    private String Url;
    private List<EventsItemList> eventsItemLists;

    private static String makeFragmentName(int viewId, int position) {
        return "android:switcher:" + viewId + ":" + position;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.events_layout, null);

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

        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        eventsItemLists = new ArrayList<>();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                mMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap mMap) {
                        googleMap = mMap;
                        for (Marker marker : markerList) {
                            marker.remove();
                        }
                        eventsItemLists.clear();
                        markerLocation.clear();
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

                        StringRequest stringRequest = new MyRequest(Request.Method.GET, Url, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {


                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray array = jsonObject.getJSONArray("places");
                                    Log.d("AllEvents", "size of array: " + array.length());
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

                                    Log.d("EventsFragment", "size: " + eventsItemLists.size());
                                    for (int i = 0; i < eventsItemLists.size(); i++) {
                                        final EventsItemList eventsItemList = eventsItemLists.get(i);
                                        if (!eventsItemList.getLat().equals("null") && !eventsItemList.getImageUrl().startsWith("http://imgur.com")) {
                                            final float lat = Float.parseFloat(eventsItemList.getLat());
                                            final float lng = Float.parseFloat(eventsItemList.getLon());

                                            Glide.with(EventsFragment.this).
                                                    load(eventsItemList.getImageUrl())
                                                    .asBitmap()
                                                    .override(75, 75)
                                                    .centerCrop()
                                                    .into(new SimpleTarget<Bitmap>() {
                                                        @Override
                                                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                                                            Bitmap bitmap1 = getCircularBitmap(bitmap);
                                                            Marker marker = googleMap.addMarker(new MarkerOptions().position(coordinateForMarker(lat, lng)).title(eventsItemList.getName()));
                                                            marker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap1));
                                                            markerList.add(marker);
                                                        }
                                                    });
                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Sightseeings", error.toString());
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


                        CameraPosition cameraPosition = new CameraPosition.Builder().target(astana).zoom(11).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                });

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
        super.onDestroy();
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
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
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
}
