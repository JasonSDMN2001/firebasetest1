package com.unipi.boidanis.firebasetest1;

public class WeightData {
    private String date,week,weight;
    public WeightData(){}
    public WeightData(String date,String week,String weight){
        this.date=date;
        this.week=week;
        this.weight=weight;
    }
    public String getDate(){
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getWeek(){
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }
    public String getWeight(){
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
