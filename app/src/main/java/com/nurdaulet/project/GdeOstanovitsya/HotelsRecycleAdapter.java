package com.nurdaulet.project.GdeOstanovitsya;


import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nurdaulet.project.R;

import java.util.List;

/**
 * Created by nurdaulet on 5/5/17.
 */

public class HotelsRecycleAdapter extends RecyclerView.Adapter<HotelsRecycleAdapter.ViewHolder> {

    private List<HotelsListItem> HotelslistItems;
    private final Context context;

    public HotelsRecycleAdapter(List<HotelsListItem> HotelslistItems, Context context) {
        this.HotelslistItems = HotelslistItems;
        this.context = context;
    }

    @Override
    public HotelsRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_gdeostanovitsya,parent,false);

        return new HotelsRecycleAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HotelsRecycleAdapter.ViewHolder holder, int position) {

        final HotelsListItem hotelsListItem = HotelslistItems.get(position);
        holder.name.setText(hotelsListItem.getName());
        holder.category.setText(hotelsListItem.getCategory());
        holder.location.setText(hotelsListItem.getAddress());
        holder.phone.setText(hotelsListItem.getPhone());

        Glide.with(context)
                .load(hotelsListItem.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "You clicked "+ hotelsListItem.getName()+"lon: "+ hotelsListItem.getLon()+" lat: "+ hotelsListItem.getLat(), Toast.LENGTH_SHORT).show();

            }
        });
        LayerDrawable stars = (LayerDrawable) holder.ratingBar.getProgressDrawable();

        setRatingStarColor(stars.getDrawable(2), ContextCompat.getColor(context, R.color.foreground));
        // Half filled stars
        setRatingStarColor(stars.getDrawable(1), ContextCompat.getColor(context, R.color.background));
        // Empty stars
        setRatingStarColor(stars.getDrawable(0), ContextCompat.getColor(context, R.color.background));
        holder.ratingBar.setRating(HotelslistItems.get(position).getStars());


    }

    @Override
    public int getItemCount() {
        return HotelslistItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private ImageView imageView;
        private RelativeLayout relativeLayout;
        private TextView category;
        private TextView phone;
        private TextView location;
        private RatingBar ratingBar;


        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.name);
            imageView = (ImageView)itemView.findViewById(R.id.imageView);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
            category = (TextView)itemView.findViewById(R.id.category);
            phone = (TextView)itemView.findViewById(R.id.phoneGde);
            location = (TextView)itemView.findViewById(R.id.locationGde);
            ratingBar = (RatingBar)itemView.findViewById(R.id.ratingBar);
        }
    }
    private void setRatingStarColor(Drawable drawable, @ColorInt int color)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            DrawableCompat.setTint(drawable, color);
        }
        else
        {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }

}

