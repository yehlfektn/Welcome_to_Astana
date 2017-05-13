package com.nurdaulet.project.Sightseeings;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nurdaulet.project.KudaShoditListItem;
import com.nurdaulet.project.R;
import com.nurdaulet.project.RecycleAdapter;
import com.nurdaulet.project.utility.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllSightseeings extends Fragment {

    private final String Url = "http://welcometoastana.kz/api/v1/places/sightseeings?limit=20&page=1";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<KudaShoditListItem> kudaShoditListItems;


    public AllSightseeings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
                            Toast.makeText(getContext(), "You clicked "+kudaShoditListItems.get(position).getName(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), DescriptionActivity.class);
                            intent.putExtra("name", kudaShoditListItems.get(position).getName());
                            intent.putExtra("description", kudaShoditListItems.get(position).getSummary());
                            intent.putExtra("imageUrl", kudaShoditListItems.get(position).getImageUrl());
                            intent.putExtra("category", kudaShoditListItems.get(position).getCategory());
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
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("places");


                    for (int i=0; i<array.length();i++){
                        JSONObject o = array.getJSONObject(i);
                        KudaShoditListItem item = new KudaShoditListItem(
                                o.getString("name"),
                                o.getString("description"),
                                o.getJSONArray("images").get(0).toString(),
                                o.getJSONObject("category").getString("name"),
                                o.optString("lon"),
                                o.optString("lat"),
                                o.getInt("id")
                        );

                        kudaShoditListItems.add(item);

                    }
                    adapter = new RecycleAdapter(kudaShoditListItems,getContext());
                    recyclerView.setAdapter(adapter);
                    recyclerView.addOnItemTouchListener(
                            new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
<<<<<<< HEAD

                                @Override
                                public void onItemClick(View view, int position) {
                                    Toast.makeText(getContext(), "You clicked "+kudaShoditListItems.get(position).getName(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), DescriptionActivity.class);
                                    intent.putExtra("name", kudaShoditListItems.get(position).getName());
=======
                                @Override
                                public void onItemClick(View view, int position) {
                                    String name = kudaShoditListItems.get(position).getName().substring(0,kudaShoditListItems.get(position).getName().length()-1);
                                    Toast.makeText(getContext(), "You clicked "+kudaShoditListItems.get(position).getName(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), DescriptionActivity.class);
                                    intent.putExtra("name", name);
>>>>>>> 24f4fbc3347329a747418723ae337b59adec1941
                                    intent.putExtra("description", kudaShoditListItems.get(position).getSummary());
                                    intent.putExtra("category",kudaShoditListItems.get(position).getCategory());
                                    intent.putExtra("imageUrl", kudaShoditListItems.get(position).getImageUrl());
                                    intent.putExtra("id", kudaShoditListItems.get(position).getId());
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
                progressDialog.dismiss();
                loadRecyclerView();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
