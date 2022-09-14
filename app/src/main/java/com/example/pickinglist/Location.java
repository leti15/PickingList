package com.example.pickinglist;

import java.util.ArrayList;

public class Location {
    private String locationToString;
    private Long sectionId;

    private String section; //sezione o area di stoccaggio
    private String sectionName; // nome alternativo a sezione
    private Long shelvingUnit; //scaffale
    private String shelvingUnitName; // nome alternativo a scaffale
    private Long shelf; //ripiano
    private String shelfName; // nome alternativo a ripiano
    private Long drawer; // cassetto
    private String drawerName; // cassetto



/*
    private long Id;
    private long WarehouseId;
    private String Code;
    private String Name;
    private long PlantId;
    public long ParentId;
    public boolean Active;
    public Location(long warehouseId, String code, String name, DataApi.Plant p)
    {
        this.Code = code;
        this.Name = name;
        this.PlantId = p.id;
        this.WarehouseId = warehouseId;
    }

*/
    //Constructor
    public Location(String section, long shelvingUnit, long shelf, long drawer)
    {
        this.section = section;
        this.shelvingUnit = shelvingUnit;
        this.shelf = shelf;
        this.drawer = drawer;
        this.sectionId = Long.valueOf(0);

        this.locationToString = "Scaffale " + shelvingUnit + ", ripiano " + shelf + ", cassetto " + drawer;
    }

    public Location(String section, String sectionName, long shelvingUnit, String shelvingUnitName, long shelf, String shelfName, long drawer, String drawerName)
    {
        if(!sectionName.isEmpty())
        {
            this.sectionName = sectionName;
            this.locationToString = sectionName + " " + section + "\n";
        }

        if(!shelvingUnitName.isEmpty())
        {
            this.shelvingUnitName = shelvingUnitName;
            this.locationToString += shelvingUnitName + " " + shelvingUnit + "\n";
        }

        if(!shelfName.isEmpty())
        {
            this.shelfName = shelfName;
            this.locationToString += shelfName + " " + shelf + "\n";
        }

        if(!drawerName.isEmpty())
        {
            this.drawerName = drawerName;
            this.locationToString += drawerName + " " + drawer + "\n";
        }

        this.section = section;
        this.shelvingUnit = shelvingUnit;
        this.shelf = shelf;
        this.drawer = drawer;

        this.sectionId = Long.valueOf(0);
    }

    //Getters
    public String getLocationToString(){ return this.locationToString; }
    public Long getSectionId(){ return this.sectionId; }
    public String getSection(){ return this.section; }
    public Long getShelvingUnit(){ return this.shelvingUnit; }
    public Long getShelf(){ return this.shelf; }
    public Long getDrawer(){ return this.drawer; }

    public String getLocationToStringCostumize(Boolean section, Boolean shelvingUnit, Boolean shelf, Boolean drawer, Boolean useDefaultName)
    {
        String s = " ";

        if(useDefaultName)
        {
            if(section)
                s += "Sezione " + this.section + "\n";

            if(shelvingUnit)
                s += "Scaffale " + this.shelvingUnit + "\n";

            if(shelf)
                s += "Ripiano " + this.shelf + "\n";

            if(shelvingUnit)
                s += "Cassetto " + this.shelvingUnit + "\n";
        }
        else
        {
            if(section)
            {
                if(!this.sectionName.isEmpty())
                    s += this.sectionName + " " + this.section + "\n";
                else
                    s += "Sezione " + this.section + "\n";
            }

            if(shelvingUnit)
            {
                if(!this.shelvingUnitName.isEmpty())
                    s += this.shelvingUnitName + " " + this.shelvingUnit + "\n";
                else
                    s += "Scaffale " + this.shelvingUnit + "\n";
            }

            if(shelf)
            {
                if(!this.shelfName.isEmpty())
                    s += this.shelfName + " " + this.shelf + "\n";
                else
                    s += "Ripiano " + this.shelf + "\n";
            }

            if(drawer)
            {
                if(!this.drawerName.isEmpty())
                    s += this.drawerName + " " + this.drawer + "\n";
                else
                    s += "Cassetto " + this.drawer + "\n";
            }
        }

        return s.trim();
    }
}
