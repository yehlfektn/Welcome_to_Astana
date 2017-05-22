package kz.welcometoastana.Pamyatka;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import kz.welcometoastana.R;

/**
 * Created by nurdaulet on 5/13/17.
 */

public class PamyatkaRecycleAdapter extends RecyclerView.Adapter<PamyatkaRecycleAdapter.ViewHolder> {

    private final Context context;
    private List<PamyatkaListItem> pamyatkaListItems;

    public PamyatkaRecycleAdapter(List<PamyatkaListItem> pamyatkaListItems, Context context) {
        this.pamyatkaListItems = pamyatkaListItems;
        this.context = context;
    }

    @Override
    public PamyatkaRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pamyatka,parent,false);

        return new PamyatkaRecycleAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PamyatkaRecycleAdapter.ViewHolder holder, int position) {

        final PamyatkaListItem kudaShoditListItem = pamyatkaListItems.get(position);
        holder.name.setText(kudaShoditListItem.getName());


        Glide.with(context)
                .load(kudaShoditListItem.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return pamyatkaListItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public ImageView imageView;




        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.name);
            imageView = (ImageView)itemView.findViewById(R.id.imageView);

        }
    }
}
