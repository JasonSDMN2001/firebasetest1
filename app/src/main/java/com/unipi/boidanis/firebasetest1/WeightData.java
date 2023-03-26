package com.unipi.boidanis.firebasetest1;

import java.util.Date;

public class WeightData {
    private Date date;
    private int week;
    private float weight;
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

    public WeightData(){}
    public WeightData(String key,Date date,int week,float weight,String babyName){
        this.key=key;
        this.date=date;
        this.week=week;
        this.weight=weight;
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
    public float getWeight(){
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
