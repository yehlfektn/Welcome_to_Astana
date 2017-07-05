package kz.welcometoastana.GdeOstanovitsya;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
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
import android.widget.TextView;

import com.bumptech.glide.RequestManager;

import java.util.List;

import kz.welcometoastana.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by nurdaulet on 5/5/17.
 */

public class HotelsRecycleAdapter extends RecyclerView.Adapter<HotelsRecycleAdapter.ViewHolder> {

    private final Context context;
    private final RequestManager glide;
    private List<HotelsListItem> hotelsListItems;
    private LayoutInflater mInflater;

    public HotelsRecycleAdapter(RequestManager Glide, List<HotelsListItem> hotelsListItems, Context context) {
        this.hotelsListItems = hotelsListItems;
        this.context = context;
        this.glide = Glide;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public HotelsRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.list_item_gdeostanovitsya, parent, false);

        return new HotelsRecycleAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HotelsRecycleAdapter.ViewHolder holder, int position) {

        final HotelsListItem hotelsListItem = hotelsListItems.get(position);
        holder.name.setText(hotelsListItem.getName());
        holder.category.setText(hotelsListItem.getCategory());
        holder.location.setText(hotelsListItem.getAddress());
        holder.phone.setText(hotelsListItem.getPhone());

        glide.load(hotelsListItem.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);

        LayerDrawable stars = (LayerDrawable) holder.ratingBar.getProgressDrawable();

        setRatingStarColor(stars.getDrawable(2), ContextCompat.getColor(context, R.color.foreground));
        // Half filled stars
        setRatingStarColor(stars.getDrawable(1), ContextCompat.getColor(context, R.color.background));
        // Empty stars
        setRatingStarColor(stars.getDrawable(0), ContextCompat.getColor(context, R.color.background));
        holder.ratingBar.setRating(hotelsListItems.get(position).getStars());

        Location startPoint=new Location("locationA");
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

        Location endPoint=new Location("locationB");
        if(hotelsListItem.getLat().equals("null")){
            holder.distance.setVisibility(View.GONE);
        }else {
            endPoint.setLatitude(Double.parseDouble(hotelsListItem.getLat()));
            endPoint.setLongitude(Double.parseDouble(hotelsListItem.getLon()));

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
        return hotelsListItems.size();
    }


    private void setRatingStarColor(Drawable drawable, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DrawableCompat.setTint(drawable, color);
        } else {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView distance;
        private TextView name;
        private ImageView imageView;
        private TextView category;
        private TextView phone;
        private TextView location;
        private RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.name);
            imageView = (ImageView)itemView.findViewById(R.id.imageView);
            category = (TextView)itemView.findViewById(R.id.category);
            phone = (TextView)itemView.findViewById(R.id.phoneGde);
            location = (TextView)itemView.findViewById(R.id.locationGde);
            ratingBar = (RatingBar)itemView.findViewById(R.id.ratingBar);
            distance = (TextView)itemView.findViewById(R.id.distance);
        }
    }

}

