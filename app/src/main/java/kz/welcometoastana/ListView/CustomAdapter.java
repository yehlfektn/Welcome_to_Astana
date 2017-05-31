package kz.welcometoastana.ListView;

/**
 * Created by nurdaulet on 4/28/17.
 */

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kz.welcometoastana.R;

public class CustomAdapter extends BaseExpandableListAdapter {
    private Context c;
    private ArrayList<group> group;
    private LayoutInflater inflater;

    public CustomAdapter(Context c,ArrayList<group> group)
    {
        this.c=c;
        this.group = group;
        inflater=(LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    //GET A Child
    @Override
    public Object getChild(int groupPos, int childPos) {
        return group.get(groupPos).items.get(childPos);
    }
    //GET CHIlD ID
    @Override
    public long getChildId(int arg0, int arg1) {
        return 0;
    }
    //GET CHILD ROW
    @Override
    public View getChildView(int groupPos, int childPos, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        //ONLY INFLATER XML ROW LAYOUT IF ITS NOT PRESENT,OTHERWISE REUSE IT
        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.items, null);
        }
        //GET CHILD/PLAYER NAME
        String  child=(String) getChild(groupPos, childPos);
        //SET CHILD NAME
        TextView nameTv=(TextView) convertView.findViewById(R.id.textView1);
        ImageView img=(ImageView) convertView.findViewById(R.id.imageView1);
        nameTv.setText(child);
        nameTv.setTextColor(ContextCompat.getColor(c, R.color.gray));
        //GET TEAM NAME
        String groupName= getGroup(groupPos).toString();
        //ASSIGN IMAGES TO PLAYERS ACCORDING TO THEIR NAMES AN TEAMS
        if (groupName.equals(c.getResources().getString(R.string.kuda_shodit)))
        {
            if (child.equals(c.getString(R.string.shopping_and)))
            {
                img.setImageResource(R.mipmap.icon_end_orange)  ;
            } else if (child.equals(c.getString(R.string.sightseegins)))
            {
                img.setImageResource(R.mipmap.icon_bw_orange)  ;
            } else if (child.equals(c.getString(R.string.events)))
            {
                img.setImageResource(R.mipmap.icon_bw_orange)  ;
            } else if (child.equals(c.getString(R.string.excursions)))
            {
                img.setImageResource(R.mipmap.icon_bw_orange)  ;
            }

        } else if (groupName.equals(c.getResources().getString(R.string.gde_poest)))
        {
            if (child.equals(c.getString(R.string.cafe)))
            {
                img.setImageResource(R.mipmap.icon_bw_green)  ;
            } else if (child.equals(c.getString(R.string.restourant)))
            {
                img.setImageResource(R.mipmap.icon_bw_green)  ;
            } else if (child.equals(c.getString(R.string.bar)))
            {
                img.setImageResource(R.mipmap.icon_end_green)  ;
            }
        } else if (groupName.contains(c.getResources().getString(R.string.gde_ostanovitsya)))
        {
            if (child.equals(c.getResources().getString(R.string.hotels)))
            {
                img.setImageResource(R.mipmap.icon_land_bw)  ;
            } else if (child.equals(c.getResources().getString(R.string.hostels)))
            {
                img.setImageResource(R.mipmap.icon_land_bw)  ;
            } else if (child.equals(c.getResources().getString(R.string.aparments)))
            {
                img.setImageResource(R.mipmap.icon_land_end)  ;
            }
        } else if (groupName.equals(c.getResources().getString(R.string.pamyatka)))
        {
            if (child.equals(c.getString(R.string.prebyvanie)))
            {
                img.setImageResource(R.mipmap.icon_bw_yellow)  ;
            } else if (child.equals(c.getString(R.string.transport)))
            {
                img.setImageResource(R.mipmap.icon_bw_yellow)  ;
            } else if (child.equals(c.getString(R.string.poleznaya)))
            {
                img.setImageResource(R.mipmap.icon_bw_yellow)  ;
            } else if (child.equals(c.getString(R.string.extrennaya)))
            {
                img.setImageResource(R.mipmap.icon_end_yellow)  ;
            }

        }

        return convertView;
    }
    //GET NUMBER OF PLAYERS
    @Override
    public int getChildrenCount(int groupPosw) {
        return group.get(groupPosw).items.size();
    }
    //GET TEAM
    @Override
    public Object getGroup(int groupPos) {
        return group.get(groupPos);
    }
    //GET NUMBER OF TEAMS
    @Override
    public int getGroupCount() {
        return group.size();
    }
    //GET TEAM ID
    @Override
    public long getGroupId(int arg0) {
        return 0;
    }
    //GET TEAM ROW
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        //ONLY INFLATE XML TEAM ROW MODEL IF ITS NOT PRESENT,OTHERWISE REUSE IT

        if(isExpanded)
        {
            convertView=inflater.inflate(R.layout.group, null);
        }else{
            convertView=inflater.inflate(R.layout.group_collapsed, null);
        }
        //GET GROUP/TEAM ITEM
        group t=(group) getGroup(groupPosition);
        //SET GROUP NAME
        TextView nameTv=(TextView) convertView.findViewById(R.id.textView1);
        ImageView img=(ImageView) convertView.findViewById(R.id.imageView1);
        String name=t.Name;
        nameTv.setText(name);
        //ASSIGN TEAM IMAGES ACCORDING TO TEAM NAME

        if(isExpanded){

            if (name.equals(c.getResources().getString(R.string.kuda_shodit))) {
                img.setImageResource(R.mipmap.icon_landscape_opened);
            } else if (name.equals(c.getResources().getString(R.string.gde_poest))) {
                img.setImageResource(R.mipmap.icon_burger_opened);
            } else if (name.contains(c.getResources().getString(R.string.gde_ostanovitsya))) {
                img.setImageResource(R.mipmap.icon_location_opened);
            } else if (name.equals(c.getResources().getString(R.string.pamyatka))) {
                img.setImageResource(R.mipmap.icon_medical_opened);
            } else if (name.equals(c.getResources().getString(R.string.expo))) {
                img.setImageResource(R.mipmap.icon_expo);
            } else if (name.equals(c.getResources().getString(R.string.chinese))) {
                img.setImageResource(R.drawable.info_marker);
            }

        }else {

            if (name.equals(c.getResources().getString(R.string.kuda_shodit))) {
                img.setImageResource(R.mipmap.icon_landscape_collapsed);
            } else if (name.equals(c.getResources().getString(R.string.gde_poest))) {
                img.setImageResource(R.mipmap.icon_burger);
            } else if (name.contains(c.getResources().getString(R.string.gde_ostanovitsya))) {
                img.setImageResource(R.mipmap.icon_location);
            } else if (name.equals(c.getResources().getString(R.string.pamyatka))) {
                img.setImageResource(R.mipmap.icon_medical);
            } else if (name.equals(c.getResources().getString(R.string.Expo))) {
                img.setImageResource(R.mipmap.icon_expo);
            } else if (name.equals(c.getResources().getString(R.string.chinese))) {
                img.setImageResource(R.drawable.info_marker);
            }
        }
        //SET TEAM ROW BACKGROUND COLOR
        //convertView.setBackgroundColor(Color.LTGRAY);
        return convertView;
    }
    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }
    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return true;
    }
}