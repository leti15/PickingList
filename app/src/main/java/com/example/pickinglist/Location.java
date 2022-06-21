package com.example.pickinglist;

import java.util.ArrayList;
import java.util.List;

public class Location {
    private String locationName;
    private long locationId;

    private ArrayList<Article> items;

    //Constructor
    public Location( String name, long id, ArrayList<Article> items)
    {
        this.locationName = name;
        this.locationId = id;
        this.items = items;
    }

    //Getters
    public String getLocationName(){ return this.locationName; }
    public long getLocationId(){ return this.locationId; }
    public ArrayList<Article> getItems(){ return this.items; }
}
