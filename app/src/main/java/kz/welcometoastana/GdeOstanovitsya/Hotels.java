package kz.welcometoastana.GdeOstanovitsya;


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
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kz.welcometoastana.R;
import kz.welcometoastana.utility.RecyclerItemClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class Hotels extends Fragment {

    private String Url = "http://89.219.32.107/api/v1/hotels?limit=20&page=1&category=7";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<HotelsListItem> hotelsListItems;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RequestManager glide;
    public Hotels() {
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

        if (glide == null) {
            glide = Glide.with(this);
        }

        if(hotelsListItems ==null) {
            hotelsListItems = new ArrayList<>();
        }
        if(hotelsListItems.size()==0){
            loadRecyclerView();
        }else{
            setAdapter();
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
        hotelsListItems.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("places");

                    for (int i=0; i<array.length();i++){
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
                                o.getInt("stars"),
                                o.optString("site"),
                                o.getInt("id"),
                                o.optString("book_url")
                        );

                        hotelsListItems.add(item);

                    }

                    setAdapter();

                    swipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    swipeRefreshLayout.setRefreshing(false);
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
        Intent intent = new Intent(getActivity(), GdeOstanovitsyaDescription.class);
        intent.putExtra("name", hotelsListItems.get(position).getName());
        intent.putExtra("id", hotelsListItems.get(position).getId());
        intent.putExtra("description", hotelsListItems.get(position).getSummary());
        intent.putExtra("imageUrl", hotelsListItems.get(position).getImageUrl());
        intent.putExtra("category", hotelsListItems.get(position).getCategory());
        intent.putExtra("longit", hotelsListItems.get(position).getLon());
        intent.putExtra("latit", hotelsListItems.get(position).getLat());
        intent.putExtra("address", hotelsListItems.get(position).getAddress());
        intent.putExtra("url", Url);
        intent.putExtra("phone", hotelsListItems.get(position).getPhone());
        intent.putExtra("website", hotelsListItems.get(position).getWebsite());
        intent.putExtra("stars", hotelsListItems.get(position).getStars());
        intent.putExtra("urlItem", hotelsListItems.get(position).getBook_url());
        startActivityForResult(intent, 0);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    private void setAdapter() {
        HotelsRecycleAdapter adapter = new HotelsRecycleAdapter(glide, hotelsListItems, getContext());
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

    @Override
    public void onDestroyView() {

        glide.onDestroy();

        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (recyclerView != null) {
            recyclerView.setAdapter(null);
        }
        if (glide != null) {
            glide.onDestroy();
            glide = null;
        }
        hotelsListItems = null;
        Url = null;
        swipeRefreshLayout = null;
        recyclerView = null;
        super.onDestroy();
    }


}