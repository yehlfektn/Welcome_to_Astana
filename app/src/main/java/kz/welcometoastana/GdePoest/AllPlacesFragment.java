package kz.welcometoastana.GdePoest;


import android.annotation.TargetApi;
import android.app.ProgressDialog;
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
public class AllPlacesFragment extends Fragment {

    private String Url = "http://89.219.32.107/api/v1/foods?limit=2000&page=1";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<GdePoestListItem> gdePoestListItems;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RequestManager glide;

    public AllPlacesFragment() {
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

        if(gdePoestListItems ==null) {
            gdePoestListItems = new ArrayList<>();
        }
        if(gdePoestListItems.size()==0){

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
        gdePoestListItems.clear();
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading data...");
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setDimAmount(0);
        }
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("places");


                    for (int i=0; i<array.length();i++){
                        JSONObject o = array.getJSONObject(i);
                        String image;
                        if(o.getJSONArray("images").length()>0){
                            image = o.getJSONArray("images").get(0).toString();
                        }else{
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

                    setAdapter();
                    progressDialog.dismiss();
                    swipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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
        Intent intent = new Intent(getActivity(), GdePoestDescription.class);
        intent.putExtra("name", gdePoestListItems.get(position).getName());
        intent.putExtra("id", gdePoestListItems.get(position).getId());
        intent.putExtra("description", gdePoestListItems.get(position).getSummary());
        intent.putExtra("imageUrl", gdePoestListItems.get(position).getImageUrl());
        intent.putExtra("category", gdePoestListItems.get(position).getCategory());
        intent.putExtra("longit", gdePoestListItems.get(position).getLon());
        intent.putExtra("latit", gdePoestListItems.get(position).getLat());
        intent.putExtra("address", gdePoestListItems.get(position).getAddress());
        intent.putExtra("url", Url);
        intent.putExtra("phone", gdePoestListItems.get(position).getPhone());
        startActivityForResult(intent, 0);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    private void setAdapter() {
        adapter = new GdePoestRecycleAdapter(glide, gdePoestListItems, getContext());
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

    @Override
    public void onDestroy() {
        recyclerView.setAdapter(null);
        adapter = null;
        recyclerView = null;
        swipeRefreshLayout = null;
        Url = null;
        gdePoestListItems = null;
        glide.onDestroy();
        glide = null;
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {

        glide.onDestroy();

        super.onDestroyView();
    }

}
