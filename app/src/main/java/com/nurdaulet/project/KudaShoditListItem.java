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
    private int id;



    public KudaShoditListItem(String name, String description, String imageUrl, String category,String lon, String lat, int id){
        this.name = name+" >";
        this.summary = description;
        this.imageUrl = imageUrl;
        this.category = category;
        this.lon=lon;
        this.lat=lat;
        this.id = id;
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

    public int getId() {
        return id;
    }

    public String getCategory() {


        return category;
    }
}
