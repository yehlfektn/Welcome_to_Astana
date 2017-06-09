package kz.welcometoastana.Nearby;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import kz.welcometoastana.Events.EventsDescription;
import kz.welcometoastana.Events.EventsItemList;
import kz.welcometoastana.GdeOstanovitsya.GdeOstanovitsyaDescription;
import kz.welcometoastana.GdeOstanovitsya.HotelsListItem;
import kz.welcometoastana.GdePoest.GdePoestDescription;
import kz.welcometoastana.GdePoest.GdePoestListItem;
import kz.welcometoastana.KudaShoditListItem;
import kz.welcometoastana.R;
import kz.welcometoastana.Sightseeings.DescriptionActivity;
import kz.welcometoastana.utility.RoundedCornersTransformation;

/**
 * Created by nurdaulet on 5/31/17.
 */

public class AdapterforNearby5 extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private listItemNearby listItemNearby;
    private String color;

    public AdapterforNearby5(Context context, listItemNearby listItemNearby, String color) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listItemNearby = listItemNearby;
        this.color = color;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View itemView = layoutInflater.inflate(R.layout.pager_layout_nearby5, container, false);


        TextView name = (TextView) itemView.findViewById(R.id.name1);
        TextView name2 = (TextView) itemView.findViewById(R.id.name2);
        TextView name3 = (TextView) itemView.findViewById(R.id.name3);
        TextView name4 = (TextView) itemView.findViewById(R.id.name4);
        TextView name5 = (TextView) itemView.findViewById(R.id.name5);

        TextView category = (TextView) itemView.findViewById(R.id.category);
        TextView category2 = (TextView) itemView.findViewById(R.id.category2);
        TextView category3 = (TextView) itemView.findViewById(R.id.category3);
        TextView category4 = (TextView) itemView.findViewById(R.id.category4);
        TextView category5 = (TextView) itemView.findViewById(R.id.category5);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.image1);
        ImageView imageView2 = (ImageView) itemView.findViewById(R.id.image2);
        ImageView imageView3 = (ImageView) itemView.findViewById(R.id.image3);
        ImageView imageView4 = (ImageView) itemView.findViewById(R.id.image4);
        ImageView imageView5 = (ImageView) itemView.findViewById(R.id.image5);

        LinearLayout linearLayout = (LinearLayout) itemView.findViewById(R.id.nearbyLayout1);
        LinearLayout linearLayout2 = (LinearLayout) itemView.findViewById(R.id.nearbyLayout2);
        LinearLayout linearLayout3 = (LinearLayout) itemView.findViewById(R.id.nearbyLayout3);
        LinearLayout linearLayout4 = (LinearLayout) itemView.findViewById(R.id.nearbyLayout4);
        LinearLayout linearLayout5 = (LinearLayout) itemView.findViewById(R.id.nearbyLayout5);

        mainListItem first;
        mainListItem second;
        mainListItem third;
        mainListItem fourth;
        mainListItem fifth;

        if (position == 0) {
            first = listItemNearby.getSights().get(0);
            second = listItemNearby.getSights().get(1);
            third = listItemNearby.getSights().get(2);
            fourth = listItemNearby.getSights().get(3);
            fifth = listItemNearby.getSights().get(4);
        } else if (position == 1) {
            first = listItemNearby.getHotels().get(0);
            second = listItemNearby.getHotels().get(1);
            third = listItemNearby.getHotels().get(2);
            fourth = listItemNearby.getHotels().get(3);
            fifth = listItemNearby.getHotels().get(4);
        } else if (position == 2) {
            first = listItemNearby.getFoods().get(0);
            second = listItemNearby.getFoods().get(1);
            third = listItemNearby.getFoods().get(2);
            fourth = listItemNearby.getFoods().get(3);
            fifth = listItemNearby.getFoods().get(4);
        } else {
            first = listItemNearby.getEvents().get(0);
            second = listItemNearby.getEvents().get(1);
            third = listItemNearby.getEvents().get(2);
            fourth = listItemNearby.getEvents().get(3);
            fifth = listItemNearby.getEvents().get(4);

        }

        name.setText(first.getName());
        name2.setText(second.getName());
        name3.setText(third.getName());
        name4.setText(fourth.getName());
        name5.setText(fifth.getName());

        category.setText(first.getCategory());
        category2.setText(second.getCategory());
        category3.setText(third.getCategory());
        category4.setText(fourth.getCategory());
        category5.setText(fifth.getCategory());

        if (color.equals("green")) {
            category.setTextColor(ContextCompat.getColor(context, R.color.green));
            category2.setTextColor(ContextCompat.getColor(context, R.color.green));
            category3.setTextColor(ContextCompat.getColor(context, R.color.green));
            category4.setTextColor(ContextCompat.getColor(context, R.color.green));
            category5.setTextColor(ContextCompat.getColor(context, R.color.green));
            category.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bulletgreen, 0, 0, 0);
            category2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bulletgreen, 0, 0, 0);
            category3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bulletgreen, 0, 0, 0);
            category4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bulletgreen, 0, 0, 0);
            category5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bulletgreen, 0, 0, 0);
        } else if (color.equals("purple")) {
            category.setTextColor(ContextCompat.getColor(context, R.color.purpleLight));
            category2.setTextColor(ContextCompat.getColor(context, R.color.purpleLight));
            category3.setTextColor(ContextCompat.getColor(context, R.color.purpleLight));
            category4.setTextColor(ContextCompat.getColor(context, R.color.purpleLight));
            category5.setTextColor(ContextCompat.getColor(context, R.color.purpleLight));
            category.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bulletpurple, 0, 0, 0);
            category2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bulletpurple, 0, 0, 0);
            category3.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bulletpurple, 0, 0, 0);
            category4.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bulletpurple, 0, 0, 0);
            category5.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bulletpurple, 0, 0, 0);
        }

        Glide.with(context)
                .load(first.getImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(context, 25, 2))
                .into(imageView);

        Glide.with(context)
                .load(second.getImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(context, 25, 2))
                .into(imageView2);

        Glide.with(context)
                .load(third.getImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(context, 25, 2))
                .into(imageView3);

        Glide.with(context)
                .load(fourth.getImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(context, 25, 2))
                .into(imageView4);

        Glide.with(context)
                .load(fifth.getImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(context, 25, 2))
                .into(imageView5);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    onClickSight(0);
                } else if (position == 1) {
                    onClickHotel(0);
                } else if (position == 2) {
                    onClickFood(0);
                } else {
                    onClickEvent(0);
                }
            }
        });
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    onClickSight(1);
                } else if (position == 1) {
                    onClickHotel(1);
                } else if (position == 2) {
                    onClickFood(1);
                } else {
                    onClickEvent(1);
                }
            }
        });

        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    onClickSight(2);
                } else if (position == 1) {
                    onClickHotel(2);
                } else if (position == 2) {
                    onClickFood(2);
                } else {
                    onClickEvent(2);
                }
            }
        });

        linearLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    onClickSight(3);
                } else if (position == 1) {
                    onClickHotel(3);
                } else if (position == 2) {
                    onClickFood(3);
                } else {
                    onClickEvent(3);
                }
            }
        });

        linearLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    onClickSight(4);
                } else if (position == 1) {
                    onClickHotel(4);
                } else if (position == 2) {
                    onClickFood(4);
                } else {
                    onClickEvent(4);
                }
            }
        });


        container.addView(itemView);
        return itemView;
    }

    private void onClickSight(int position) {
        Intent intent = new Intent(context, DescriptionActivity.class);
        final List<KudaShoditListItem> kudaShoditListItems = listItemNearby.getSights();
        intent.putExtra("name", kudaShoditListItems.get(position).getName());
        intent.putExtra("id", kudaShoditListItems.get(position).getId());
        intent.putExtra("description", kudaShoditListItems.get(position).getSummary());
        intent.putExtra("imageUrl", kudaShoditListItems.get(position).getImageUrl());
        intent.putExtra("category", kudaShoditListItems.get(position).getCategory());
        intent.putExtra("longit", kudaShoditListItems.get(position).getLon());
        intent.putExtra("latit", kudaShoditListItems.get(position).getLat());
        intent.putExtra("url", "http://89.219.32.107/api/v1/places/sightseeings?limit=2000&page=1");
        intent.putExtra("address", kudaShoditListItems.get(position).getAddress());
        ((Activity) context).startActivityForResult(intent, 0);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void onClickEvent(int position) {
        Intent intent = new Intent(context, EventsDescription.class);
        final EventsItemList eventsItemList = listItemNearby.getEvents().get(position);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("name", eventsItemList.getName());
        intent.putExtra("id", eventsItemList.getId());
        intent.putExtra("description", eventsItemList.getSummary());
        intent.putExtra("imageUrl", eventsItemList.getImageUrl());
        intent.putExtra("category", eventsItemList.getCategory());
        intent.putExtra("longit", eventsItemList.getLon());
        intent.putExtra("latit", eventsItemList.getLat());
        intent.putExtra("url", "http://89.219.32.107/api/v1/places/events?limit=2000&page=1");
        intent.putExtra("address", eventsItemList.getAddress());
        intent.putExtra("money", eventsItemList.getMoney());
        intent.putExtra("date", eventsItemList.getDate());
        intent.putExtra("urlItem", eventsItemList.getUrl());
        ((Activity) context).startActivityForResult(intent, 0);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void onClickHotel(int position) {

        Intent intent = new Intent(context, GdeOstanovitsyaDescription.class);
        final List<HotelsListItem> hotelsListItems = listItemNearby.getHotels();
        intent.putExtra("name", hotelsListItems.get(position).getName());
        intent.putExtra("id", hotelsListItems.get(position).getId());
        intent.putExtra("description", hotelsListItems.get(position).getSummary());
        intent.putExtra("imageUrl", hotelsListItems.get(position).getImageUrl());
        intent.putExtra("category", hotelsListItems.get(position).getCategory());
        intent.putExtra("longit", hotelsListItems.get(position).getLon());
        intent.putExtra("latit", hotelsListItems.get(position).getLat());
        intent.putExtra("address", hotelsListItems.get(position).getAddress());
        intent.putExtra("url", "http://89.219.32.107/api/v1/hotels?limit=2000&page=1");
        intent.putExtra("phone", hotelsListItems.get(position).getPhone());
        intent.putExtra("website", hotelsListItems.get(position).getWebsite());
        intent.putExtra("stars", hotelsListItems.get(position).getStars());
        ((Activity) context).startActivityForResult(intent, 0);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

    private void onClickFood(int position) {
        Intent intent = new Intent(context, GdePoestDescription.class);
        final List<GdePoestListItem> gdePoestListItems = listItemNearby.getFoods();
        intent.putExtra("name", gdePoestListItems.get(position).getName());
        intent.putExtra("id", gdePoestListItems.get(position).getId());
        intent.putExtra("description", gdePoestListItems.get(position).getSummary());
        intent.putExtra("imageUrl", gdePoestListItems.get(position).getImageUrl());
        intent.putExtra("category", gdePoestListItems.get(position).getCategory());
        intent.putExtra("longit", gdePoestListItems.get(position).getLon());
        intent.putExtra("latit", gdePoestListItems.get(position).getLat());
        intent.putExtra("address", gdePoestListItems.get(position).getAddress());
        intent.putExtra("url", "http://89.219.32.107/api/v1/foods?limit=2000&page=1");
        intent.putExtra("phone", gdePoestListItems.get(position).getPhone());
        ((Activity) context).startActivityForResult(intent, 0);
        ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.kuda_shodit);
            case 1:
                return context.getString(R.string.gde_ostanovitsya);
            case 2:
                return context.getString(R.string.gde_poest);
            case 3:
                return context.getString(R.string.events);
        }

        return null;
    }
}
