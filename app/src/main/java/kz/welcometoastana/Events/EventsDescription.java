package kz.welcometoastana.Events;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import at.blogc.android.views.ExpandableTextView;
import kz.welcometoastana.GdeOstanovitsya.HotelsListItem;
import kz.welcometoastana.GdePoest.GdePoestListItem;
import kz.welcometoastana.MainActivity;
import kz.welcometoastana.R;
import kz.welcometoastana.utility.AdapterforNearby;
import kz.welcometoastana.utility.DashedUnderlineSpan;
import kz.welcometoastana.utility.MyRequest;
import kz.welcometoastana.utility.ViewPagerAdapter;
import kz.welcometoastana.utility.listItemNearby;


public class EventsDescription extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public TabLayout tabLayout;
    public ViewPager viewPagerNext;
    public ScrollView mScrollView;
    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;
    double lat, lng;
    //= 51.128249084968,lng = 71.430494032634;
    String lngStr, latStr;
    int id;
    ViewPager viewPager;
    LinearLayout linearLayout;
    double lat2, lng2;
    LocationRequest mLocationRequest;
    private int dotscount;
    private ImageView[] dots;
    private ArrayList<String> imageUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra("category"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, new int[]{0xffFF5800, 0xffFF8B00});
        getSupportActionBar().setBackgroundDrawable(g);

        final TextView name = (TextView) findViewById(R.id.name);
        TextView address = (TextView) findViewById(R.id.address);
        TextView date = (TextView)findViewById(R.id.date);
        Button frameButton = (Button)findViewById(R.id.buttonFrame);
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        frameButton.bringToFront();
        String loc = getCurrentLocale().toString();
        if (loc.startsWith("kk")) {
            int pixels = (int) (18 * scale + 0.5f);
            frameButton.setCompoundDrawablePadding(-pixels);
            pixels = (int) (10 * scale + 0.5f);
            frameButton.setPadding(0, 0, 0, pixels);
        }

        if (getIntent().getStringExtra("address").length() < 2) {
            address.setVisibility(View.GONE);
        } else {
            address.setText(getIntent().getStringExtra("address"));
        }

        date.setText(getIntent().getStringExtra("date"));
        name.setText(getIntent().getStringExtra("name"));

        final String Url=getIntent().getStringExtra("url");
        Log.d("DescriptionActivity", Url);

        if(MainActivity.gpsLocation != null){

            lat2 = MainActivity.gpsLocation.getLatitude();
            lng2 = MainActivity.gpsLocation.getLongitude();
            Log.d("DescriptionActivity", "lat: "+lat2+"lon: "+lng2);

        }
        //---------------Animated text--------------
        final ExpandableTextView expandableTextView = (ExpandableTextView) this.findViewById(R.id.summary);
        final String big = getIntent().getStringExtra("description");
        expandableTextView.setText(big);
        final Shader p = expandableTextView.getPaint().getShader();

        final Shader textShader=new LinearGradient(0, 100, 0, 250, new int[]{Color.BLACK,Color.WHITE}, new float[]{0, 1}, Shader.TileMode.CLAMP);
        expandableTextView.getPaint().setShader(textShader);
        final TextView OpenCollapse = (TextView)this.findViewById(R.id.openCollapse);

        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getResources().getString(R.string.read_more));

        int intSpannableStringBuilderLength = spannableStringBuilder.length();

        spannableStringBuilder.setSpan(
                new DashedUnderlineSpan(OpenCollapse, ContextCompat.getColor(this, R.color.gray),
                        getResources().getDimension(R.dimen.dus_stroke_thickness),
                        getResources().getDimension(R.dimen.dus_dash_path),
                        getResources().getDimension(R.dimen.dus_offset_y),
                        getResources().getDimension(R.dimen.dus_spacing_extra)), 0,
                intSpannableStringBuilderLength, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        OpenCollapse.setText(spannableStringBuilder);


        expandableTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v){
                if (expandableTextView.isExpanded())
                {
                    expandableTextView.collapse();
                    OpenCollapse.setText(spannableStringBuilder);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (expandableTextView.isExpanded()) {
                                expandableTextView.getPaint().setShader(textShader);
                            }
                        }
                    }, 1000);
                }
                else
                {
                    expandableTextView.getPaint().setShader(p);
                    expandableTextView.expand();
                    OpenCollapse.setText("");
                }
            }

        });
        OpenCollapse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                if (expandableTextView.isExpanded())
                {
                    //
                    expandableTextView.collapse();
                    OpenCollapse.setText(spannableStringBuilder);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (expandableTextView.isExpanded()) {
                                expandableTextView.getPaint().setShader(textShader);
                            }
                        }
                    }, 1000);
                }
                else
                {
                    expandableTextView.getPaint().setShader(p);
                    expandableTextView.expand();
                    OpenCollapse.setText("");
                }
            }
        });


        //----------------------End of Animated Text --------------------


        if (googleServicesAvailable()) {
            initMap();
        }

        id = getIntent().getIntExtra("id", 0);

        imageUrls = new ArrayList<>();
        linearLayout = (LinearLayout) findViewById(R.id.LinearSlider);


        StringRequest stringRequest = new MyRequest(Request.Method.GET, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("places");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        if (o.getInt("id") == id) {
                            JSONArray arr = o.getJSONArray("images");
                            latStr = getIntent().getStringExtra("latit");
                            lngStr = getIntent().getStringExtra("longit");
                            if (lngStr.equals("null")) {
                                lngStr = "0";
                                latStr = "0";
                            }
                            lat = Double.parseDouble(latStr);
                            lng = Double.parseDouble(lngStr);
                            goToLocationZoom(lat, lng, 15);
                            setMarker(getIntent().getStringExtra("name"), lat, lng);
                            if(arr.length()==0){

                                imageUrls.add("http://imgur.com/bpx2TrL");
                            }else {

                                for (int j = 0; j < arr.length(); j++) {
                                    imageUrls.add(arr.get(j).toString());
                                }
                            }
                        }
                    }

                    viewPager = (ViewPager) findViewById(R.id.viewPager);
                    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getApplicationContext(), imageUrls);
                    viewPager.setAdapter(viewPagerAdapter);
                    dotscount = viewPagerAdapter.getCount();
                    dots = new ImageView[dotscount];

                    for (int i = 0; i < dotscount; i++) {

                        dots[i] = new ImageView(getApplicationContext());
                        dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(8, 0, 8, 0);
                        linearLayout.addView(dots[i], params);

                    }
                    dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

                    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            for (int i = 0; i < dotscount; i++) {
                                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                            }
                            dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });

                    if (imageUrls.size() < 2) {
                        linearLayout.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("EventsDescription", error.toString());
            }
        });

        StringRequest nearbyRequest = new MyRequest(Request.Method.GET, "http://89.219.32.107/api/v1/nearby?lat=51&lon=41", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                ArrayList<EventsItemList> arrayListEvents = new ArrayList<>();
                ArrayList<HotelsListItem> hotelsListItems = new ArrayList<>();
                ArrayList<GdePoestListItem> gdePoestListItems = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject object = jsonObject.getJSONObject("data");
                    JSONArray array = object.getJSONArray("sightseeings");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        EventsItemList item = new EventsItemList(
                                o.getString("name"),
                                o.getString("description"),
                                o.getJSONArray("images").get(0).toString(),
                                o.getJSONObject("category").getString("name"),
                                o.optString("lon"),
                                o.optString("lat"),
                                o.getInt("id"),
                                o.getString("date"),
                                o.getString("address"),
                                "от 5000тг",
                                o.getString("url")
                        );

                        arrayListEvents.add(item);
                    }


                    array = object.getJSONArray("hotels");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        HotelsListItem item = new HotelsListItem(
                                o.getString("name"),
                                o.getString("description"),
                                o.getJSONArray("images").get(0).toString(),
                                o.getJSONObject("category").getString("name"),
                                o.optString("lon"),
                                o.optString("lat"),
                                o.optString("phone"),
                                o.optString("address"),
                                o.getInt("stars"),
                                o.optString("site"),
                                o.getInt("id")
                        );

                        hotelsListItems.add(item);

                    }

                    array = object.getJSONArray("foods");
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("EventsDescription", "eventsSize: " + arrayListEvents.size());

                listItemNearby list = new listItemNearby(arrayListEvents, hotelsListItems, gdePoestListItems);

                tabLayout = (TabLayout) findViewById(R.id.tabsEvent);
                viewPagerNext = (ViewPager) findViewById(R.id.viewpagerEvent);
                mScrollView = (ScrollView) findViewById(R.id.scrollView);
                viewPagerNext.setAdapter(new AdapterforNearby(getApplicationContext(), list));
                viewPagerNext.setOnTouchListener(new View.OnTouchListener() {

                    int dragthreshold = 30;
                    int downX;
                    int downY;

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                downX = (int) event.getRawX();
                                downY = (int) event.getRawY();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                int distanceX = Math.abs((int) event.getRawX() - downX);
                                int distanceY = Math.abs((int) event.getRawY() - downY);

                                if (distanceY > distanceX && distanceY > dragthreshold) {
                                    viewPagerNext.getParent().requestDisallowInterceptTouchEvent(false);
                                    mScrollView.getParent().requestDisallowInterceptTouchEvent(true);
                                } else if (distanceX > distanceY && distanceX > dragthreshold) {
                                    viewPagerNext.getParent().requestDisallowInterceptTouchEvent(true);
                                    mScrollView.getParent().requestDisallowInterceptTouchEvent(false);
                                }
                                break;
                            case MotionEvent.ACTION_UP:
                                mScrollView.getParent().requestDisallowInterceptTouchEvent(false);
                                viewPagerNext.getParent().requestDisallowInterceptTouchEvent(false);
                                break;
                        }
                        return false;
                    }
                });
                tabLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        tabLayout.setupWithViewPager(viewPagerNext);
                    }
                });


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("EventsDescription", error.toString());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        requestQueue.add(nearbyRequest);
    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
    }
    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Can't connect to play services", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;


        if(mGoogleMap != null){


            mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    EventsDescription.this.setMarker("Local", latLng.latitude, latLng.longitude);
                }
            });
            mGoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {
                }
                @Override
                public void onMarkerDrag(Marker marker) {
                }
                @Override
                public void onMarkerDragEnd(Marker marker) {
                    marker.setTitle(getIntent().getStringExtra("name"));
                    marker.showInfoWindow();
                }
            });
            mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter(){
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }
                @Override
                public View getInfoContents(Marker marker) {
                    return null;
                }
            });
        }
    }


    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mGoogleMap.moveCamera(update);
    }

    private void setMarker(String locality, double lat, double lng) {
        MarkerOptions options = new MarkerOptions()
                .title(locality)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("icon_marker", 50, 50)))
                .position(new LatLng(lat, lng));
        mGoogleMap.addMarker(options).showInfoWindow();
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location == null){
            Toast.makeText(this, "Can't get current location", Toast.LENGTH_LONG).show();
        } else {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            lat2=location.getLatitude();
            lng2=location.getLongitude();
            Log.d("myLat", String.valueOf(location.getLatitude()));
            Log.d("myLng", String.valueOf(location.getLongitude()));
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15);
            mGoogleMap.animateCamera(update);
        }
    }
    public void GoogleMapEvents(View view) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr="+lat+","+lng+""));
        startActivity(intent);
    }
    public void ShareEvents(View view) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = getIntent().getStringExtra("name") + getIntent().getStringExtra("urlItem");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getIntent().getStringArrayExtra("name"));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
    public void CallTaxiEvent(View view){
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "15800"));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
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
