package com.unipi.boidanis.firebasetest1;

import java.util.Date;

public class HeadData {
    private Date date;
    private int week;
    private float head;
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

    public HeadData(){}
    public HeadData(String key,Date date,int week,float head,String babyName){
        this.key=key;
        this.date=date;
        this.week=week;
        this.head=head;
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
    public float getHead(){
        return head;
    }

    public void setHead(float head) {
        this.head = head;
    }
}
