package com.unipi.boidanis.firebasetest1;

import java.util.Date;

public class WeightData {
    private Date date;
    private int week;
    private float weight;
    public WeightData(){}
    public WeightData(Date date,int week,float weight){
        this.date=date;
        this.week=week;
        this.weight=weight;
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
