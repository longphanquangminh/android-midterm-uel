package com.phanquangminhlong.models;

import java.io.Serializable;

public class Tour implements Serializable {
    int tourId;
    String tourName;
    String tourDescription;
    int tourCount;
    String tourSchedule;
    double tourPrice;

    public Tour(int tourId, String tourName, String tourDescription, int tourCount, String tourSchedule, double tourPrice) {
        this.tourId = tourId;
        this.tourName = tourName;
        this.tourDescription = tourDescription;
        this.tourCount = tourCount;
        this.tourSchedule = tourSchedule;
        this.tourPrice = tourPrice;
    }

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getTourDescription() {
        return tourDescription;
    }

    public void setTourDescription(String tourDescription) {
        this.tourDescription = tourDescription;
    }

    public int getTourCount() {
        return tourCount;
    }

    public void setTourCount(int tourCount) {
        this.tourCount = tourCount;
    }

    public String getTourSchedule() {
        return tourSchedule;
    }

    public void setTourSchedule(String tourSchedule) {
        this.tourSchedule = tourSchedule;
    }

    public double getTourPrice() {
        return tourPrice;
    }

    public void setTourPrice(double tourPrice) {
        this.tourPrice = tourPrice;
    }

    @Override
    public String toString() {
        return tourId + " - " + tourName + " - " + tourDescription + " - " + tourCount + " - " + tourSchedule + " - " + tourPrice;
    }
}
