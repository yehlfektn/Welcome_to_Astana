package kz.welcometoastana.utility;

import java.util.List;

import kz.welcometoastana.Events.EventsItemList;
import kz.welcometoastana.GdeOstanovitsya.HotelsListItem;
import kz.welcometoastana.GdePoest.GdePoestListItem;
import kz.welcometoastana.KudaShoditListItem;

/**
 * Created by nurdaulet on 5/31/17.
 */

public class listItemNearby {
    private List<EventsItemList> events;
    private List<HotelsListItem> hotels;
    private List<GdePoestListItem> foods;
    private List<KudaShoditListItem> sights;

    public listItemNearby(List<EventsItemList> events, List<HotelsListItem> hotels, List<GdePoestListItem> foods, List<KudaShoditListItem> sights) {
        this.events = events;
        this.hotels = hotels;
        this.foods = foods;
        this.sights = sights;
    }

    public List<EventsItemList> getEvents() {
        return events;
    }

    public List<HotelsListItem> getHotels() {
        return hotels;
    }

    public List<KudaShoditListItem> getSights() {
        return sights;
    }

    public List<GdePoestListItem> getFoods() {
        return foods;
    }
}


