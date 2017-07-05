package kz.welcometoastana.Entertainment;


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
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kz.welcometoastana.Events.AllEvents;
import kz.welcometoastana.Events.Concerts;
import kz.welcometoastana.Events.ExpoEvent;
import kz.welcometoastana.Events.Sports;
import kz.welcometoastana.Events.Theatre;
import kz.welcometoastana.Events.Vystavki;
import kz.welcometoastana.KudaShoditListItem;
import kz.welcometoastana.R;
import kz.welcometoastana.Sightseeings.DescriptionActivity;
import kz.welcometoastana.utility.FixedSpeedScroller;
import kz.welcometoastana.utility.MyRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class EntertainmentFragment extends Fragment {

    static final float COORDINATE_OFFSET = 0.0002f;
    public TabLayout tabLayout;
    public ViewPager viewPager;
    MapView mMapView;
    HashMap<String, String> markerLocation = new HashMap<>();
    List<Marker> markerList = new ArrayList<>();
    Map<Marker, KudaShoditListItem> markerMap;
    private GoogleMap googleMap;
    private String Url;
    private List<KudaShoditListItem> kudaShoditListItems;
    private BottomSheetBehavior mBottomSheetBehavior;
    private boolean map = false;

    public EntertainmentFragment() {
        // Required empty public constructor
    }

    private String makeFragmentName(int viewId, int position) {
        return "android:switcher:" + viewId + ":" + position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_entertainment, container, false);

        tabLayout = (TabLayout) v.findViewById(R.id.tabsEnter);
        viewPager = (ViewPager) v.findViewById(R.id.viewpagerEnter);
        //set an adpater

        viewPager.setAdapter(new MyAdapter(getChildFragmentManager(), getActivity()));
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext());
            // scroller.setFixedDuration(5000);
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

        View bottomSheet = v.findViewById(R.id.nested);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("position", Context.MODE_PRIVATE);
        map = sharedPref.getBoolean("map", false);


        if (map) {
                v.findViewById(R.id.viewpagerEnter).setVisibility(View.GONE);
                v.findViewById(R.id.mapView).setVisibility(View.VISIBLE);
            } else {
                v.findViewById(R.id.viewpagerEnter).setVisibility(View.VISIBLE);
                v.findViewById(R.id.viewpagerEnter).bringToFront();
                v.findViewById(R.id.mapView).setVisibility(View.GONE);
            }

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
        kudaShoditListItems = new ArrayList<>();
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

                            kudaShoditListItems.clear();
                            markerLocation.clear();
                            markerMap.clear();
                            markerList.clear();
                            // For dropping a marker at a point on the Map

                            int a = tabLayout.getSelectedTabPosition();
                            if (a == 0) {
                                Url = "http://89.219.32.107/api/v1/places/shopping?limit=1000&page=1&category=6";
                            } else if (a == 1) {
                                Url = "http://89.219.32.107/api/v1/places/shopping?limit=1000&page=1&category=30";
                            } else if (a == 2) {
                                Url = "http://89.219.32.107/api/v1/places/shopping?limit=1000&page=1&category=40";
                            } else if (a == 3) {
                                Url = "http://89.219.32.107/api/v1/places/shopping?limit=1000&page=1&category=41";
                            } else if (a == 4) {
                                Url = "http://89.219.32.107/api/v1/places/shopping?limit=1000&page=1&category=46";
                            } else if (a == 5) {
                                Url = "http://89.219.32.107/api/v1/places/shopping?limit=1000&page=1&category=63";
                            }

                            StringRequest stringRequest = new MyRequest(Request.Method.GET, Url, new Response.Listener<String>() {

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

                                        int size = kudaShoditListItems.size();
                                        for (int i = 0; i < size; i++) {
                                            final KudaShoditListItem kudaShoditListItem = kudaShoditListItems.get(i);
                                            if (!kudaShoditListItem.getLat().equals("null") && !kudaShoditListItem.getImageUrl().startsWith("http://imgur.com")) {
                                                float lat = Float.parseFloat(kudaShoditListItem.getLat());
                                                float lng = Float.parseFloat(kudaShoditListItem.getLon());
                                                final Marker marker = googleMap.addMarker(new MarkerOptions().position(coordinateForMarker(lat, lng)).title(kudaShoditListItem.getName()));
                                                marker.setVisible(false);
                                                markerList.add(marker);
                                                markerMap.put(marker, kudaShoditListItem);
                                                Glide.with(getActivity().getApplicationContext()).
                                                        load(kudaShoditListItem.getImageUrl())
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
                                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                    final KudaShoditListItem kuda = markerMap.get(marker);

                                    if (kuda != null) {
                                        ((TextView) v.findViewById(R.id.name)).setText(kuda.getName());
                                        ((TextView) v.findViewById(R.id.category)).setText(kuda.getCategory());
                                        if (kuda.getAddress().length() < 2) {
                                            v.findViewById(R.id.address).setVisibility(View.GONE);
                                        } else {
                                            ((TextView) v.findViewById(R.id.address)).setText(kuda.getAddress());
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
                                                Intent intent = new Intent(getActivity(), DescriptionActivity.class);
                                                intent.putExtra("name", kuda.getName());
                                                intent.putExtra("id", kuda.getId());
                                                intent.putExtra("description", kuda.getSummary());
                                                intent.putExtra("imageUrl", kuda.getImageUrl());
                                                intent.putExtra("category", kuda.getCategory());
                                                intent.putExtra("longit", kuda.getLon());
                                                intent.putExtra("latit", kuda.getLat());
                                                intent.putExtra("url", Url);
                                                intent.putExtra("address", kuda.getAddress());
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
        super.onDestroy();


        FragmentPagerAdapter fragmentPagerAdapter = (FragmentPagerAdapter) viewPager.getAdapter();
        for (int i = 0; i < fragmentPagerAdapter.getCount(); i++) {
            String name = makeFragmentName(viewPager.getId(), i);
            Fragment viewPagerFragment = getChildFragmentManager().findFragmentByTag(name);
            if (viewPagerFragment != null) {
                // Interact with any views/data that must be alive
                viewPagerFragment.onDestroy();
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
        try {
            mMapView.onDestroy();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        float r;

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
