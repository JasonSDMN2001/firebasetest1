package com.unipi.boidanis.firebasetest1;

import java.util.Date;

public class ChildInfo {
    String name;
    Date birthDate;
    String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getbirthDate() {
        return birthDate;
    }

    public void setbirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public ChildInfo(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public ChildInfo(String name,Date birthDate,String imageUrl){
        this.name=name;
        this.birthDate=birthDate;
        this.imageUrl=imageUrl;
    }
}
