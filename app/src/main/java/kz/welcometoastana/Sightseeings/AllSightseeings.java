package kz.welcometoastana.Sightseeings;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kz.welcometoastana.KudaShoditListItem;
import kz.welcometoastana.R;
import kz.welcometoastana.RecycleAdapter;
import kz.welcometoastana.utility.RecyclerItemClickListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllSightseeings extends Fragment implements LocationListener {

    private final String Url = "http://89.219.32.107/api/v1/places/sightseeings?limit=2000&page=1";
    double lat2, lng2;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<KudaShoditListItem> kudaShoditListItems;


    public AllSightseeings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_all_sightseeings, container, false);


        recyclerView = (RecyclerView) v.findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        if (kudaShoditListItems == null) {
            kudaShoditListItems = new ArrayList<>();
        }
        if (kudaShoditListItems.size() == 0) {

            loadRecyclerView();

        } else {
            adapter = new RecycleAdapter(kudaShoditListItems, getContext());
            recyclerView.setAdapter(adapter);
            recyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {

                        @Override
                        public void onItemClick(View view, int position) {
                            //Toast.makeText(getContext(), "You clicked " + kudaShoditListItems.get(position).getName(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), DescriptionActivity.class);
                            intent.putExtra("name", kudaShoditListItems.get(position).getName());
                            intent.putExtra("id",kudaShoditListItems.get(position).getId());
                            intent.putExtra("description", kudaShoditListItems.get(position).getSummary());
                            intent.putExtra("imageUrl", kudaShoditListItems.get(position).getImageUrl());
                            intent.putExtra("category", kudaShoditListItems.get(position).getCategory());
                            intent.putExtra("longit", kudaShoditListItems.get(position).getLon());
                            intent.putExtra("latit", kudaShoditListItems.get(position).getLat());
                            intent.putExtra("url",Url);
                            intent.putExtra("address",kudaShoditListItems.get(position).getAddress());
                            startActivityForResult(intent, 0);
                            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {
                            // do whatever
                        }
                    })
            );

        }
        return v;

    }


    private void loadRecyclerView() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
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
                    adapter = new RecycleAdapter(kudaShoditListItems,getContext());
                    recyclerView.setAdapter(adapter);
                    recyclerView.addOnItemTouchListener(
                            new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {

                                @Override
                                public void onItemClick(View view, int position) {
                                    //Toast.makeText(getContext(), "You clicked " + kudaShoditListItems.get(position).getName(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), DescriptionActivity.class);
                                    intent.putExtra("name", kudaShoditListItems.get(position).getName());
                                    intent.putExtra("id",kudaShoditListItems.get(position).getId());
                                    intent.putExtra("description", kudaShoditListItems.get(position).getSummary());
                                    intent.putExtra("imageUrl", kudaShoditListItems.get(position).getImageUrl());
                                    intent.putExtra("category", kudaShoditListItems.get(position).getCategory());
                                    intent.putExtra("longit", kudaShoditListItems.get(position).getLon());
                                    intent.putExtra("latit", kudaShoditListItems.get(position).getLat());
                                    intent.putExtra("url",Url);
                                    intent.putExtra("address",kudaShoditListItems.get(position).getAddress());
                                    startActivityForResult(intent, 0);
                                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                }

                                @Override
                                public void onLongItemClick(View view, int position) {
                                    // do whatever
                                }
                            })
                    );
                    progressDialog.dismiss();
                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                loadRecyclerView();
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
    }

    @Override
    public void onLocationChanged(Location loc)
    {
        lat2=loc.getLatitude();
        lng2=loc.getLongitude();
        String Text = "My current location is: " +"Latitud = "+ loc.getLatitude() +"Longitud = " + loc.getLongitude();

        //System.out.println("Lat & Lang form Loc"+Text);
        //Toast.makeText( getApplicationContext(), Text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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
}
