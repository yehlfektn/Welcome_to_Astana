package kz.welcometoastana;

import kz.welcometoastana.Nearby.mainListItem;

/**
 * Created by nurdaulet on 5/5/17.
 */

public class KudaShoditListItem extends mainListItem {

    private String summary;
    private String lon;
    private String lat;
    private int id;
    private String address;
    private String url;



    public KudaShoditListItem(String name, String description, String imageUrl, String category,String lon, String lat, int id,String address){
        this.name = name;
        this.summary = description;
        this.imageUrl = imageUrl;
        this.category = category;
        this.lon=lon;
        this.lat=lat;
        this.id = id;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public String getCategory() {
        return category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
