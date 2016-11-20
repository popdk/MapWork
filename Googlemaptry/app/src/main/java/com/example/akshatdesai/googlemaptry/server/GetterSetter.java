package com.example.akshatdesai.googlemaptry.server;

/**
 * Created by Akshat Desai on 11/16/2016.
 */

public class GetterSetter {

    private double[] latitude;
    private double[] longitude;
    double lat;
    double lang;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLang() {
        return lang;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }

    public double[] getLatitude() {
        return latitude;
    }

    public void setLatitude(double[] latitude) {
        this.latitude = latitude;
    }

    public double[] getLongitude() {
        return longitude;
    }

    public void setLongitude(double[] longitude) {
        this.longitude = longitude;
    }

    public String[] getTime() {
        return time;
    }

    public void setTime(String[] time) {
        this.time = time;
    }

    private String[] time;



}
