package com.unipi.boidanis.firebasetest1;

public class WeightStatisticsData {
    float total_weight;
    int baby_pop;

    public float getTotal_weight() {
        return total_weight;
    }

    public void setTotal_weight(float total_weight) {
        this.total_weight = total_weight;
    }

    public int getBaby_pop() {
        return baby_pop;
    }

    public void setBaby_pop(int baby_pop) {
        this.baby_pop = baby_pop;
    }
    public WeightStatisticsData(){}
    public WeightStatisticsData(float total_weight,int baby_pop){
        this.total_weight=total_weight;
        this.baby_pop=baby_pop;
    }
}
