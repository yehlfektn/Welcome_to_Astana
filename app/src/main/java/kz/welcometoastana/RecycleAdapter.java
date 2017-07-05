package kz.welcometoastana;


import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by nurdaulet on 5/5/17.
 */

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

    private final Context context;
    private final RequestManager glide;
    private List<KudaShoditListItem> kudaShoditListItems;
    private LayoutInflater mInflater;

    public RecycleAdapter(RequestManager Glide, List<KudaShoditListItem> kudaShoditListItems, Context context) {
        this.kudaShoditListItems = kudaShoditListItems;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.glide = Glide;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.list_item_kudashodit, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final KudaShoditListItem kudaShoditListItem = kudaShoditListItems.get(position);
        holder.name.setText(kudaShoditListItem.getName());
        holder.category.setText(kudaShoditListItem.getCategory());

        Location startPoint = new Location("locationA");
        SharedPreferences sharedPref = context.getSharedPreferences("app", MODE_PRIVATE);
        String latitude = sharedPref.getString("lat", "null");
        String lon = sharedPref.getString("lon", "null");
        double lat2 = 0.0;
        double lng2 = 0.0;
        if (!latitude.equals("null")) {
            lat2 = Double.parseDouble(latitude);
            lng2 = Double.parseDouble(lon);
        }
        startPoint.setLatitude(lat2);
        startPoint.setLongitude(lng2);


        Location endPoint = new Location("locationB");
        if (kudaShoditListItem.getLat().equals("null")) {
            holder.distance.setVisibility(View.GONE);
        } else {
            endPoint.setLatitude(Double.parseDouble(kudaShoditListItem.getLat()));
            endPoint.setLongitude(Double.parseDouble(kudaShoditListItem.getLon()));

            double distanceDouble = startPoint.distanceTo(endPoint);
            //Intent intent = new Intent(get, DescriptionActivity.class);

            if (distanceDouble > 5000000) {
                holder.distance.setVisibility(View.GONE);

            } else {
                if (distanceDouble > 1000) {
                    holder.distance.setText(" " + (int) distanceDouble / 1000 + "." + (int) ((distanceDouble % 1000) / 100) + "км ");
                } else {
                    holder.distance.setText(" " + (int) distanceDouble + "м ");
                }
            }
        }
        glide.load(kudaShoditListItem.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return kudaShoditListItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public ImageView imageView;
        public RelativeLayout relativeLayout;
        public TextView category;
        public TextView distance;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout);
            category = (TextView) itemView.findViewById(R.id.category);
            distance = (TextView) itemView.findViewById(R.id.distance);
        }
    }


}
