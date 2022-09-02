package com.example.pickinglist;

public class Article {
    private int qta;
    private int needingQta;
    private String measureUnit;
    private String name;
    private long id;
    private int remainingQta;
    private Location location;

    //Constructor
    public Article( int need, String unit, String name, long id, int avaiable, Location location){
        this.qta = 0;
        this.needingQta = need;
        this.measureUnit = unit;
        this.name = name;
        this.id = id;
        this.remainingQta = avaiable;
        this.location = location;
    }
    //Getters
    public int GetQta(){
        return this.qta;
    }
    public int GetNeedingQta(){
        return this.needingQta;
    }
    public String GetMeasureUnits(){
        return this.measureUnit;
    }
    public String GetName(){
        return this.name;
    }
    public long Getid(){ return this.id; }
    public int GetRemainingQta(){
        return this.remainingQta;
    }
    public Location GetLocation(){return this.location;}

}
