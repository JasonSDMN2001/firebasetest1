package com.unipi.boidanis.firebasetest1;

public class FacePicture {
    String imageUrl,key;
    int day;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public FacePicture(){}

    public FacePicture(String imageUrl,String key,int day) {
        this.imageUrl = imageUrl;
        this.key=key;
        this.day=day;
    }
}
