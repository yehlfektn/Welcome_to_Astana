package kz.welcometoastana.Events;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kz.welcometoastana.MainActivity;
import kz.welcometoastana.R;
import kz.welcometoastana.utility.RecyclerItemClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class Vystavki extends Fragment {

    SimpleDateFormat formatDateTime = new SimpleDateFormat("yyyy-MM-dd");
    private String Url = "http://89.219.32.107/api/v1/places/events?limit=200&page=1&category=13";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<EventsItemList> eventsItemLists;
    private SwipeRefreshLayout swipeRefreshLayout;

    public Vystavki() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hostels, container, false);
        recyclerView = (RecyclerView)v.findViewById(R.id.recycleHostels);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        if(eventsItemLists == null) {
            eventsItemLists = new ArrayList<>();
        }
        if(eventsItemLists.size()==0){
            loadRecyclerView();
        }else{

            adapter = new EventsRecycleAdapter(eventsItemLists,getContext());
            recyclerView.setAdapter(adapter);
            recyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            onClick(position);
                        }
                        @Override
                        public void onLongItemClick(View view, int position) {
                        }
                    })
            );
        }
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                loadRecyclerView();
            }
        });

        return v;
    }

    public void loadRecyclerView() {
        eventsItemLists.clear();
        if (MainActivity.dateTimeFrom != null) {
            Calendar dateFrom = MainActivity.dateTimeFrom;
            if (MainActivity.dateTimeTo != null) {
                Calendar dateTo = MainActivity.dateTimeTo;
                Url = "http://89.219.32.107/api/v1/places/events?limit=2000&page=1&category=13" + "&from=" + formatDateTime.format(dateFrom.getTime()) + "&to=" + formatDateTime.format(dateTo.getTime());
            } else {
                Calendar dateTo = Calendar.getInstance();
                Url = "http://89.219.32.107/api/v1/places/events?limit=2000&page=1&category=13" + "&from=" + formatDateTime.format(dateFrom.getTime()) + "&to=" + formatDateTime.format(dateTo.getTime());
            }
        }
        Log.d("ExpoEvent", "URL: " + Url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
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


                    adapter = new EventsRecycleAdapter(eventsItemLists,getContext());
                    recyclerView.setAdapter(adapter);
                    recyclerView.addOnItemTouchListener(
                            new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {

                                @Override
                                public void onItemClick(View view, int position) {
                                    onClick(position);
                                }

                                @Override
                                public void onLongItemClick(View view, int position) {
                                    // do whatever
                                }
                            })
                    );
                    swipeRefreshLayout.setRefreshing(false);

                } catch (JSONException e) {
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Sightseeings",error.toString());
                swipeRefreshLayout.setRefreshing(false);
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

    @TargetApi(Build.VERSION_CODES.N)
    public Locale getCurrentLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return getResources().getConfiguration().locale;
        }
    }

    private void onClick(int position) {
        Intent intent = new Intent(getActivity(), EventsDescription.class);
        final EventsItemList eventsItemList = eventsItemLists.get(position);
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

}