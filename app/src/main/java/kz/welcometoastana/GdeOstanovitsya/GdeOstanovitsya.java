package kz.welcometoastana.GdeOstanovitsya;


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
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
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

import kz.welcometoastana.R;
import kz.welcometoastana.utility.FixedSpeedScroller;
import kz.welcometoastana.utility.MyRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class GdeOstanovitsya extends Fragment {

    static final float COORDINATE_OFFSET = 0.0002f;
    public TabLayout tabLayout;
    public ViewPager viewPager;
    MapView mMapView;
    HashMap<String, String> markerLocation = new HashMap<>();
    List<Marker> markerList = new ArrayList<>();
    Map<Marker, HotelsListItem> markerMap;
    private GoogleMap googleMap;
    private String Url;
    private List<HotelsListItem> kudaShoditListItems;
    private BottomSheetBehavior mBottomSheetBehavior;
    private boolean map = false;

    public GdeOstanovitsya() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_gde_ostanovitsya, container, false);

        tabLayout = (TabLayout) v.findViewById(R.id.tabsHotel);
        viewPager = (ViewPager) v.findViewById(R.id.viewpagerHotel);
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


        SharedPreferences sharedPref = getActivity().getSharedPreferences("position", Context.MODE_PRIVATE);
        final int position = sharedPref.getInt("Gposition", 0);
        viewPager.setCurrentItem(position);


        View bottomSheet = v.findViewById(R.id.nested);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        map = sharedPref.getBoolean("map", false);
        if (map) {
                viewPager.setVisibility(View.GONE);
                v.findViewById(R.id.mapView).setVisibility(View.VISIBLE);
            } else {
                viewPager.setVisibility(View.VISIBLE);
                v.findViewById(R.id.mapView).setVisibility(View.GONE);
            }

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            tabLayout.getTabAt(position).select();
                        } catch (Exception e) {
                            e.printStackTrace();
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
                        SharedPreferences sharedPref = getActivity().getSharedPreferences("position", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("Gposition", a);
                        editor.commit();

                        if (a == 0) {
                            Url = "http://89.219.32.107/api/v1/hotels?limit=2000&page=1";
                        } else if (a == 1) {
                            Url = "http://89.219.32.107/api/v1/hotels?limit=20&page=1&category=7";
                        } else if (a == 2) {
                            Url = "http://89.219.32.107/api/v1/hotels?limit=20&page=1&category=35";
                        } else if (a == 3) {
                            Url = "http://89.219.32.107/api/v1/hotels?limit=20&page=1&category=36";
                        }

                        StringRequest stringRequest = new MyRequest(Request.Method.GET, Url, new Response.Listener<String>() {

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

                                        kudaShoditListItems.add(item);
                                    }


                                    int size = kudaShoditListItems.size();
                                    for (int i = 0; i < size; i++) {
                                        final HotelsListItem hotelsListItem = kudaShoditListItems.get(i);
                                        if (!hotelsListItem.getLat().equals("null") && !hotelsListItem.getImageUrl().startsWith("http://imgur.com")) {
                                            final float lat = Float.parseFloat(hotelsListItem.getLat());
                                            final float lng = Float.parseFloat(hotelsListItem.getLon());
                                            final Marker marker = googleMap.addMarker(new MarkerOptions().position(coordinateForMarker(lat, lng)).title(hotelsListItem.getName()));
                                            marker.setVisible(false);
                                            markerList.add(marker);
                                            markerMap.put(marker, hotelsListItem);
                                            Glide.with(getActivity()).
                                                    load(hotelsListItem.getImageUrl())
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
                                                                e.printStackTrace();
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
                                final HotelsListItem hotelsListItem = markerMap.get(marker);

                                if (hotelsListItem != null) {
                                    ((TextView) v.findViewById(R.id.name)).setText(hotelsListItem.getName());
                                    ((TextView) v.findViewById(R.id.category)).setText(hotelsListItem.getCategory());
                                    if (hotelsListItem.getAddress().length() < 2) {
                                        v.findViewById(R.id.address).setVisibility(View.GONE);
                                    } else {
                                        ((TextView) v.findViewById(R.id.address)).setText(hotelsListItem.getAddress());
                                    }

                                    RatingBar ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
                                    LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();

                                    setRatingStarColor(stars.getDrawable(2), ContextCompat.getColor(getActivity(), R.color.foreground));
                                    // Half filled stars
                                    setRatingStarColor(stars.getDrawable(1), ContextCompat.getColor(getActivity(), R.color.background));
                                    // Empty stars
                                    setRatingStarColor(stars.getDrawable(0), ContextCompat.getColor(getActivity(), R.color.background));
                                    ratingBar.setRating(hotelsListItem.getStars());


                                    v.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                                        }
                                    });
                                    v.findViewById(R.id.fromMap).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getActivity(), GdeOstanovitsyaDescription.class);
                                            intent.putExtra("name", hotelsListItem.getName());
                                            intent.putExtra("id", hotelsListItem.getId());
                                            intent.putExtra("description", hotelsListItem.getSummary());
                                            intent.putExtra("imageUrl", hotelsListItem.getImageUrl());
                                            intent.putExtra("category", hotelsListItem.getCategory());
                                            intent.putExtra("longit", hotelsListItem.getLon());
                                            intent.putExtra("latit", hotelsListItem.getLat());
                                            intent.putExtra("address", hotelsListItem.getAddress());
                                            intent.putExtra("url", Url);
                                            intent.putExtra("phone", hotelsListItem.getPhone());
                                            intent.putExtra("website", hotelsListItem.getWebsite());
                                            intent.putExtra("stars", hotelsListItem.getStars());
                                            intent.putExtra("urlItem", hotelsListItem.getBook_url());
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

    private String makeFragmentName(int viewId, int position) {
        return "android:switcher:" + viewId + ":" + position;
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
        //mMapView.onDestroy();
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

    public void close() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void setRatingStarColor(Drawable drawable, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DrawableCompat.setTint(drawable, color);
        } else {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }

}
