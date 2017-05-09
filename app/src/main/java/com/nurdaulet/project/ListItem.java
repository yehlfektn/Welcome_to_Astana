package com.nurdaulet.project;

/**
 * Created by nurdaulet on 5/5/17.
 */

public class ListItem {

    private String name;
    private String summary;
    private String imageUrl;

    public ListItem(String name, String description, String imageUrl){
        this.name = name;
        this.summary = description;
        this.imageUrl = imageUrl;
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
}
