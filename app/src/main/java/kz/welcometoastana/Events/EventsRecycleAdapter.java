package kz.welcometoastana.Events;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.List;

import kz.welcometoastana.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by nurdaulet on 5/15/17.
 */

public class EventsRecycleAdapter extends RecyclerView.Adapter<EventsRecycleAdapter.ViewHolder> {

    private final Context context;
    private final RequestManager glide;
    private double lat2, lng2;
    private List<EventsItemList> eventsItemLists;
    private LayoutInflater mInflater;

    public EventsRecycleAdapter(RequestManager Glide, List<EventsItemList> eventsItemLists, Context context) {
        this.eventsItemLists = eventsItemLists;
        this.context = context;
        this.glide = Glide;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public EventsRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.list_item_events, parent, false);

        return new EventsRecycleAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EventsRecycleAdapter.ViewHolder holder, int position) {

        final EventsItemList kudaShoditListItem = eventsItemLists.get(position);
        holder.name.setText(kudaShoditListItem.getName());

        glide.load(kudaShoditListItem.getImageUrl())
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);

        holder.category.setText(kudaShoditListItem.getCategory());

        if(kudaShoditListItem.getAddress().length()<2){
            holder.address.setVisibility(View.GONE);
        }else {
            holder.address.setText(kudaShoditListItem.getAddress());
        }

        holder.date.setText(kudaShoditListItem.getDate());
        Location startPoint=new Location("locationA");

        SharedPreferences sharedPref = context.getSharedPreferences("app", MODE_PRIVATE);
        String latitude = sharedPref.getString("lat", "null");
        String lon = sharedPref.getString("lon", "null");
        if (!latitude.equals("null")) {
            lat2 = Double.parseDouble(latitude);
            lng2 = Double.parseDouble(lon);
        }
        startPoint.setLatitude(lat2);
        startPoint.setLongitude(lng2);


        Location endPoint=new Location("locationB");
        if(kudaShoditListItem.getLat().equals("null")){
            holder.distance.setVisibility(View.GONE);
        }else {
            endPoint.setLatitude(Double.parseDouble(kudaShoditListItem.getLat()));
            endPoint.setLongitude(Double.parseDouble(kudaShoditListItem.getLon()));

            double distanceDouble = startPoint.distanceTo(endPoint);
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
    }

    @Override
    public int getItemCount() {
        return eventsItemLists.size();
    }
    @Override
    public void onViewRecycled(EventsRecycleAdapter.ViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.clear(holder.imageView);
        Log.d("EventsRecycle", "Glide clear triggered");
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private ImageView imageView;
        private TextView category;
        private TextView address;
        private TextView date;
        private TextView distance;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.name);
            imageView = (ImageView)itemView.findViewById(R.id.imageView);
            category = (TextView)itemView.findViewById(R.id.category);
            date = (TextView)itemView.findViewById(R.id.date);
            address = (TextView)itemView.findViewById(R.id.address);
            distance = (TextView)itemView.findViewById(R.id.distance);

        }
    }


}