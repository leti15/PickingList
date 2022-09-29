package com.example.pickinglist;

import java.util.ArrayList;

public class Location {
    private long id;
    private String code;
    private String name;
    private long warehouseId;
    private long plantId;
    public long parentId;
    public ArrayList<Location> path;

    //Constructor
    public Location( long id, long warehouseId, String code, String name, long plantId, long parentId, ArrayList<Location> path)
    {
        this.id = id;
        this.code = code;
        this.name = name;
        this.warehouseId = warehouseId;
        this.plantId = plantId;
        this.parentId = parentId;
        this.path = path;
    }

    //Getters & Setters
    public long getId() {
        return id;
    }
    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }
    public long getWarehouseId() {
        return warehouseId;
    }
    public long getPlantId() {
        return plantId;
    }
    public long getParentId() {
        return parentId;
    }
    public ArrayList<Location> getPath() {
        return path;
    }

    //Methods
    public String getLocationToStringCustomized()
    {
        String s = " ";
        Integer i = 1;
        Location tmp;

        if(this.path.size() > 1)
            do{
                tmp = this.path.get(i);
                s += tmp.name + "\n";
                i++;
            }while (i < this.path.size());
        else
            s += "Informazioni sulla locazione non disponibili";

        return s.trim();
    }

    public String getStorageUnit( Integer depth)
    {
        if(path.size() > depth)
            return path.get(depth).name;
        else
            return " ";
    }
}
