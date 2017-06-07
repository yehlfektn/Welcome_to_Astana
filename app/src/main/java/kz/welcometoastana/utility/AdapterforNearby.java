package kz.welcometoastana.utility;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import kz.welcometoastana.R;

/**
 * Created by nurdaulet on 5/31/17.
 */

public class AdapterforNearby extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private listItemNearby listItemNearby;

    public AdapterforNearby(Context context, listItemNearby listItemNearby) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listItemNearby = listItemNearby;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View itemView = layoutInflater.inflate(R.layout.pager_layout_nearby, container, false);


        Log.d("AdadterNearby", "eventsSize: " + listItemNearby.getEvents().get(0).getName());


        TextView name = (TextView) itemView.findViewById(R.id.name1);
        TextView name2 = (TextView) itemView.findViewById(R.id.name2);

        TextView category = (TextView) itemView.findViewById(R.id.category);
        TextView category2 = (TextView) itemView.findViewById(R.id.category2);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.image1);
        ImageView imageView2 = (ImageView) itemView.findViewById(R.id.image2);

        name.setText(listItemNearby.getEvents().get(0).getName());
        name2.setText(listItemNearby.getEvents().get(1).getName());

        category.setText(listItemNearby.getEvents().get(0).getCategory());
        category2.setText(listItemNearby.getEvents().get(0).getCategory());

        Glide.with(context)
                .load(listItemNearby.getEvents().get(0).getImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(context, 25, 2))
                .into(imageView);

        Glide.with(context)
                .load(listItemNearby.getEvents().get(1).getImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(context, 25, 2))
                .into(imageView2);


        container.addView(itemView);
        return itemView;
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
        }

        return null;
    }
}
