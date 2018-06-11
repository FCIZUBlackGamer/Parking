package com.example.user.parking;

public class owner_data {
    String name, address, position, lat, lan;

    public owner_data(String address, String position,String name,  String lat, String lan) {
        this.name = name;
        this.address = address;
        this.position = position;
        this.lat = lat;
        this.lan = lan;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getLan() {
        return lan;
    }

    public String getLat() {
        return lat;
    }
}
