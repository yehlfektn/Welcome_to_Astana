package com.nurdaulet.project;


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


import java.util.List;

/**
 * Created by nurdaulet on 5/5/17.
 */

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

    private List<KudaShoditListItem> kudaShoditListItems;
    private final Context context;

    public RecycleAdapter(List<KudaShoditListItem> kudaShoditListItems, Context context) {
        this.kudaShoditListItems = kudaShoditListItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_kudashodit,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final KudaShoditListItem kudaShoditListItem = kudaShoditListItems.get(position);
        holder.name.setText(kudaShoditListItem.getName());
        holder.category.setText(kudaShoditListItem.getCategory());

        Glide.with(context)
                .load(kudaShoditListItem.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "You clicked "+ kudaShoditListItem.getName()+"lon: "+kudaShoditListItem.getLon()+" lat: "+kudaShoditListItem.getLat(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return kudaShoditListItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public ImageView imageView;
        public RelativeLayout relativeLayout;
        public TextView category;
        public TextView distance;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.name);
            imageView = (ImageView)itemView.findViewById(R.id.imageView);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
            category = (TextView)itemView.findViewById(R.id.category);
            distance = (TextView)itemView.findViewById(R.id.distance);
        }
    }

}
