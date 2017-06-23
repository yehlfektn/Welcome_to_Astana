package kz.welcometoastana.Entertainment;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import kz.welcometoastana.Sightseeings.DescriptionActivity;
import kz.welcometoastana.utility.RecyclerItemClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class CinemaFragment extends Fragment {

    private final String Url = "http://89.219.32.107/api/v1/places/shopping?limit=200&page=1&category=46";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<KudaShoditListItem> kudaShoditListItems;
    private SwipeRefreshLayout swipeRefreshLayout;

    public CinemaFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_hostels, container, false);


        recyclerView = (RecyclerView) v.findViewById(R.id.recycleHostels);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if(kudaShoditListItems ==null) {
            kudaShoditListItems = new ArrayList<>();
        }
        if(kudaShoditListItems.size()==0){
            loadRecyclerView();
        }else{
            adapter = new RecycleAdapter(kudaShoditListItems,getContext());
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
        }
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRecyclerView();
            }
        });
        return v;
    }

    private void loadRecyclerView() {
        kudaShoditListItems.clear();
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
    private Locale getCurrentLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return getResources().getConfiguration().locale;
        }
    }

    private void onClick(int position) {

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

}