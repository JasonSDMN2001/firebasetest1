package com.unipi.boidanis.firebasetest1;

import java.util.Date;

public class HeightData {
    private Date date;
    private int week;
    private float height;
    private String key;
    private String babyName;

    public String getBabyName() {
        return babyName;
    }

    public void setBabyName(String babyName) {
        this.babyName = babyName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public HeightData(){}
    public HeightData(String key,Date date,int week,float height,String babyName){
        this.key=key;
        this.date=date;
        this.week=week;
        this.height=height;
        this.babyName=babyName;
    }
    public Date getDate(){
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public int getWeek(){
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }
    public float getHeight(){
        return height;
    }

    public void setHeight(float weight) {
        this.height = height;
    }
}
