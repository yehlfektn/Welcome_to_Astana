package kz.welcometoastana.Sightseeings;


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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kz.welcometoastana.KudaShoditListItem;
import kz.welcometoastana.R;
import kz.welcometoastana.RecycleAdapter;
import kz.welcometoastana.utility.RecyclerItemClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class InterestingPlacesFragment extends Fragment {

    private static final String Url = "http://89.219.32.107/api/v1/places/sightseeings?limit=200&page=1&category=53";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<KudaShoditListItem> kudaShoditListItems;


    public InterestingPlacesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_architecture, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycleArchitecture);
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
                            intent.putExtra("id", kudaShoditListItems.get(position).getId());
                            intent.putExtra("description", kudaShoditListItems.get(position).getSummary());
                            intent.putExtra("imageUrl", kudaShoditListItems.get(position).getImageUrl());
                            intent.putExtra("category", kudaShoditListItems.get(position).getCategory());
                            intent.putExtra("longit", kudaShoditListItems.get(position).getLon());
                            intent.putExtra("latit", kudaShoditListItems.get(position).getLat());
                            intent.putExtra("url", Url);
                            intent.putExtra("address", kudaShoditListItems.get(position).getAddress());
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
                    Log.d("Sightseeings", "AdapterAttached");
                    adapter = new RecycleAdapter(kudaShoditListItems, getContext());
                    recyclerView.setAdapter(adapter);
                    recyclerView.addOnItemTouchListener(
                            new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {

                                @Override
                                public void onItemClick(View view, int position) {
                                    //Toast.makeText(getContext(), "You clicked " + kudaShoditListItems.get(position).getName(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), DescriptionActivity.class);
                                    intent.putExtra("name", kudaShoditListItems.get(position).getName());
                                    intent.putExtra("id", kudaShoditListItems.get(position).getId());
                                    intent.putExtra("description", kudaShoditListItems.get(position).getSummary());
                                    intent.putExtra("imageUrl", kudaShoditListItems.get(position).getImageUrl());
                                    intent.putExtra("category", kudaShoditListItems.get(position).getCategory());
                                    intent.putExtra("longit", kudaShoditListItems.get(position).getLon());
                                    intent.putExtra("latit", kudaShoditListItems.get(position).getLat());
                                    intent.putExtra("url", Url);
                                    intent.putExtra("address", kudaShoditListItems.get(position).getAddress());
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

                Log.d("Sightseeings", error.toString());

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
