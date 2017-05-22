package kz.welcometoastana.Pamyatka;


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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kz.welcometoastana.R;
import kz.welcometoastana.utility.RecyclerItemClickListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class Prebyvanie extends Fragment {

    private  final String Url = "http://89.219.32.107/api/v1/tourist?limit=20&page=1&category=57";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<PamyatkaListItem> pamyatkaListItems;


    public Prebyvanie() {
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


        if(pamyatkaListItems ==null) {
            pamyatkaListItems = new ArrayList<>();
        }
        if(pamyatkaListItems.size()==0){

            loadRecyclerView();


        }else{
            adapter = new PamyatkaRecycleAdapter(pamyatkaListItems,getContext());
            recyclerView.setAdapter(adapter);
            recyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {

                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent = new Intent(getActivity(), PamyatkaDescription.class);
                            intent.putExtra("name", pamyatkaListItems.get(position).getName());
                            intent.putExtra("description", pamyatkaListItems.get(position).getSummary());
                            intent.putExtra("imageUrl", pamyatkaListItems.get(position).getImageUrl());
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

                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("places");

                    for (int i=0; i<array.length();i++){
                        JSONObject o = array.getJSONObject(i);
                        PamyatkaListItem item = new PamyatkaListItem(
                                o.getString("name"),
                                o.getString("description"),
                                o.getJSONArray("images").get(0).toString(),
                                o.getJSONObject("category").getString("name")
                        );

                        pamyatkaListItems.add(item);

                    }

                    adapter = new PamyatkaRecycleAdapter(pamyatkaListItems,getContext());
                    recyclerView.setAdapter(adapter);
                    recyclerView.addOnItemTouchListener(
                            new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {

                                @Override
                                public void onItemClick(View view, int position) {
                                    Intent intent = new Intent(getActivity(), PamyatkaDescription.class);
                                    intent.putExtra("name", pamyatkaListItems.get(position).getName());
                                    intent.putExtra("description", pamyatkaListItems.get(position).getSummary());
                                    intent.putExtra("imageUrl", pamyatkaListItems.get(position).getImageUrl());
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
                progressDialog.dismiss();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
