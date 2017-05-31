package kz.welcometoastana.utility;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kz.welcometoastana.R;

/**
 * Created by nurdaulet on 5/31/17.
 */

public class AdapterforNearby extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<listItemNearby> arrayList;

    public AdapterforNearby(Context context, ArrayList<listItemNearby> arrayList) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = layoutInflater.inflate(R.layout.pager_layout_nearby, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.image1);
        ImageView imageView2 = (ImageView) itemView.findViewById(R.id.image2);
        TextView name = (TextView) itemView.findViewById(R.id.name1);
        TextView name2 = (TextView) itemView.findViewById(R.id.name2);
        TextView category = (TextView) itemView.findViewById(R.id.category);
        TextView category2 = (TextView) itemView.findViewById(R.id.category2);

        listItemNearby listItemNearby = arrayList.get(position);
        Glide.with(context).load(listItemNearby.getFirstImg())
                .centerCrop()
                .bitmapTransform(new RoundedCornersTransformation(context, 40, 2))
                .into(imageView);

        Glide.with(context).load(listItemNearby.getSecondImg())
                .bitmapTransform(new RoundedCornersTransformation(context, 40, 2))
                .into(imageView2);

        name.setText(listItemNearby.getFirstName());
        name2.setText(listItemNearby.getSecondName());
        category.setText(listItemNearby.getCategory());
        category2.setText(listItemNearby.getCategory());


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
