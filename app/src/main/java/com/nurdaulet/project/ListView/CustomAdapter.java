package com.nurdaulet.project.ListView;

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

import com.nurdaulet.project.R;

import java.util.ArrayList;

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
        if(groupName=="КУДА СХОДИТЬ")
        {
            if(child=="Шоппинг и Развлечения")
            {
                img.setImageResource(R.mipmap.icon_end_orange)  ;
            }else if(child=="Достопримечательности")
            {
                img.setImageResource(R.mipmap.icon_bw_orange)  ;
            }else if(child=="События")
            {
                img.setImageResource(R.mipmap.icon_bw_orange)  ;
            }else if(child=="Экскурсии")
            {
                img.setImageResource(R.mipmap.icon_bw_orange)  ;
            }

        }else if(groupName=="ГДЕ ПОЕСТЬ")
        {
            if(child=="Кафе")
            {
                img.setImageResource(R.mipmap.icon_bw_green)  ;
            }else if(child=="Рестораны")
            {
                img.setImageResource(R.mipmap.icon_bw_green)  ;
            }else if(child=="Бары")
            {
                img.setImageResource(R.mipmap.icon_end_green)  ;
            }
        }else if(groupName=="ГДЕ ОСТАНОВИТЬСЯ")
        {
            if(child=="Гостиницы")
            {
                img.setImageResource(R.mipmap.icon_land_bw)  ;
            }else if(child=="Хостелы")
            {
                img.setImageResource(R.mipmap.icon_land_bw)  ;
            }else if(child=="Апартаменты")
            {
                img.setImageResource(R.mipmap.icon_land_end)  ;
            }
        }else if(groupName=="ПАМЯТКА ТУРИСТУ")
        {
            if(child=="Пребывание")
            {
                img.setImageResource(R.mipmap.icon_bw_yellow)  ;
            }else if(child=="Транспорт")
            {
                img.setImageResource(R.mipmap.icon_bw_yellow)  ;
            }else if(child=="Полезная информация")
            {
                img.setImageResource(R.mipmap.icon_bw_yellow)  ;
            }else if(child=="Экстренная помощь")
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

            if (name == "КУДА СХОДИТЬ") {
                img.setImageResource(R.mipmap.icon_landscape_opened);
            } else if (name == "ГДЕ ПОЕСТЬ") {
                img.setImageResource(R.mipmap.icon_burger_opened);
            } else if (name == "ГДЕ ОСТАНОВИТЬСЯ") {
                img.setImageResource(R.mipmap.icon_location_opened);
            } else if (name == "ПАМЯТКА ТУРИСТУ") {
                img.setImageResource(R.mipmap.icon_medical_opened);
            } else if (name == "EXPO") {
                img.setImageResource(R.mipmap.icon_expo);
            }

        }else {

            if (name == "КУДА СХОДИТЬ") {
                img.setImageResource(R.mipmap.icon_landscape_collapsed);
            } else if (name == "ГДЕ ПОЕСТЬ") {
                img.setImageResource(R.mipmap.icon_burger);
            } else if (name == "ГДЕ ОСТАНОВИТЬСЯ") {
                img.setImageResource(R.mipmap.icon_location);
            } else if (name == "ПАМЯТКА ТУРИСТУ") {
                img.setImageResource(R.mipmap.icon_medical);
            } else if (name == "EXPO") {
                img.setImageResource(R.mipmap.icon_expo);
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