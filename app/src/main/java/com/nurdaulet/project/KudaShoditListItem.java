package com.nurdaulet.project;

/**
 * Created by nurdaulet on 5/5/17.
 */

public class KudaShoditListItem {

    private String name;
    private String summary;
    private String imageUrl;
    private String category;
    private String lon;
    private String lat;



    public KudaShoditListItem(String name, String description, String imageUrl, String category,String lon, String lat){
        this.name = name+" >";
        this.summary = description;
        this.imageUrl = imageUrl;
        this.category = category;
        this.lon=lon;
        this.lat=lat;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSummary() {
        return summary;
    }

    public String getLon() {
        return lon;
    }

    public String getLat() {
        return lat;
    }

    public String getCategory() {

        return category;
    }
}
