package kz.welcometoastana.utility;

import java.util.List;

import kz.welcometoastana.Events.EventsItemList;
import kz.welcometoastana.GdeOstanovitsya.HotelsListItem;
import kz.welcometoastana.GdePoest.GdePoestListItem;

/**
 * Created by nurdaulet on 5/31/17.
 */

public class listItemNearby {
    private List<EventsItemList> events;
    private List<HotelsListItem> hotels;
    private List<GdePoestListItem> foods;

    public listItemNearby(List<EventsItemList> events, List<HotelsListItem> hotels, List<GdePoestListItem> foods) {
        this.events = events;
        this.hotels = hotels;
        this.foods = foods;
    }

    public List<EventsItemList> getEvents() {
        return events;
    }

    public List<HotelsListItem> getHotels() {
        return hotels;
    }

    public List<GdePoestListItem> getFoods() {
        return foods;
    }
}


