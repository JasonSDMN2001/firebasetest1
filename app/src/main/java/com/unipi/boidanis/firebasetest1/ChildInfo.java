package com.unipi.boidanis.firebasetest1;

import java.util.Date;

public class ChildInfo {
    String name;
    Date birthDate;

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
    public ChildInfo(String name,Date birthDate){
        this.name=name;
        this.birthDate=birthDate;
    }
}
