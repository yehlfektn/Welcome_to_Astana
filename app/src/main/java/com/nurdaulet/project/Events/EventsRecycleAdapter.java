package com.nurdaulet.project.Events;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nurdaulet.project.MainActivity;
import com.nurdaulet.project.R;

import java.util.List;

/**
 * Created by nurdaulet on 5/15/17.
 */

public class EventsRecycleAdapter extends RecyclerView.Adapter<EventsRecycleAdapter.ViewHolder> implements LocationListener {

    private List<EventsItemList> eventsItemLists;
    private final Context context;
    double lat2, lng2;
    double distanceDouble;

    public EventsRecycleAdapter(List<EventsItemList> eventsItemLists, Context context) {
        this.eventsItemLists = eventsItemLists;
        this.context = context;
    }

    @Override
    public EventsRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_events,parent,false);

        return new EventsRecycleAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EventsRecycleAdapter.ViewHolder holder, int position) {

        final EventsItemList kudaShoditListItem = eventsItemLists.get(position);
        holder.name.setText(kudaShoditListItem.getName());

        Glide.with(context)
                .load(kudaShoditListItem.getImageUrl())
                .into(holder.imageView);
        holder.imageView.setScaleType(ImageView.ScaleType.FIT_START);
        holder.category.setText(kudaShoditListItem.getCategory());




    }

    @Override
    public int getItemCount() {
        return eventsItemLists.size();
    }

    @Override
    public void onLocationChanged(Location loc)
    {
        lat2=loc.getLatitude();
        lng2=loc.getLongitude();
        String Text = "My current location is: " +"Latitud = "+ loc.getLatitude() +"Longitud = " + loc.getLongitude();

        Log.d("LAAAAT AND LONGIT", Text);
        //Toast.makeText( getApplicationContext(), Text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public ImageView imageView;
        public RelativeLayout relativeLayout;
        public TextView category;




        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.name);
            imageView = (ImageView)itemView.findViewById(R.id.imageView);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
            category = (TextView)itemView.findViewById(R.id.category);



        }
    }




}