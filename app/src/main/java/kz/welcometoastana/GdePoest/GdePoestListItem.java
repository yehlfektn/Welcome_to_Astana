package kz.welcometoastana.GdePoest;

import kz.welcometoastana.Nearby.mainListItem;

/**
 * Created by nurdaulet on 5/11/17.
 */

public class GdePoestListItem extends mainListItem {

    private String name;
    private String summary;
    private String imageUrl;
    private String category;
    private String lon;
    private String lat;
    private String phone;
    private String address;
    private int id;



    public GdePoestListItem(String name, String description, String imageUrl, String category,String lon, String lat, String phone, String address,int id){
        this.name = name;
        this.summary = description;
        this.imageUrl = imageUrl;
        this.category = category;
        this.lon=lon;
        this.lat=lat;
        this.phone=phone;
        this.address=address;
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

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {


        return category;
    }
}