package com.example.pickinglist;

import java.util.ArrayList;
import java.util.List;

public class Location {
    private String locationName;
    private Long shelvingUnit; //scaffale
    private Long shelf; //ripiano
    private String section; //sezione o area di stoccaggio
    private Long sectionId;

    private ArrayList<Article> items;

    //Constructor
    public Location(long shelvingUnit, long shelf, String section)
    {
        this.shelvingUnit = shelvingUnit;
        this.shelf = shelf;
        this.section = section;
        this.sectionId = Long.valueOf(0);

        this.locationName = "Scaffale " + shelvingUnit + ", ripiano " + shelf + ", sezione " + section;
    }

    //Getters
    public String getLocationName(){ return this.locationName; }
    public Long getSectionId(){ return this.sectionId; }
    public Long getShelvingUnit(){ return this.shelvingUnit; }
    public Long getShelf(){ return this.shelf; }
    public String getSection(){ return this.section; }
    public ArrayList<Article> getItems(){ return this.items; }
}
