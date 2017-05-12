package com.nurdaulet.project.GdePoest;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nurdaulet.project.KudaShoditListItem;
import com.nurdaulet.project.R;

import java.util.List;

/**
 * Created by nurdaulet on 5/5/17.
 */

public class GdePoestRecycleAdapter extends RecyclerView.Adapter<GdePoestRecycleAdapter.ViewHolder> {

    private List<GdePoestListItem> gdelistItems;
    private final Context context;

    public GdePoestRecycleAdapter(List<GdePoestListItem> gdelistItems, Context context) {
        this.gdelistItems = gdelistItems;
        this.context = context;
    }

    @Override
    public GdePoestRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_gdepoest,parent,false);

        return new GdePoestRecycleAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GdePoestRecycleAdapter.ViewHolder holder, int position) {

        final GdePoestListItem gdePoestListItem = gdelistItems.get(position);
        holder.name.setText(gdePoestListItem.getName());
        holder.category.setText(gdePoestListItem.getCategory());
        holder.location.setText(gdePoestListItem.getAddress());
        holder.phone.setText(gdePoestListItem.getPhone());

        Glide.with(context)
                .load(gdePoestListItem.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "You clicked "+ gdePoestListItem.getName()+"lon: "+gdePoestListItem.getLon()+" lat: "+gdePoestListItem.getLat(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return gdelistItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private ImageView imageView;
        private RelativeLayout relativeLayout;
        private TextView category;
        private TextView phone;
        private TextView location;


        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.name);
            imageView = (ImageView)itemView.findViewById(R.id.imageView);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
            category = (TextView)itemView.findViewById(R.id.category);
            phone = (TextView)itemView.findViewById(R.id.phoneGde);
            location = (TextView)itemView.findViewById(R.id.locationGde);
        }
    }

}
