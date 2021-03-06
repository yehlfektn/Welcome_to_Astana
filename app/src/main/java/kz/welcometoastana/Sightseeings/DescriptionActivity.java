package kz.welcometoastana.Sightseeings;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import at.blogc.android.views.ExpandableTextView;
import kz.welcometoastana.Events.EventsItemList;
import kz.welcometoastana.GdeOstanovitsya.HotelsListItem;
import kz.welcometoastana.GdePoest.GdePoestListItem;
import kz.welcometoastana.KudaShoditListItem;
import kz.welcometoastana.Nearby.AdapterforNearby;
import kz.welcometoastana.Nearby.AdapterforNearby5;
import kz.welcometoastana.Nearby.listItemNearby;
import kz.welcometoastana.R;
import kz.welcometoastana.utility.DashedUnderlineSpan;
import kz.welcometoastana.utility.MyRequest;
import kz.welcometoastana.utility.ViewPagerAdapter;


public class DescriptionActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;
    double lat, lng;
    String lngStr, latStr;
    int id;
    ViewPager viewPager;
    LinearLayout linearLayout;
    double lat2, lng2;
    LocationRequest mLocationRequest;
    listItemNearby list;
    private int dotscount;
    private ImageView[] dots;
    private ArrayList<String> imageUrls;
    private TabLayout tabLayout;
    private ViewPager viewPagerNext;
    private KudaShoditListItem nextItem;
    private String Url;
    private RequestManager glide;

    @Override
    public void onDestroy() {
        Log.d("GdePoest", "OnDestroy");
        mGoogleApiClient = null;
        mGoogleMap = null;
        mLocationRequest = null;
        lngStr = null;
        latStr = null;
        viewPager = null;
        linearLayout = null;
        list = null;
        dots = null;
        imageUrls = null;
        viewPagerNext = null;
        tabLayout = null;
        nextItem = null;
        Url = null;
        glide.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getIntent().getStringExtra("category"));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        if (glide == null) {
            glide = Glide.with(this);
        }


        TextView name = (TextView) findViewById(R.id.name);
        TextView category = (TextView) findViewById(R.id.category);
        TextView summary = (TextView) findViewById(R.id.summary);
        final TextView distance  = (TextView) findViewById(R.id.distance);
        TextView address = (TextView)findViewById(R.id.address);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        relativeLayout.setVisibility(View.GONE);


        address.setText(getIntent().getStringExtra("address"));
        latStr = getIntent().getStringExtra("latit");
        lngStr = getIntent().getStringExtra("longit");


        if(getIntent().getStringExtra("address").length()<2){
            address.setVisibility(View.GONE);
            findViewById(R.id.address_image).setVisibility(View.GONE);
        }
        name.setText(getIntent().getStringExtra("name"));
        category.setText(getIntent().getStringExtra("category"));
        summary.setText(getIntent().getStringExtra("description"));
        Url = getIntent().getStringExtra("url");

        SharedPreferences sharedPref = getSharedPreferences("app", MODE_PRIVATE);
        String latitude = sharedPref.getString("lat", "null");
        String lon = sharedPref.getString("lon", "null");
        if (!latitude.equals("null")) {
            lat2 = Double.parseDouble(latitude);
            lng2 = Double.parseDouble(lon);
        }
        TextView txtShowMore = (TextView) findViewById(R.id.txtShowMore);

        SpannableStringBuilder span = new SpannableStringBuilder(getResources().getString(R.string.show_more));
        int length = span.length();
        span.setSpan(
                new DashedUnderlineSpan(txtShowMore, ContextCompat.getColor(this, R.color.gray),
                        getResources().getDimension(R.dimen.dus_stroke_thickness),
                        getResources().getDimension(R.dimen.dus_dash_path),
                        getResources().getDimension(R.dimen.dus_offset_y),
                        getResources().getDimension(R.dimen.dus_spacing_extra)), 0,
                length, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        txtShowMore.setText(span);


        //---------------Text Animation---------------------------
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

        //---------------TextAnimation End---------------------------



        if (googleServicesAvailable()) {
            if (latStr.equals("null")) {
                findViewById(R.id.wayButton).setVisibility(View.GONE);
                findViewById(R.id.map).setVisibility(View.GONE);
                initMap();
            } else {
                initMap();
            }
        }

        id = getIntent().getIntExtra("id", 0);

        imageUrls = new ArrayList<>();
        linearLayout = (LinearLayout) findViewById(R.id.LinearSlider);



        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("places");


                    for (int i = 0; i < array.length(); i++) {

                        JSONObject o = array.getJSONObject(i);
                        if (o.getInt("id") == id) {
                            JSONArray arr = o.getJSONArray("images");
                            latStr = o.getString("lat");
                            lngStr = o.getString("lon");

                            if (lngStr.equals("null")) {
                                lngStr = "0";
                                latStr = "0";
                            }

                            lat = Double.parseDouble(latStr);
                            lng = Double.parseDouble(lngStr);

                            Location startPoint=new Location("locationA");

                            startPoint.setLatitude(lat2);
                            startPoint.setLongitude(lng2);
                            Location endPoint=new Location("locationB");
                            endPoint.setLatitude(lat);
                            endPoint.setLongitude(lng);

                            float distanceDouble=startPoint.distanceTo(endPoint);
                            if(distanceDouble/1000>6000){
                                distance.setVisibility(View.GONE);
                            }else {
                                if (distanceDouble > 1000) {
                                    distance.setText(" " + (int) distanceDouble / 1000 + "." + (int) ((distanceDouble % 1000) / 100) + "км ");
                                } else {
                                    distance.setText(" " + (int) distanceDouble + "м ");
                                }
                            }
                            goToLocationZoom(lat, lng, 15);
                            setMarker(getIntent().getStringExtra("name"), lat, lng);

                            if(arr.length()==0){

                                imageUrls.add("http://imgur.com/bpx2TrL");
                            }else {

                                for (int j = 0; j < arr.length(); j++) {
                                    imageUrls.add(arr.get(j).toString());
                                }
                            }

                            if ((i + 1) != array.length()) {
                                o = array.getJSONObject(i + 1);
                                nextItem = new KudaShoditListItem(
                                        o.getString("name"),
                                        o.getString("description"),
                                        o.getJSONArray("images").get(0).toString(),
                                        o.getJSONObject("category").getString("name"),
                                        o.getString("lon"),
                                        o.getString("lat"),
                                        o.getInt("id"),
                                        o.getString("address")
                                );
                            }
                        }
                    }

                    viewPager = (ViewPager) findViewById(R.id.viewPager);

                    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getApplicationContext(), imageUrls);
                    viewPager.setAdapter(viewPagerAdapter);
                    dotscount = viewPagerAdapter.getCount();
                    if (dotscount != 0) {
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
                    }
                    if (imageUrls.size() < 2) {
                        linearLayout.setVisibility(View.GONE);
                    }


                    if (nextItem != null) {
                        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
                        relativeLayout.setVisibility(View.VISIBLE);
                        ImageView imageViewNext = (ImageView) findViewById(R.id.imageViewNext);
                        TextView nameNext = (TextView) findViewById(R.id.nameNext);
                        TextView categoryNext = (TextView) findViewById(R.id.categoryNext);

                        nameNext.setText(nextItem.getName());
                        categoryNext.setText(nextItem.getCategory());
                        glide
                                .load(nextItem.getImageUrl())
                                .centerCrop()
                                .into(imageViewNext);
                    } else {
                        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
                        relativeLayout.setVisibility(View.GONE);
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

        StringRequest nearbyRequest = new MyRequest(Request.Method.GET, "http://89.219.32.107/api/v1/nearby?lat=" + lat + "&lon=" + lng, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                ArrayList<EventsItemList> arrayListEvents = new ArrayList<>();
                ArrayList<HotelsListItem> hotelsListItems = new ArrayList<>();
                ArrayList<GdePoestListItem> gdePoestListItems = new ArrayList<>();
                ArrayList<KudaShoditListItem> kudaShoditListItems = new ArrayList<>();


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject object = jsonObject.getJSONObject("data");
                    JSONArray array = object.getJSONArray("sightseeings");

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


                    array = object.getJSONArray("hotels");
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
                                o.optInt("stars"),
                                o.optString("site"),
                                o.getInt("id"),
                                o.optString("book_url")
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
                    array = object.getJSONArray("events");
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

                        EventsItemList item = new EventsItemList(
                                o.getString("name"),
                                o.getString("description"),
                                o.getJSONArray("images").get(0).toString(),
                                o.getJSONObject("category").getString("name"),
                                lon,
                                lat,
                                o.getInt("id"),
                                o.getString("date"),
                                o.getString("address"),
                                "от 5000тг",
                                o.getString("url")
                        );

                        arrayListEvents.add(item);

                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    e.printStackTrace();
                }

                list = new listItemNearby(arrayListEvents, hotelsListItems, gdePoestListItems, kudaShoditListItems);

                tabLayout = (TabLayout) findViewById(R.id.tabsEvent);
                viewPagerNext = (ViewPager) findViewById(R.id.viewpagerEvent);
                viewPagerNext.setAdapter(new AdapterforNearby(glide, DescriptionActivity.this, list, "orange"));
                tabLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        tabLayout.setupWithViewPager(viewPagerNext);
                    }
                });
                tabLayout.setVisibility(View.VISIBLE);
                viewPagerNext.setVisibility(View.VISIBLE);
                findViewById(R.id.txtShowMore).setVisibility(View.VISIBLE);

                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
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
                    DescriptionActivity.this.setMarker("Local", latLng.latitude, latLng.longitude);
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
        try {
            mGoogleMap.animateCamera(update);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setMarker(String locality, double lat, double lng) {
        MarkerOptions options = new MarkerOptions()
                .title(locality)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("icon_marker", 50, 50)))
                .position(new LatLng(lat, lng));
        try {
            mGoogleMap.addMarker(options).showInfoWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15);
            mGoogleMap.animateCamera(update);
        }
    }

    public void GoogleMap(View view) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr="+lat+","+lng+""));
        startActivity(intent);
    }
    public void Share(View view) {
        String urlItem = getIntent().getStringExtra("urlItem");
        urlItem = urlItem.replace("89.219.32.107", "welcometoastana.kz");
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = getIntent().getStringExtra("name") + "\n\n" + urlItem;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getIntent().getStringArrayExtra("name"));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
    public void CallTaxi(View view){
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "15800"));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    public void showMore(View view) {
        int position = viewPagerNext.getCurrentItem();
        viewPagerNext.setAdapter(new AdapterforNearby5(glide, this, list, ""));
        viewPagerNext.setCurrentItem(position);
        ViewGroup.LayoutParams params = viewPagerNext.getLayoutParams();
        float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        params.height = (int) (400 * scale + 0.5f);
        viewPagerNext.setLayoutParams(params);
        (findViewById(view.getId())).setVisibility(View.GONE);
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

    public void NextItem(View view) {
        Intent intent = new Intent(this, DescriptionActivity.class);
        intent.putExtra("name", nextItem.getName());
        intent.putExtra("id", nextItem.getId());
        intent.putExtra("description", nextItem.getSummary());
        intent.putExtra("imageUrl", nextItem.getImageUrl());
        intent.putExtra("category", nextItem.getCategory());
        intent.putExtra("longit", nextItem.getLon());
        intent.putExtra("latit", nextItem.getLat());
        intent.putExtra("url", Url);
        intent.putExtra("address", nextItem.getAddress());
        startActivityForResult(intent, 0);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        finish();
    }
}
