package kz.welcometoastana.Events;

import kz.welcometoastana.Nearby.mainListItem;

/**
 * Created by nurdaulet on 5/15/17.
 */
public class EventsItemList extends mainListItem {

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
    private String url;



    public EventsItemList(String name, String description, String imageUrl, String category,String lon, String lat, int id,String data,String address, String money, String url){
        this.name = name;
        this.summary = description;
        this.imageUrl = imageUrl;
        this.category = category;
        this.lon=lon;
        this.lat=lat;
        this.id = id;
        this.date = data;
        this.address = address;
        this.money = money;
        this.url = url;
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

    public String getUrl() {
        return url;
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