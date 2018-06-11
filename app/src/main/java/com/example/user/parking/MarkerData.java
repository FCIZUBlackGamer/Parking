package com.example.user.parking;

/**
 * Created by USER on 4/25/2018.
 */
public class MarkerData {
    double lat;
    double lon;
    String title;

    public MarkerData(double lat, double lon, String title) {
        this.lat = lat;
        this.lon = lon;
        this.title = title;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getTitle() {
        return title;
    }
}
