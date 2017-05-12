package com.nurdaulet.project.GdePoest;

/**
 * Created by nurdaulet on 5/11/17.
 */

public class GdePoestListItem {

    private String name;
    private String summary;
    private String imageUrl;
    private String category;
    private String lon;
    private String lat;
    private String phone;
    private String address;



    public GdePoestListItem(String name, String description, String imageUrl, String category,String lon, String lat, String phone, String address){
        this.name = name+" >";
        this.summary = description;
        this.imageUrl = imageUrl;
        this.category = category;
        this.lon=lon;
        this.lat=lat;
        this.phone=phone;
        this.address=address;
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

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getCategory() {

        return category;
    }
}