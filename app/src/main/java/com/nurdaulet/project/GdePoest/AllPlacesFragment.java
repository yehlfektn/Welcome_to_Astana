package com.nurdaulet.project.GdePoest;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.nurdaulet.project.Sightseeings.DescriptionActivity;
import com.nurdaulet.project.utility.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllPlacesFragment extends Fragment {

    private final String Url = "http://89.219.32.107/api/v1/foods?limit=100&page=1";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<GdePoestListItem> gdePoestListItems;

    public AllPlacesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_all_places, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycleAllPlaces);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        if(gdePoestListItems ==null) {
            gdePoestListItems = new ArrayList<>();
        }
        if(gdePoestListItems.size()==0){

            loadRecyclerView();

        }else{
            adapter = new GdePoestRecycleAdapter(gdePoestListItems,getContext());
            recyclerView.setAdapter(adapter);
            recyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {

                        @Override
                        public void onItemClick(View view, int position) {
                            //Toast.makeText(getContext(), "You clicked " + kudaShoditListItems.get(position).getName(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), GdePoestDescription.class);
                            intent.putExtra("name", gdePoestListItems.get(position).getName());
                            intent.putExtra("id",gdePoestListItems.get(position).getId());
                            intent.putExtra("description", gdePoestListItems.get(position).getSummary());
                            intent.putExtra("imageUrl", gdePoestListItems.get(position).getImageUrl());
                            intent.putExtra("category", gdePoestListItems.get(position).getCategory());
                            intent.putExtra("longit", gdePoestListItems.get(position).getLon());
                            intent.putExtra("latit", gdePoestListItems.get(position).getLat());
                            intent.putExtra("address",gdePoestListItems.get(position).getAddress());
                            intent.putExtra("url",Url);
                            intent.putExtra("phone",gdePoestListItems.get(position).getPhone());
                            startActivityForResult(intent, 0);
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

                    for (int i=0; i<array.length();i++){
                        JSONObject o = array.getJSONObject(i);
                        GdePoestListItem item = new GdePoestListItem(
                                o.getString("name"),
                                o.getString("description"),
                                o.getJSONArray("images").get(0).toString(),
                                o.getJSONObject("category").getString("name"),
                                o.optString("lon"),
                                o.optString("lat"),
                                o.optString("phone"),
                                o.optString("address"),
                                o.getInt("id")
                        );

                        gdePoestListItems.add(item);

                    }

                    adapter = new GdePoestRecycleAdapter(gdePoestListItems,getContext());
                    recyclerView.setAdapter(adapter);
                    recyclerView.addOnItemTouchListener(
                            new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {

                                @Override
                                public void onItemClick(View view, int position) {
                                    //Toast.makeText(getContext(), "You clicked " + kudaShoditListItems.get(position).getName(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), GdePoestDescription.class);
                                    intent.putExtra("name", gdePoestListItems.get(position).getName());
                                    intent.putExtra("id",gdePoestListItems.get(position).getId());
                                    intent.putExtra("description", gdePoestListItems.get(position).getSummary());
                                    intent.putExtra("imageUrl", gdePoestListItems.get(position).getImageUrl());
                                    intent.putExtra("category", gdePoestListItems.get(position).getCategory());
                                    intent.putExtra("longit", gdePoestListItems.get(position).getLon());
                                    intent.putExtra("latit", gdePoestListItems.get(position).getLat());
                                    intent.putExtra("address",gdePoestListItems.get(position).getAddress());
                                    intent.putExtra("url",Url);
                                    intent.putExtra("phone",gdePoestListItems.get(position).getPhone());
                                    startActivityForResult(intent, 0);
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


            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}
