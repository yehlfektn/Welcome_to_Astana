package com.nurdaulet.project.Events;

/**
 * Created by nurdaulet on 5/15/17.
 */
public class EventsItemList {

    private String name;
    private String summary;
    private String imageUrl;
    private String category;
    private String lon;
    private String lat;
    private int id;
    private String date;
    private String address;
    private String money;



    public EventsItemList(String name, String description, String imageUrl, String category,String lon, String lat, int id,String data,String address, String money){
        this.name = name+" >";
        this.summary = description;
        this.imageUrl = imageUrl;
        this.category = category;
        this.lon=lon;
        this.lat=lat;
        this.id = id;
        this.date = data;
        if(address.length()>2){
            this.address = address;
        }else{
            this.address = "не указано";
        }

        this.money = money;
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

    public String getDate() {
        return date;
    }

    public String getAddress() {
        return address;
    }

    public String getMoney() {
        return money;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {


        return category;
    }
}