package com.unipi.boidanis.firebasetest1;

public class MomentImages {
    String key;
    String momentName,babyName;
    String imageUrl;

    public MomentImages(){}
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBabyName() {
        return babyName;
    }

    public void setBabyName(String babyName) {
        this.babyName = babyName;
    }

    public String getMomentName() {
        return momentName;
    }
    public void setMomentName(String momentName) {
        this.momentName = momentName;
    }
    public MomentImages(String key, String momentName, String imageUrl,String babyName) {
        this.key = key;
        this.momentName = momentName;
        this.imageUrl = imageUrl;
        this.babyName=babyName;
    }


}
