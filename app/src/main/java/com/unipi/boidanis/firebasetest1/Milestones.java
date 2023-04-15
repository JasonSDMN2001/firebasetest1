package com.unipi.boidanis.firebasetest1;

public class Milestones {
    String milestoneName,key,babyName;
    int upperbound,lowerbound;
    Boolean completed;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBabyName() {
        return babyName;
    }

    public void setBabyName(String babyName) {
        this.babyName = babyName;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName;
    }

    public int getUpperbound() {
        return upperbound;
    }

    public void setUpperbound(int upperbound) {
        this.upperbound = upperbound;
    }

    public int getLowerbound() {
        return lowerbound;
    }

    public void setLowerbound(int lowerbound) {
        this.lowerbound = lowerbound;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
    public Milestones(){}
    public Milestones(String milestoneName, int lowerbound, int upperbound, Boolean completed,String key,String babyName) {
        this.milestoneName = milestoneName;
        this.lowerbound = lowerbound;
        this.upperbound = upperbound;
        this.completed = completed;
        this.key=key;
        this.babyName=babyName;
    }
}
