package com.example.pickinglist;

public class Article {
    private int qta;
    private int needingQta;
    private String measureUnit;
    private String name;
    private String id;
    private Location location;




    //Constructor
    public Article( int need, String unit, String name, String id, int avaiable, Location location)
    {
        this.qta = 0;
        this.needingQta = need;
        this.measureUnit = unit;
        this.name = name;
        this.id = id;
        this.location = location;
    }
    //Getters
    public int GetQta(){
        return this.qta;
    }
    public void SetQta(int qta){ this.qta = qta; }
    public int GetNeedingQta(){
        return this.needingQta;
    }
    public String GetMeasureUnits(){
        return this.measureUnit;
    }
    public String GetName(){
        return this.name;
    }
    public String Getid(){ return this.id; }
    public Location GetLocation(){return this.location;}
    public String GetLocationStringForAdapter()
    {
        return this.location.getLocationToStringCostumize(false, true, true, true, false);
    }

}
