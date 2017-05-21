package com.nurdaulet.project.Events;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nurdaulet.project.R;
import com.nurdaulet.project.utility.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpoEvent extends Fragment {


    private final String Url = "http://89.219.32.107/api/v1/places/events?limit=200&page=1&category=76";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<EventsItemList> eventsItemLists;

    public ExpoEvent() {
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

            Log.d("AllEvents","Startingloadingdata");
            loadRecyclerView();

        }else{

            adapter = new EventsRecycleAdapter(eventsItemLists,getContext());
            recyclerView.setAdapter(adapter);
            recyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {

                        @Override
                        public void onItemClick(View view, int position) {
                            //Toast.makeText(getContext(), "You clicked " + eventsItemLists.get(position).getName(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), EventsDescription.class);
                            final EventsItemList eventsItemList = eventsItemLists.get(position);
                            intent.putExtra("name", eventsItemList.getName());
                            intent.putExtra("id", eventsItemList.getId());
                            intent.putExtra("description", eventsItemList.getSummary());
                            intent.putExtra("imageUrl", eventsItemList.getImageUrl());
                            intent.putExtra("category", eventsItemList.getCategory());
                            intent.putExtra("longit", eventsItemList.getLon());
                            intent.putExtra("latit", eventsItemList.getLat());
                            intent.putExtra("url",Url);
                            intent.putExtra("address",eventsItemList.getAddress());
                            intent.putExtra("money",eventsItemList.getMoney());
                            intent.putExtra("date",eventsItemList.getDate());
                            intent.putExtra("urlItem",eventsItemList.getUrl());
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



        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("places");

                    for (int i=0; i<array.length();i++) {

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


                        eventsItemLists.add(item);

                    }


                    adapter = new EventsRecycleAdapter(eventsItemLists,getContext());
                    recyclerView.setAdapter(adapter);
                    recyclerView.addOnItemTouchListener(
                            new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {

                                @Override
                                public void onItemClick(View view, int position) {
                                    //Toast.makeText(getContext(), "You clicked " + eventsItemLists.get(position).getName(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), EventsDescription.class);
                                    final EventsItemList eventsItemList = eventsItemLists.get(position);
                                    intent.putExtra("name", eventsItemList.getName());
                                    intent.putExtra("id", eventsItemList.getId());
                                    intent.putExtra("description", eventsItemList.getSummary());
                                    intent.putExtra("imageUrl", eventsItemList.getImageUrl());
                                    intent.putExtra("category", eventsItemList.getCategory());
                                    intent.putExtra("longit", eventsItemList.getLon());
                                    intent.putExtra("latit", eventsItemList.getLat());
                                    intent.putExtra("url",Url);
                                    intent.putExtra("address",eventsItemList.getAddress());
                                    intent.putExtra("money",eventsItemList.getMoney());
                                    intent.putExtra("date",eventsItemList.getDate());
                                    intent.putExtra("urlItem",eventsItemList.getUrl());
                                    startActivityForResult(intent, 0);
                                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                }

                                @Override
                                public void onLongItemClick(View view, int position) {
                                    // do whatever
                                }
                            })
                    );

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("Sightseeings",error.toString());

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
