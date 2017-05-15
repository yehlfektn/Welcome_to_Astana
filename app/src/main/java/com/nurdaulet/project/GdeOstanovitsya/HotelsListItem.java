package com.nurdaulet.project.GdeOstanovitsya;

/**
 * Created by nurdaulet on 5/13/17.
 */

public class HotelsListItem {
    private String name;
    private String summary;
    private String imageUrl;
    private String category;
    private String lon;
    private String lat;
    private String phone;
    private String address;
    private int stars;
    private String website;
    private int id;



    public HotelsListItem(String name, String description, String imageUrl, String category,String lon, String lat, String phone, String address, int Stars, String website,int id){
        this.name = name+" >";
        this.summary = description;
        this.imageUrl = imageUrl;
        this.category = category;
        this.lon=lon;
        this.lat=lat;
        if(phone.length()==0){
            this.phone = "Не указано";
        }else {
            this.phone=phone;
        }
        if(address.length()==0){
            this.address = "Не указано";
        }else{
            this.address=address;
        }

        this.stars = Stars;
        this.website = website;
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

    public int getStars() {
        return stars;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getWebsite() {
        return website;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {


        return category;
    }
}