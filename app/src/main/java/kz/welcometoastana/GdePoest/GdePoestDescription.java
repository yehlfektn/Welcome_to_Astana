package kz.welcometoastana.GdePoest;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import at.blogc.android.views.ExpandableTextView;
import kz.welcometoastana.MainActivity;
import kz.welcometoastana.R;
import kz.welcometoastana.utility.DashedUnderlineSpan;
import kz.welcometoastana.utility.ViewPagerAdapter;

public class GdePoestDescription extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;
    double lat, lng;
    String lngStr, latStr;
    int id;
    ViewPager viewPager;
    LinearLayout linearLayout;
    double lat2, lng2;
    Marker marker;
    LocationRequest mLocationRequest;
    private int dotscount;
    private ImageView[] dots;
    private ArrayList<String> imageUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gde_poest_description);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();
        GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, new int[]{ 0xff17A400 , 0xff5ABC05 });
        getSupportActionBar().setBackgroundDrawable(g);
        getSupportActionBar().setTitle(getIntent().getStringExtra("category"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        TextView name = (TextView) findViewById(R.id.name);
        TextView category = (TextView) findViewById(R.id.category);
        final TextView distance  = (TextView) findViewById(R.id.distance);
        TextView phone =  (TextView)findViewById(R.id.phone);
        TextView address = (TextView)findViewById(R.id.address);

        address.setText(getIntent().getStringExtra("address"));
        name.setText(getIntent().getStringExtra("name"));
        category.setText(getIntent().getStringExtra("category"));
        phone.setText(getIntent().getStringExtra("phone"));
        final String Url=getIntent().getStringExtra("url");
        Log.d("GdePoestDescription", Url);
        if(MainActivity.gpsLocation != null){

            lat2 = MainActivity.gpsLocation.getLatitude();
            lng2 = MainActivity.gpsLocation.getLongitude();
            Log.d("DescriptionActivity", "lat: "+lat2+"lon: "+lng2);

        }

        //---------------Text Animation---------------------------
        final ExpandableTextView expandableTextView = (ExpandableTextView) this.findViewById(R.id.summary);
        final String big = getIntent().getStringExtra("description");
        expandableTextView.setText(big);
        final TextView OpenCollapse = (TextView)this.findViewById(R.id.openCollapse);


        final Shader p = expandableTextView.getPaint().getShader();
        final Shader textShader=new LinearGradient(0, 100, 0, 250, new int[]{Color.BLACK,Color.WHITE}, new float[]{0, 1}, Shader.TileMode.CLAMP);
        expandableTextView.getPaint().setShader(textShader);


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
                    //expandableTextView.setText("Here is what worked for me using some of the above responses (I am using ButterKnife in the example):asd as das dasdasdasdasd asdas das dasd asd sad sad Here is what worked for me using some of the above responses (I am using ButterKnife in the example):Here is what worked for me using some of the above responses (I am using ButterKnife in the example):");
                    expandableTextView.setText(big);
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
                    //expandableTextView.getPaint().setShader(textShader);
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
                    //expandableTextView.setText("Here is what worked for me using some of the above responses (I am using ButterKnife in the example):asd as das dasdasdasdasd asdas das dasd asd sad sad Here is what worked for me using some of the above responses (I am using ButterKnife in the example):Here is what worked for me using some of the above responses (I am using ButterKnife in the example):");
                    expandableTextView.setText(big);
                    expandableTextView.getPaint().setShader(p);
                    expandableTextView.expand();

                    OpenCollapse.setText("");
                }
            }
        });

        //---------------TextAnimation End---------------------------


        if (googleServicesAvailable()) {
            //Toast.makeText(this, "Perfect!", Toast.LENGTH_LONG).show();
            initMap();
        }

        id = getIntent().getIntExtra("id", 0);

        imageUrls = new ArrayList<>();
        linearLayout = (LinearLayout) findViewById(R.id.LinearSlider);



        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d("DescriptionActivity", Url);
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
                            Log.d("DescriptionActivity", "lat: "+lat+"lon: "+lng);
                            startPoint.setLatitude(lat2);
                            startPoint.setLongitude(lng2);
                            Location endPoint=new Location("locationB");
                            endPoint.setLatitude(lat);
                            endPoint.setLongitude(lng);


                            float distanceDouble=startPoint.distanceTo(endPoint);
                            Log.d("DescriptionActivity", "distance: "+distanceDouble);
                            if(distanceDouble/1000 > 6000){
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
                            Log.d("DescriptionActivity", "size: "+arr.length());
                            if(arr.length()==0){

                                imageUrls.add("http://imgur.com/bpx2TrL");
                            }else {

                                for (int j = 0; j < arr.length(); j++) {
                                    imageUrls.add(arr.get(j).toString());
                                    Log.d("MainActivity", arr.get(j).toString());
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
                    dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot_green));

                    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {

                            for (int i = 0; i < dotscount; i++) {
                                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                            }
                            dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot_green));

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
                progressDialog.dismiss();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

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
                    GdePoestDescription.this.setMarker("Local", latLng.latitude, latLng.longitude);
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
                .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("icon_marker_green", 50, 50)))
                .position(new LatLng(lat, lng));

        mGoogleMap.addMarker(options).showInfoWindow();

    }

    public Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
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
    public void GoogleMapGdePoest(View view) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr="+lat+","+lng+""));
        startActivity(intent);


    }
    public void ShareGdePoest(View view) {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = getIntent().getStringExtra("description");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getIntent().getStringArrayExtra("name"));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));

    }
    public void CallTaxiPoest(View view){

        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "15800"));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }

}
