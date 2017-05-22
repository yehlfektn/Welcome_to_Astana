package kz.welcometoastana.GdePoest;


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
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import kz.welcometoastana.MainActivity;
import kz.welcometoastana.R;

/**
 * Created by nurdaulet on 5/5/17.
 */

public class GdePoestRecycleAdapter extends RecyclerView.Adapter<GdePoestRecycleAdapter.ViewHolder> implements LocationListener {

    private final Context context;
    double lat2, lng2;
    double distanceDouble;
    private List<GdePoestListItem> eventsItemLists;

    public GdePoestRecycleAdapter(List<GdePoestListItem> eventsItemLists, Context context) {
        this.eventsItemLists = eventsItemLists;
        this.context = context;
    }

    @Override
    public GdePoestRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_gdepoest,parent,false);

        return new GdePoestRecycleAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GdePoestRecycleAdapter.ViewHolder holder, int position) {

        final GdePoestListItem kudaShoditListItem = eventsItemLists.get(position);
        holder.name.setText(kudaShoditListItem.getName());
        holder.category.setText(kudaShoditListItem.getCategory());

        if(kudaShoditListItem.getAddress().length()<2){
            holder.address.setVisibility(View.GONE);
        }else{
            holder.address.setText(kudaShoditListItem.getAddress());
        }
        if(kudaShoditListItem.getPhone().length()<2){
            holder.phone.setVisibility(View.GONE);
        }else{
            holder.phone.setText(kudaShoditListItem.getPhone());
        }

        Location startPoint = new Location("locationA");
        if(MainActivity.gpsLocation != null){

            lat2 = MainActivity.gpsLocation.getLatitude();
            lng2 = MainActivity.gpsLocation.getLongitude();

        }
        startPoint.setLatitude(lat2);
        startPoint.setLongitude(lng2);


        Location endPoint=new Location("locationB");
        if(kudaShoditListItem.getLat().equals("null")){
            holder.distance.setVisibility(View.GONE);
        }else {
            endPoint.setLatitude(Double.parseDouble(kudaShoditListItem.getLat()));
            endPoint.setLongitude(Double.parseDouble(kudaShoditListItem.getLon()));

            distanceDouble = startPoint.distanceTo(endPoint);
            //Intent intent = new Intent(get, DescriptionActivity.class);

            if(distanceDouble > 5000000){
                holder.distance.setVisibility(View.GONE);

            }else{
                if (distanceDouble > 1000) {
                    holder.distance.setText(" " + (int) distanceDouble / 1000 + "." + (int) ((distanceDouble % 1000) / 100) + "км ");
                } else {
                    holder.distance.setText(" " + (int) distanceDouble + "м ");
                }
            }}
        Glide.with(context)
                .load(kudaShoditListItem.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);

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
        public TextView category;
        public TextView distance;
        public TextView phone;
        public TextView address;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.name);
            imageView = (ImageView)itemView.findViewById(R.id.imageView);
            category = (TextView)itemView.findViewById(R.id.category);
            distance = (TextView)itemView.findViewById(R.id.distance);
            phone = (TextView)itemView.findViewById(R.id.phone);
            address = (TextView)itemView.findViewById(R.id.address);
        }
    }



}
