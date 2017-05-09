package com.nurdaulet.project.ListView;

/**
 * Created by nurdaulet on 4/28/17.
 */

import java.util.ArrayList;

public class group {
    //PROPERTIES OF A SINGLE TEAM
    public String Name;
    public String Image;
    public ArrayList<String> items =new ArrayList<String>();
    public group(String Name)
    {
        this.Name=Name;
    }
    @Override
    public String toString() {
        return Name;
    }
}
