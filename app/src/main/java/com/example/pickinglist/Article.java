package com.example.pickinglist;

public class Article {
    private long id;
    private int qta;
    private int needingQta;
    private String measureUnit;
    private String name;
    private String registerCode;
    private Location location;

    //Constructor
    public Article(long id, int need, String unit, String name, String registerCode, Location location)
    {
        this.id = id;
        this.qta = 0;
        this.needingQta = need;
        this.measureUnit = unit;
        this.name = name;
        this.registerCode = registerCode;
        this.location = location;
    }

    //Getters & Setters
    public int getQta(){
        return this.qta;
    }
    public void setQta(int qta){ this.qta = qta; }
    public int getNeedingQta(){
        return this.needingQta;
    }
    public String getMeasureUnit(){
        return this.measureUnit;
    }
    public String getName(){
        return this.name;
    }
    public String getRegisterCode(){ return this.registerCode; }
    public Location getLocation(){return this.location;}
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
}
